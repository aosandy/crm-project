package ru.aosandy.crm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.aosandy.common.CommandType;

@Service
public class MessageSender {

    private final JmsTemplate jmsTemplate;
    private final String cdrMq;
    private final String updateCacheSendMq;

    public MessageSender(
        JmsTemplate jmsTemplate,
        @Value("${billing.perform.mq}") String cdrMq,
        @Value("${update-cache-send.mq}") String updateCacheSendMq
    ) {
        this.jmsTemplate = jmsTemplate;
        this.cdrMq = cdrMq;
        this.updateCacheSendMq = updateCacheSendMq;
    }

    public void sendPerformBillingCommand() {
        jmsTemplate.convertAndSend(cdrMq, CommandType.PERFORM_BILLING);
    }

    public void sendCacheUpdateCommand() {
        jmsTemplate.convertAndSend(updateCacheSendMq, CommandType.UPDATE_CACHE);
    }
}
