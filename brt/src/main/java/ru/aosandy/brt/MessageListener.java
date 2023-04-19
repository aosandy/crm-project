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

    @JmsListener(destination = "cdr")
    public void processCdrMq(@Payload List<CallDataRecord> listCdr) {
        listCdr.forEach(cdr -> log.info(cdr.toString()));
    }
}
