package ru.aosandy.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "calls")
@NoArgsConstructor
@Getter
@Setter
public class Call implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "call_type_id")
    private int callType;

    @Column(name = "start_time")
    private LocalDateTime startDateTime;

    @Column(name = "end_time")
    private LocalDateTime endDateTime;

    @Column
    private Duration duration;

    @Column
    private int cost;

    //@ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "billing_period_id")
    //private BillingPeriod billingPeriod;

    public Call(int callType, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.callType = callType;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.duration = Duration.between(startDateTime, endDateTime);
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
