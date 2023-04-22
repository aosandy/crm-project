package ru.aosandy.common;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.SneakyThrows;
import org.postgresql.util.PGInterval;

import java.time.Duration;

@Converter
public class DurationConverter implements AttributeConverter<Duration, PGInterval> {

    @SneakyThrows
    @Override
    public PGInterval convertToDatabaseColumn(Duration duration) {
        PGInterval pgInterval = new PGInterval(0, 0, 0, 0, 0, duration.toSeconds());
        System.out.println(pgInterval);
        return pgInterval;
    }

    @Override
    public Duration convertToEntityAttribute(PGInterval pgInterval) {
        return Duration.ofSeconds(pgInterval.getWholeSeconds());
    }
}
