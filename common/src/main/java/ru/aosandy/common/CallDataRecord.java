package ru.aosandy.common;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CallDataRecord implements Serializable {

    private final CallType callType;
    private final String number;
    private final LocalDateTime dateTimeStart;
    private final LocalDateTime dateTimeEnd;

    public CallDataRecord(
        CallType callType,
        String number,
        LocalDateTime dateTimeStart,
        LocalDateTime dateTimeEnd
    ) {
        this.callType = callType;
        this.number = number;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
    }

    public CallType getCallType() {
        return callType;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getDateTimeStart() {
        return dateTimeStart;
    }

    public LocalDateTime getDateTimeEnd() {
        return dateTimeEnd;
    }

    @Override
    public String toString() {
        return "CallDataRecord{" +
            "callType=" + callType +
            ", number='" + number + '\'' +
            ", dateTimeStart=" + dateTimeStart +
            ", dateTimeEnd=" + dateTimeEnd +
            '}';
    }
}
