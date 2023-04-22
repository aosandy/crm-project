package ru.aosandy.common;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.SneakyThrows;
import org.postgresql.util.PGInterval;

import java.time.Duration;

@Converter
public class CallTypeConverter implements AttributeConverter<CallType, Integer> {

    @SneakyThrows
    @Override
    public Integer convertToDatabaseColumn(CallType callType) {
        return callType.getIndex();
    }

    @Override
    public CallType convertToEntityAttribute(Integer id) {
        return CallType.valueOfIndex(id);
    }
}
