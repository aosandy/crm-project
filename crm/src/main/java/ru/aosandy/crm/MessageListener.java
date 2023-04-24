package ru.aosandy.crm;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import ru.aosandy.common.client.Client;
import ru.aosandy.common.client.ClientsRepository;
import ru.aosandy.crm.service.ManagerService;

@Service
@AllArgsConstructor
public class MessageListener {

    private final ManagerService service;
    private final ClientsRepository repository;

    @JmsListener(destination = "${billing.completed.mq}")
    public void processBillingCompletedMq() {
        synchronized (service) {
            service.notifyAll();
        }
    }

    @JmsListener(destination = "${update-cache-listen.mq}")
    public void processCacheUpdateMq() {
        repository.updateCache();
    }
}
