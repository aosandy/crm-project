package ru.aosand.hrs;

import lombok.Getter;
import ru.aosandy.common.CallType;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Getter
public class ClientData {

    private final Set<Call> calls;
    private final String number;
    private final int tariffId;

    public ClientData(String number, int tariffId) {
        this.number = number;
        this.tariffId = tariffId;
        this.calls = new TreeSet<>(Comparator.comparing(Call::getTimeStart));
    }

    public void appendCall(CallType callType, LocalDateTime timeStart, LocalDateTime timeEnd) {
        Call call = new Call(callType, timeStart, timeEnd);
        calls.add(call);
    }
}
