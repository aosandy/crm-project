package ru.aosandy.brt;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.aosandy.common.CallDataRecord;
import ru.aosandy.common.Report;
import ru.aosandy.common.client.ClientsRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageListener {

    private final ClientService service;
    private final MessageSender messageSender;
    private final ClientsRepository repository;

    @JmsListener(destination = "${cdr.mq}")
    public void processCdrMq(@Payload List<CallDataRecord> listCdr) {
        messageSender.sendCdrPlus(service.proceedCdrToCdrPlus(listCdr));
    }

    @JmsListener(destination = "${report.mq}")
    public void processReports(@Payload List<Report> listReport) {
        try {
            service.proceedReports(listReport);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @JmsListener(destination = "${update-cache-listen.mq}")
    public void processCacheUpdateMq() {
        repository.updateCache();
    }
}
