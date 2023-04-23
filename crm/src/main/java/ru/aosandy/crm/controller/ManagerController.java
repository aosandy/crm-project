package ru.aosandy.crm.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.aosandy.crm.payload.*;
import ru.aosandy.crm.service.ManagerService;

import java.util.List;

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
    public BillingResponse performBilling(@RequestBody BillingRequest request) {
        return service.performBilling(request);
    }
}
