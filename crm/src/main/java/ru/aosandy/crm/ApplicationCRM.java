package ru.aosandy.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.aosandy.common", "ru.aosandy.crm"})
@EntityScan(basePackages = {"ru.aosandy.common"})
@EnableJpaRepositories(basePackages = "ru.aosandy.common")
public class ApplicationCRM {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationCRM.class, args);
    }
}
