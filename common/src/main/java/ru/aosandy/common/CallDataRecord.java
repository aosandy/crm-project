package ru.aosandy.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CallDataRecord implements Serializable {
    private final int callType;
    private final String number;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
}
