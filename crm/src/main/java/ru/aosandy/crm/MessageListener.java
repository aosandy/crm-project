package ru.aosandy.crm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import ru.aosandy.crm.service.ManagerService;

@Service
@Slf4j
public class MessageListener {

    private final ManagerService service;

    public MessageListener(ManagerService service) {
        this.service = service;
    }

    @JmsListener(destination = "${billing.completed.mq}")
    public void processReports() {
        synchronized (service) {
            service.notifyAll();
        }
    }
}
