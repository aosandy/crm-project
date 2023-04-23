package ru.aosandy.brt;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.aosandy.common.BillingPeriod;
import ru.aosandy.common.CallDataRecord;
import ru.aosandy.common.CallDataRecordPlus;
import ru.aosandy.common.Report;
import ru.aosandy.common.client.Client;
import ru.aosandy.common.client.ClientsRepository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ClientService {

    private final ClientsRepository repository;
    private final MessageSender messageSender;

    public List<CallDataRecordPlus> proceedCdrToCdrPlus(List<CallDataRecord> listCdr) {
        Map<String, Client> clientsMap = repository.findAll().stream()
            .collect(Collectors.toMap(Client::getNumber, Function.identity()));
        return listCdr.stream()
            .filter(cdr -> clientsMap.containsKey(cdr.getNumber()))
            .filter(cdr -> clientsMap.get(cdr.getNumber()).getBalance() > 0)
            .map(cdr -> new CallDataRecordPlus(cdr, clientsMap.get(cdr.getNumber()).getTariffId()))
            .toList();
    }

    public void proceedReports(List<Report> listReport) {
        for (Report report : listReport) {
            BillingPeriod billingPeriod = new BillingPeriod();
            Client client = repository.getClientByNumber(report.getNumber());
            billingPeriod.setTotalCost(report.getTotalCost());
            billingPeriod.setClient(client);
            report.getCalls().forEach(call -> call.setBillingPeriod(billingPeriod));
            billingPeriod.setCalls(report.getCalls().stream().toList());
            client.getBillingPeriods().add(billingPeriod);
            client.setBalance(client.getBalance() - report.getTotalCost());
            repository.save(client);
        }
        messageSender.sendBillingCompletedMessage();
    }
}
