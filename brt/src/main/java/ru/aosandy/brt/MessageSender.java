package ru.aosandy.brt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.aosandy.common.CallDataRecordPlus;

import java.util.List;

@Service
public class MessageSender {

    private final JmsTemplate jmsTemplate;
    private final String cdrMq;

    public MessageSender(JmsTemplate jmsTemplate, @Value("${cdrplus.mq}") String cdrMq) {
        this.jmsTemplate = jmsTemplate;
        this.cdrMq = cdrMq;
    }

    public void sendMessage(List<CallDataRecordPlus> listCdr) {
        jmsTemplate.convertAndSend(cdrMq, listCdr);
    }
}
