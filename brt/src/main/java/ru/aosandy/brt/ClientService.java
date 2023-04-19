package ru.aosandy.brt;

import org.springframework.stereotype.Service;
import ru.aosandy.common.CallDataRecord;
import ru.aosandy.common.CallDataRecordPlus;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientsRepository repository;
    private final MessageSender messageSender;

    public ClientService(ClientsRepository repository, MessageSender messageSender) {
        this.repository = repository;
        this.messageSender = messageSender;
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

    public void sendCdrPlus(List<CallDataRecordPlus> listCdrPlus) {
        messageSender.sendMessage(listCdrPlus);
    }
}
