package ru.aosandy.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "billing_periods")
@Getter
@Setter
public class BillingPeriod implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int totalCost;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(
        mappedBy = "billingPeriod", cascade = CascadeType.ALL,
        orphanRemoval = true, fetch = FetchType.EAGER
    )
    private List<Call> calls;
}
