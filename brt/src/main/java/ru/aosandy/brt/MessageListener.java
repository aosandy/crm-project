package ru.aosandy.brt;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.aosandy.common.CallDataRecord;
import ru.aosandy.common.Report;

import java.util.List;

@Service
@Slf4j
public class MessageListener {

    private final ClientService service;
    private final MessageSender messageSender;

    public MessageListener(ClientService service, MessageSender messageSender) {
        this.service = service;
        this.messageSender = messageSender;
    }

    @JmsListener(destination = "${cdr.mq}")
    public void processCdrMq(@Payload List<CallDataRecord> listCdr) {
        messageSender.sendMessage(service.proceedCdrToCdrPlus(listCdr));
    }

    @JmsListener(destination = "${report.mq}")
    public void processReports(@Payload List<Report> listReport) {
        try {
            service.proceedReports(listReport);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
