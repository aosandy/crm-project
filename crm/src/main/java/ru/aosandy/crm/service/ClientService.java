package ru.aosandy.crm.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.aosandy.common.BillingPeriod;
import ru.aosandy.common.Call;
import ru.aosandy.common.Report;
import ru.aosandy.common.client.Client;
import ru.aosandy.common.client.ClientsRepository;
import ru.aosandy.crm.MessageListener;
import ru.aosandy.crm.MessageSender;
import ru.aosandy.crm.mapper.PaymentMapper;
import ru.aosandy.crm.mapper.ReportMapper;
import ru.aosandy.crm.payload.PaymentRequest;
import ru.aosandy.crm.payload.PaymentResponse;
import ru.aosandy.crm.payload.ReportResponse;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class ClientService {

    private final ClientsRepository repository;
    private final PaymentMapper paymentMapper;
    private final ReportMapper reportMapper;
    private final MessageSender messageSender;

    public PaymentResponse abonentPay(PaymentRequest request) {
        Client client = repository.findByNumber(request.getNumberPhone())
            .orElseThrow(() -> new UsernameNotFoundException("Client not found"));
        client.setBalance((int) (client.getBalance() + (request.getMoney() * 100)));
        client.incrementOperationsCount();
        repository.save(client);
        updateCache();
        messageSender.sendCacheUpdateCommand();

        return paymentMapper.mapClientToPaymentResponse(client);
    }

    public ReportResponse abonentReport(String number) {
        Client client = repository.findByNumber(number)
            .orElseThrow(() -> new UsernameNotFoundException("Client not found"));
        List<Call> calls = new LinkedList<>();
        int totalCost = 0;
        for (BillingPeriod billingPeriod : client.getBillingPeriods()) {
            calls.addAll(billingPeriod.getCalls());
            totalCost += billingPeriod.getTotalCost();
        }
        calls.sort(Comparator.comparing(Call::getStartDateTime));
        Report report = new Report(calls, number, client.getTariffId(), totalCost);

        ReportResponse response = reportMapper.mapReportToReportResponse(report);
        response.setId(client.getId());
        return response;
    }

    private void updateCache() {
        repository.updateCache();
    }
}
