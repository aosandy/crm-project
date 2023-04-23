
package ru.aosandy.crm.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.aosandy.crm.service.ClientService;
import ru.aosandy.crm.payload.PaymentRequest;
import ru.aosandy.crm.payload.PaymentResponse;
import ru.aosandy.crm.payload.ReportResponse;

@AllArgsConstructor
@RestController
@RequestMapping("/abonent")
public class ClientController {

    private final ClientService service;

    @PatchMapping("/pay")
    public PaymentResponse abonentPay(@RequestBody PaymentRequest request) {
        return service.payAbonent(request);
    }

    @GetMapping("/report/{numberPhone}")
    public ReportResponse abonentReport(@PathVariable("numberPhone") String number) {
        return service.getReport(number);
    }
}
