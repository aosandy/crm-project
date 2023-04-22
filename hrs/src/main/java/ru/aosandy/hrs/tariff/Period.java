package ru.aosandy.hrs.tariff;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "periods")
@Getter
@Setter
public class Period {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @Nullable
    private Integer minuteLimit;

    @Column
    private int fixCost;

    @Column
    private int pricePerMinute;

    @Column
    @Nullable
    private Integer nextPeriodId;
}
