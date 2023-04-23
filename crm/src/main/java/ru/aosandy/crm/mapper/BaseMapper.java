package ru.aosandy.crm.mapper;

import org.mapstruct.Named;

public interface BaseMapper {

    @Named("convertToRubles")
    default double convertToRubles(int source) {
        return source / 100.0;
    }
}
