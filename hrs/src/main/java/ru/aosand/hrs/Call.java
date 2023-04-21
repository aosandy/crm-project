package ru.aosand.hrs;

import lombok.Getter;
import ru.aosandy.common.CallType;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class Call {
    private final CallType callType;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private Duration duration;

    public Call(CallType callType, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.callType = callType;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.duration = Duration.between(startDateTime, endDateTime);
    }
}
