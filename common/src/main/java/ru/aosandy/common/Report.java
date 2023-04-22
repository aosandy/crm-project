package ru.aosandy.common;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Getter
public class Report implements Serializable {

    private final List<Call> calls;
    private final String number;
    private final int tariffId;
    private int totalPrice;

    public Report(String number, int tariffId) {
        this.number = number;
        this.tariffId = tariffId;
        this.calls = new LinkedList<>();
    }

    public void appendCall(CallType callType, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Call call = new Call(callType, startDateTime, endDateTime);
        calls.add(call);
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
