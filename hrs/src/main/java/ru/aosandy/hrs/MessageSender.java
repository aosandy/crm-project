package ru.aosandy.hrs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.aosandy.common.Report;

import java.util.List;

@Service
public class MessageSender {

    private final JmsTemplate jmsTemplate;
    private final String reportMq;

    public MessageSender(JmsTemplate jmsTemplate, @Value("${report.mq}") String reportMq) {
        this.jmsTemplate = jmsTemplate;
        this.reportMq = reportMq;
    }

    public void sendMessage(List<Report> listReport) {
        jmsTemplate.convertAndSend(reportMq, listReport);
    }
}
