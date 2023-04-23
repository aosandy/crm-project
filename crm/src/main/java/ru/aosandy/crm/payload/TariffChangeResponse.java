package ru.aosandy.crm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TariffChangeResponse {
    private int id;
    private String numberPhone;
    private int tariffIndex;
}
