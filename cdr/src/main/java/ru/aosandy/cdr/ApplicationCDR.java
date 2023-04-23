package ru.aosandy.cdr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.aosandy.common", "ru.aosandy.cdr"})
@EntityScan(basePackages = {"ru.aosandy.common"})
@EnableJpaRepositories(basePackages = "ru.aosandy.common")
public class ApplicationCDR {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ApplicationCDR.class, args);
        context.getBean(GeneratorCDR.class).generateAndSend();
    }
}
