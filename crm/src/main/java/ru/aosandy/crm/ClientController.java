
package ru.aosandy.crm;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aosandy.common.Report;
import ru.aosandy.crm.payload.PaymentRequest;
import ru.aosandy.crm.payload.PaymentResponse;
import ru.aosandy.crm.payload.ReportResponse;

@RestController
@RequestMapping("/abonent")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PatchMapping("/pay")
    public PaymentResponse abonentPay(@RequestBody PaymentRequest request) {
        return service.payAbonent(request);
    }

    @GetMapping("/report/{numberPhone}")
    public ReportResponse abonentReport(@PathVariable("numberPhone") String number) {
        return service.getReport(number);
    }
}
