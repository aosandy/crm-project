package ru.aosandy.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Getter
public class Report implements Serializable {

    private final List<Call> calls;
    private final String number;
    private final int tariffId;
    private int totalCost;

    public Report(String number, int tariffId) {
        this.number = number;
        this.tariffId = tariffId;
        this.calls = new LinkedList<>();
    }

    public void appendCall(int callType, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Call call = new Call(callType, startDateTime, endDateTime);
        calls.add(call);
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }
}
