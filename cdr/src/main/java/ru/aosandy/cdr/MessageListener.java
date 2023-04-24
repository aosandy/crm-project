package ru.aosandy.cdr;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MessageListener {

    private final GeneratorCDR generator;

    @JmsListener(destination = "${billing.perform.mq}")
    public void processPerformBillingMq() {
        generator.generateAndSend();
    }
}
