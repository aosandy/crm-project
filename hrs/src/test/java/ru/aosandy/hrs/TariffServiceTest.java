package ru.aosandy.hrs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.aosandy.common.Report;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(TariffTestcontainerExtension.class)
@ContextConfiguration(initializers = {TariffTestcontainerExtension.Initializer.class})
class TariffServiceTest {

    @Autowired
    TariffService service;

    @Test
    void unlimited300TariffTest() {
        Report report = new Report("71234567890", 6);
        report.appendCall(
            1,
            LocalDateTime.of(2023, 1, 1, 0, 0, 0),
            LocalDateTime.of(2023, 1, 1, 5, 1, 9)
        );
        report.appendCall(
            2,
            LocalDateTime.of(2023, 2, 1, 1, 0, 0),
            LocalDateTime.of(2023, 2, 1, 1, 0, 59)
        );
        report.appendCall(
            1,
            LocalDateTime.of(2023, 3, 1, 1, 0, 0),
            LocalDateTime.of(2023, 3, 1, 1, 1, 59)
        );
        service.calculatePriceForReport(report);
        double expected = 100 + 2 + 1 + 2;
        double actual = report.getTotalCost() / 100.0;
        assertEquals(expected, actual);
    }

    @Test
    void minuteByMinuteTariffTest() {
        Report report = new Report("71234567890", 3);
        report.appendCall(
            2,
            LocalDateTime.of(2023, 1, 1, 0, 0, 0),
            LocalDateTime.of(2023, 1, 1, 0, 2, 9)
        );
        report.appendCall(
            1,
            LocalDateTime.of(2023, 2, 1, 1, 0, 0),
            LocalDateTime.of(2023, 2, 1, 1, 0, 59)
        );
        report.appendCall(
            2,
            LocalDateTime.of(2023, 3, 1, 1, 0, 0),
            LocalDateTime.of(2023, 3, 1, 2, 1, 59)
        );
        service.calculatePriceForReport(report);
        double expected = 3 * 1.5 + 1.5 + 62 * 1.5;
        double actual = report.getTotalCost() / 100.0;
        assertEquals(expected, actual);
    }

    @Test
    void regularTariffTest() {
        Report report = new Report("71234567890", 11);
        report.appendCall(
            2,
            LocalDateTime.of(2023, 1, 1, 0, 0, 0),
            LocalDateTime.of(2023, 1, 1, 3, 2, 9)
        );
        report.appendCall(
            1,
            LocalDateTime.of(2023, 2, 1, 1, 0, 0),
            LocalDateTime.of(2023, 2, 1, 1, 49, 30)
        );
        report.appendCall(
            1,
            LocalDateTime.of(2023, 3, 1, 1, 0, 0),
            LocalDateTime.of(2023, 3, 1, 2, 1, 59)
        );
        service.calculatePriceForReport(report);
        double expected = 50 * 0.5 + 50 * 0.5 + 12 * 1.5;
        double actual = report.getTotalCost() / 100.0;
        assertEquals(expected, actual);
    }
}
