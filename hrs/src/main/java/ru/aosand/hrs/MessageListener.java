package ru.aosand.hrs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.aosandy.common.CallDataRecordPlus;

import java.util.List;

@Service
@Slf4j
public class MessageListener {

    @JmsListener(destination = "${cdrplus.mq}")
    public void processCdrMq(@Payload List<CallDataRecordPlus> listCdrPlus) {
        listCdrPlus.forEach(cdr -> log.info(cdr.toString()));
    }
}
