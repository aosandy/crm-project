package ru.aosandy.crm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ClientData {
    private String numberPhone;
    private String password;
    private int tariffIndex;
    private double ballance;
}
