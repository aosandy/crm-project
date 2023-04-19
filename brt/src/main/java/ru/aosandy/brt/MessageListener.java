package ru.aosandy.brt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.aosandy.common.CallDataRecord;

import java.util.List;

@Service
@Slf4j
public class MessageListener {

    private final ClientService service;

    public MessageListener(ClientService service) {
        this.service = service;
    }

    @JmsListener(destination = "cdr")
    public void processCdrMq(@Payload List<CallDataRecord> listCdr) {
        service.proceedCdrToCdrPlus(listCdr);
    }
}
