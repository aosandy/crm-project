package ru.aosand.hrs;

import lombok.Data;
import lombok.Getter;
import ru.aosandy.common.CallType;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class Call {
    private final CallType callType;
    private final LocalDateTime timeStart;
    private final LocalDateTime timeEnd;
    private Duration duration;

    public Call(CallType callType, LocalDateTime timeStart, LocalDateTime timeEnd) {
        this.callType = callType;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.duration = Duration.between(timeStart, timeEnd);
    }
}
