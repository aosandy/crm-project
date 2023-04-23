package ru.aosandy.crm.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.aosandy.common.*;
import ru.aosandy.crm.mapper.PaymentMapper;
import ru.aosandy.crm.mapper.ReportMapper;
import ru.aosandy.crm.payload.PaymentRequest;
import ru.aosandy.crm.payload.PaymentResponse;
import ru.aosandy.crm.payload.ReportResponse;

import java.util.*;

@Service
@AllArgsConstructor
public class ClientService {

    private final ClientsRepository repository;
    private final PaymentMapper paymentMapper;
    private final ReportMapper reportMapper;

    public PaymentResponse payAbonent(PaymentRequest request) {
        Client client = repository.getClientByNumber(request.getNumberPhone());
        client.setBalance((int) (client.getBalance() + (request.getMoney() * 100)));
        client.incrementOperationsCount();
        repository.save(client);
        return paymentMapper.mapClientToPaymentResponse(client);
    }

    public ReportResponse getReport(String number) {
        Client client = repository.getClientByNumber(number);
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
}
