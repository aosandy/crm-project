package ru.aosandy.common.client;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.aosandy.common.BillingPeriod;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "clients")
@NoArgsConstructor
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

    public Client(String number, int tariffId, int balance) {
        this.number = number;
        this.tariffId = tariffId;
        this.balance = balance;
    }

    public void incrementOperationsCount() {
        operationsCount += 1;
    }
}
