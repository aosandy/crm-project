package ru.aosandy.hrs.tariff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StartPeriodChoiceRepository extends JpaRepository<StartPeriodChoice, Integer> {

    List<StartPeriodChoice> findAllByTariffId(int tariffId);
}
