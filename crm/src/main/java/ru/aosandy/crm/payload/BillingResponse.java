package ru.aosandy.crm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BillingResponse {
    private List<BillingNumberResponse> numbers;
}
