package ru.aosandy.cdr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.aosandy.common.CallDataRecord;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class ApplicationCDR {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ApplicationCDR.class, args);
        MessageSender sender = context.getBean(MessageSender.class);

        Generator generator = new Generator();
        List<CallDataRecord> generatedList = generator.generateCDRs(1);
        sender.sendMessage(generatedList);

        try {
            CDRFileBuilder.buildReport(generatedList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
