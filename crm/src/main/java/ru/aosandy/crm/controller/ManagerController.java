package ru.aosandy.crm.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aosandy.crm.payload.BillingRequest;
import ru.aosandy.crm.payload.ClientData;
import ru.aosandy.crm.payload.TariffChangeResponse;
import ru.aosandy.crm.service.ManagerService;

@AllArgsConstructor
@RestController
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService service;

    @PatchMapping("/changeTariff")
    public TariffChangeResponse changeTariff(@RequestBody ClientData request) {
        return service.changeTariff(request);
    }

    @PostMapping("/abonent")
    public ClientData createClient(@RequestBody ClientData request) {
        return service.createClient(request);
    }

    @PatchMapping("/billing")
    public ResponseEntity performBilling(@RequestBody BillingRequest request) {
        try {
            return ResponseEntity.ok(service.performBilling(request));
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
