package ru.aosandy.hrs;

import org.junit.jupiter.api.extension.Extension;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TariffTestcontainerExtension implements Extension {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer;
    static {
        postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2")
            .withDatabaseName("tariff-test")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("tariffs-init-script.sql");
        postgreSQLContainer.start();
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                "DATABASE_USERNAME=" + postgreSQLContainer.getUsername(),
                "DATABASE_PASSWORD=" + postgreSQLContainer.getPassword(),
                "DATABASE_URL=" + postgreSQLContainer.getJdbcUrl()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
