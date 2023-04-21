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
        this.calls = new TreeSet<>(Comparator.comparing(Call::getStartDateTime));
    }

    public void appendCall(CallType callType, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Call call = new Call(callType, startDateTime, endDateTime);
        calls.add(call);
    }
}
