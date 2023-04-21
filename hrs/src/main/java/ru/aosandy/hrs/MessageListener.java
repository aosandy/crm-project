package ru.aosandy.hrs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.aosandy.common.CallDataRecordPlus;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MessageListener {

    private final TariffService service;

    public MessageListener(TariffService service) {
        this.service = service;
    }

    @JmsListener(destination = "${cdrplus.mq}")
    public void processCdrMq(@Payload List<CallDataRecordPlus> listCdrPlus) {
          Map<String, Integer> bills = service.calculateBillsMap(listCdrPlus);
    }
}
