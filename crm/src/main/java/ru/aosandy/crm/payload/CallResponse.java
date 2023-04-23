package ru.aosandy.crm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CallResponse {
    private String callType;
    private String startTime;
    private String endTime;
    private String duration;
    private double cost;
}
