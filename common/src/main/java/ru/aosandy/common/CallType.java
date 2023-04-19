package ru.aosandy.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum CallType {
    OUTGOING(1),
    INCOMING(2);

    private static final Map<Integer, CallType> BY_INDEX = new HashMap<>();

    static {
        for (CallType callType : values()) {
            BY_INDEX.put(callType.index, callType);
        }
    }

    private final int index;

    CallType(int index) {
        this.index = index;
    }

    public static CallType valueOfIndex(int index) {
        return BY_INDEX.get(index);
    }

    public int getIndex() {
        return index;
    }
}
