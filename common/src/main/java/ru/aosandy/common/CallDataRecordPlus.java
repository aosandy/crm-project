package ru.aosandy.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class CallDataRecordPlus implements Serializable {
    private final CallDataRecord callDataRecord;
    private final int tariffId;
}
