package ru.aosandy.crm.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.aosandy.common.client.Client;
import ru.aosandy.common.client.ClientsRepository;
import ru.aosandy.crm.MessageSender;
import ru.aosandy.crm.payload.*;

import java.util.Objects;

@Service
@AllArgsConstructor
public class ManagerService {

    private final ClientsRepository repository;
    private final MessageSender messageSender;

    public TariffChangeResponse changeTariff(ClientData request) {
        Client client = repository.getClientByNumber(request.getNumberPhone());
        client.setTariffId(request.getTariffIndex());
        client.incrementOperationsCount();
        repository.save(client);
        return new TariffChangeResponse(client.getOperationsCount(), client.getNumber(), client.getTariffId());
    }

    public ClientData createClient(ClientData clientData) {
        Client client = new Client(
            clientData.getNumberPhone(),
            clientData.getTariffIndex(),
            (int) (clientData.getBallance() * 100)
        );
        repository.save(client);
        return clientData;
    }

    public synchronized BillingResponse performBilling(BillingRequest request) {
        if (!Objects.equals(request.getAction(), "run")) {
            throw new IllegalStateException();
        }
        messageSender.sendPerformBillingMessage();
        boolean wait = true;
        while (wait) {
            wait = false;
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
        return new BillingResponse(
            repository.findAll().stream()
                .map(client -> new BillingNumberResponse(client.getNumber(), client.getBalance() / 100.0))
                .toList()
        );
    }
}
