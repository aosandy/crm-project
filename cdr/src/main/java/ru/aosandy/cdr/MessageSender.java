package ru.aosandy.cdr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.aosandy.common.CallDataRecord;

import java.util.List;

@Service
public class MessageSender {

    private final JmsTemplate jmsTemplate;
    private final String cdrMq;

    public MessageSender(JmsTemplate jmsTemplate, @Value("${cdr.mq}") String cdrMq) {
        this.jmsTemplate = jmsTemplate;
        this.cdrMq = cdrMq;
    }

    public void sendCdr(List<CallDataRecord> listCdr) {
        jmsTemplate.convertAndSend(cdrMq, listCdr);
    }
}
