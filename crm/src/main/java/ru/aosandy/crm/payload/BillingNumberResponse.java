package ru.aosandy.crm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BillingNumberResponse {
    private String numberPhone;
    private double ballance;
}
