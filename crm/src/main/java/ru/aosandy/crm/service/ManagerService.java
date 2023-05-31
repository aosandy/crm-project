package ru.aosandy.crm.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.aosandy.common.client.Client;
import ru.aosandy.common.client.ClientsRepository;
import ru.aosandy.crm.MessageSender;
import ru.aosandy.crm.payload.*;
import ru.aosandy.crm.user.User;
import ru.aosandy.crm.user.UserRepository;
import ru.aosandy.crm.user.UserRoleRepository;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ManagerService {

    private final ClientsRepository clientRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final MessageSender messageSender;
    private final BCryptPasswordEncoder passwordEncoder;

    public TariffChangeResponse changeTariff(ClientData request) {
        Client client = clientRepository.findByNumber(request.getNumberPhone())
            .orElseThrow(() -> new UsernameNotFoundException("Client not found"));
        client.setTariffId(request.getTariffIndex());
        client.incrementOperationsCount();
        clientRepository.save(client);
        updateCache();
        messageSender.sendCacheUpdateCommand();

        return new TariffChangeResponse(client.getOperationsCount(), client.getNumber(), client.getTariffId());
    }

    public ClientData createClient(ClientData clientData) {
        Optional<Client> foundClient = clientRepository.findByNumber(clientData.getNumberPhone());
        if (foundClient.isPresent()) {
            throw new IllegalStateException("Client already exist");
        }
        String encodedPassword = passwordEncoder.encode(clientData.getPassword());
        User user = new User(
            clientData.getNumberPhone(),
            encodedPassword,
            userRoleRepository.findByName("CLIENT")
                .orElseThrow(() -> new UsernameNotFoundException("Role not found"))
        );
        Client client = new Client(
            clientData.getNumberPhone(),
            clientData.getTariffIndex(),
            (int) (clientData.getBallance() * 100)
        );
        userRepository.save(user);
        clientRepository.save(client);
        updateCache();
        messageSender.sendCacheUpdateCommand();

        clientData.setPassword(encodedPassword);
        return clientData;
    }

    public synchronized BillingResponse performBilling(BillingRequest request) throws InterruptedException {
        if (!Objects.equals(request.getAction(), "run")) {
            throw new IllegalStateException();
        }

        // Записываем клиентов до тарификации, чтобы вывести в отчете только тех, чьи балансы изменились
        Map<String, Integer> oldClientsMap =  clientRepository.findAll().stream()
                .collect(Collectors.toMap(Client::getNumber, Client::getBalance));

        messageSender.sendPerformBillingCommand();
        boolean wait = true;
        while (wait) {
            wait = false;
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw e;
            }
        }
        updateCache();
        return new BillingResponse(
            clientRepository.findAll().stream()
                .filter(client -> oldClientsMap.get(client.getNumber()) != client.getBalance())
                .map(client -> new BillingNumberResponse(client.getNumber(), client.getBalance() / 100.0))
                .toList()
        );
    }

    private void updateCache() {
        clientRepository.updateCache();
    }
}
