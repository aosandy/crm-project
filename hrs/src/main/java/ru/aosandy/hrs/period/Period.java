package ru.aosandy.hrs.period;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "periods")
@Getter
@Setter
public class Period {

    @Id
    private int id;

    @Column
    @Nullable
    private Integer minuteLimit;

    @Column
    private int fixPrice;

    @Column
    private int pricePerMinute;

    @Column
    @Nullable
    private Integer nextPeriod;
}
