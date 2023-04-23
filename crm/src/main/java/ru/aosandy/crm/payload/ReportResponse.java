package ru.aosandy.crm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ReportResponse {
    private int id;
    private String numberPhone;
    private String tariffIndex;
    private List<CallResponse> payload;
    private double totalCost;
    private String monetaryUnit;
}
