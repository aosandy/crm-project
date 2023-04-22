package ru.aosandy.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.aosandy.common.BillingPeriod;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "clients")
@Getter
@Setter
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String number;

    @Column
    private int balance;

    @Column
    private int tariffId;

    @Column
    private int operationsCount;

    @OneToMany(
        mappedBy = "client", cascade = CascadeType.ALL,
        orphanRemoval = true, fetch = FetchType.EAGER
    )
    private List<BillingPeriod> billingPeriods;
}
