package ru.aosandy.hrs.tariff;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "start_period_choice")
@Getter
@Setter
public class StartPeriodChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int tariffId;

    @Column
    private int callTypeId;

    @Column
    private boolean isIntranet;

    @Column
    private int startPeriodId;
}
