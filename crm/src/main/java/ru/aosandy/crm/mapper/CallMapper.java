package ru.aosandy.crm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.aosandy.common.Call;
import ru.aosandy.crm.payload.CallResponse;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper
public interface CallMapper extends BaseMapper {

    @Mapping(source = "callType", target = "callType", qualifiedByName = "formatCallType")
    @Mapping(source = "startDateTime", target = "startTime", qualifiedByName = "formatTime")
    @Mapping(source = "endDateTime", target = "endTime", qualifiedByName = "formatTime")
    @Mapping(source = "duration", target = "duration", qualifiedByName = "formatDuration")
    @Mapping(source = "cost", target = "cost", qualifiedByName = "convertToRubles")
    CallResponse mapCallToCallResponse(Call call);

    @Named("formatCallType")
    default String formatCallType(int source) {
        return String.format("%02d", source);
    }

    @Named("formatTime")
    default String formatDateTime(LocalDateTime source) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(source);
    }

    @Named("formatDuration")
    default String formatDuration(Duration source) {
        return String.format("%02d:%02d:%02d",
            source.toHours(),
            source.toMinutesPart(),
            source.toSecondsPart());
    }
}
