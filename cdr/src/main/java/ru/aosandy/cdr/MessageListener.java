package ru.aosandy.cdr;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import ru.aosandy.common.CallDataRecord;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class MessageListener {

    private final Generator generator;
    private final MessageSender messageSender;

    @JmsListener(destination = "${billing.perform.mq}")
    public void processReports() {
        List<CallDataRecord> generatedList = generator.generateCDRs(10);
        messageSender.sendMessage(generatedList);
    }
}
