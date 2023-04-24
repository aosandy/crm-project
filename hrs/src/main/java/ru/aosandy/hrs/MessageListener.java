package ru.aosandy.hrs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.aosandy.common.CallDataRecordPlus;

import java.util.List;

@Service
@Slf4j
public class MessageListener {

    private final TariffService service;
    private final MessageSender messageSender;

    public MessageListener(TariffService service, MessageSender messageSender) {
        this.service = service;
        this.messageSender = messageSender;
    }

    @JmsListener(destination = "${cdrplus.mq}")
    public void processCdrMq(@Payload List<CallDataRecordPlus> listCdrPlus) {
        messageSender.sendReports(service.calculateBilling(listCdrPlus));
    }
}
