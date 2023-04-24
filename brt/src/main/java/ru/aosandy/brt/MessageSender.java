package ru.aosandy.brt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.aosandy.common.CallDataRecordPlus;
import ru.aosandy.common.CommandType;

import java.util.List;

@Service
public class MessageSender {

    private final JmsTemplate jmsTemplate;
    private final String cdrMq;
    private final String billingCompletedMq;
    private final String updateCacheSendMq;

    public MessageSender(
        JmsTemplate jmsTemplate,
        @Value("${cdrplus.mq}") String cdrMq,
        @Value("${billing.completed.mq}") String billingCompletedMq,
        @Value("update-cache-send.mq") String updateCacheSendMq
    ) {
        this.jmsTemplate = jmsTemplate;
        this.cdrMq = cdrMq;
        this.billingCompletedMq = billingCompletedMq;
        this.updateCacheSendMq = updateCacheSendMq;
    }

    public void sendCdrPlus(List<CallDataRecordPlus> listCdr) {
        jmsTemplate.convertAndSend(cdrMq, listCdr);
    }

    public void sendBillingCompletedCommand() {
        jmsTemplate.convertAndSend(billingCompletedMq, CommandType.BILLING_COMPLETED);
    }

    public void sendCacheUpdateCommand() {
        jmsTemplate.convertAndSend(updateCacheSendMq, CommandType.UPDATE_CACHE);
    }
}
