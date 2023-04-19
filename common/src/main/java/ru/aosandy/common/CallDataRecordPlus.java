package ru.aosandy.common;

import java.io.Serializable;

public class CallDataRecordPlus implements Serializable {

    private final CallDataRecord callDataRecord;
    private final int tariffId;

    public CallDataRecordPlus(CallDataRecord callDataRecord, int tariffId) {
        this.callDataRecord = callDataRecord;
        this.tariffId = tariffId;
    }

    public CallDataRecord getCallDataRecord() {
        return callDataRecord;
    }

    public int getTariffId() {
        return tariffId;
    }

    @Override
    public String toString() {
        return "CallDataRecordPlus{" +
            "callDataRecord=" + callDataRecord +
            ", tariffId=" + tariffId +
            '}';
    }
}
