package ru.aosandy.crm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    private final JmsTemplate jmsTemplate;
    private final String queueName;
    private final String performBillingMsg;

    public MessageSender(
        JmsTemplate jmsTemplate,
        @Value("${billing.perform.mq}") String cdrMq,
        @Value("${billing.perform.message}") String performBillingMsg
    ) {
        this.jmsTemplate = jmsTemplate;
        this.queueName = cdrMq;
        this.performBillingMsg = performBillingMsg;
    }

    public void sendPerformBillingMessage() {
        jmsTemplate.convertAndSend(queueName, performBillingMsg);
    }
}
