package ru.aosandy.crm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.aosandy.common.Client;
import ru.aosandy.crm.payload.PaymentResponse;

@Mapper
public interface PaymentMapper extends BaseMapper {

    @Mapping(source = "operationsCount", target = "id")
    @Mapping(source = "number", target = "numberPhone")
    @Mapping(source = "balance", target = "money", qualifiedByName = "convertToRubles")
    PaymentResponse mapClientToPaymentResponse(Client client);
}
