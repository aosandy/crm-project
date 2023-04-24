package ru.aosandy.brt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.aosandy.common", "ru.aosandy.brt"})
@EntityScan(basePackages = {"ru.aosandy.common"})
@EnableJpaRepositories(basePackages = "ru.aosandy.common")
@EnableCaching
public class ApplicationBRT {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationBRT.class, args);
    }
}
