package ru.aosandy.crm.mapper;

import org.mapstruct.*;
import ru.aosandy.common.Report;
import ru.aosandy.crm.payload.ReportResponse;

import java.time.Duration;

@Mapper(uses = CallMapper.class)
public interface ReportMapper {

    @Mapping(source = "number", target = "numberPhone")
    @Mapping(source = "tariffId", target = "tariffIndex", qualifiedByName = "formatTariffIndex")
    @Mapping(source = "calls", target = "payload")
    @Mapping(source = "totalCost", target = "totalCost", qualifiedByName = "convertToRubles")
    ReportResponse mapReportToReportResponse(Report report);

    @Named("formatTariffIndex")
    default String formatCallType(int source) {
        return String.format("%02d", source);
    }

    @Named("formatDuration")
    default String formatDuration(Duration source) {
        return String.format("%02d:%02d:%02d",
            source.toHours(),
            source.toMinutesPart(),
            source.toSecondsPart());
    }

    @AfterMapping
    default void setMonetaryUnit(@MappingTarget ReportResponse response) {
        response.setMonetaryUnit("rubles");
    }
}
