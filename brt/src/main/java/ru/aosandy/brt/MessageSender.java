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
    private final String billingCompletedMq;
    private final String billingCompletedMsg;

    public MessageSender(
        JmsTemplate jmsTemplate,
        @Value("${cdrplus.mq}") String cdrMq,
        @Value("${billing.completed.mq}") String billingCompletedMq,
        @Value("${billing.completed.message}") String billingCompletedMsg
    ) {
        this.jmsTemplate = jmsTemplate;
        this.cdrMq = cdrMq;
        this.billingCompletedMq = billingCompletedMq;
        this.billingCompletedMsg = billingCompletedMsg;
    }

    public void sendMessage(List<CallDataRecordPlus> listCdr) {
        jmsTemplate.convertAndSend(cdrMq, listCdr);
    }

    public void sendBillingCompletedMessage() {
        jmsTemplate.convertAndSend(billingCompletedMq, billingCompletedMsg);
    }
}
