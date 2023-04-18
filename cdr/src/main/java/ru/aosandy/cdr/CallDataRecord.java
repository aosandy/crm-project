package ru.aosandy.cdr;

import java.time.LocalDateTime;

public class CallDataRecord {

    private CallType callType;
    private String number;
    private LocalDateTime dateTimeStart;
    private LocalDateTime dateTimeEnd;

    public CallDataRecord(CallType callType, String number, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd) {
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
}
