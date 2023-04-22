package ru.aosandy.brt;

import org.springframework.stereotype.Service;
import ru.aosandy.common.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientsRepository repository;

    public ClientService(ClientsRepository repository) {
        this.repository = repository;
    }

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
            repository.save(client);
        }
    }
}
