package ru.aosandy.crm.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.aosandy.common.client.Client;
import ru.aosandy.common.client.ClientsRepository;
import ru.aosandy.crm.MessageSender;
import ru.aosandy.crm.manager.ManagerRepository;
import ru.aosandy.crm.payload.*;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ManagerService implements UserDetailsService {

    private final ClientsRepository clientRepository;
    private final ManagerRepository managerRepository;
    private final MessageSender messageSender;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return managerRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Manager not found"));
    }

    public TariffChangeResponse changeTariff(ClientData request) {
        Client client = clientRepository.findByNumber(request.getNumberPhone())
            .orElseThrow(() -> new UsernameNotFoundException("Client not found"));
        client.setTariffId(request.getTariffIndex());
        client.incrementOperationsCount();
        clientRepository.save(client);
        return new TariffChangeResponse(client.getOperationsCount(), client.getNumber(), client.getTariffId());
    }

    public ClientData createClient(ClientData clientData) {
        Optional<Client> foundClient = clientRepository.findByNumber(clientData.getNumberPhone());
        if (foundClient.isPresent()) {
            throw new IllegalStateException("Client already exist");
        }
        String encodedPassword = passwordEncoder.encode(clientData.getPassword());
        Client client = new Client(
            clientData.getNumberPhone(),
            encodedPassword,
            clientData.getTariffIndex(),
            (int) (clientData.getBallance() * 100)
        );
        clientRepository.save(client);
        clientData.setPassword(encodedPassword);
        return clientData;
    }

    public synchronized BillingResponse performBilling(BillingRequest request) throws InterruptedException {
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
                throw e;
            }
        }
        return new BillingResponse(
            clientRepository.findAll().stream()
                .map(client -> new BillingNumberResponse(client.getNumber(), client.getBalance() / 100.0))
                .toList()
        );
    }
}
