package ru.aosandy.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.postgresql.util.PGInterval;
import org.springframework.data.relational.core.dialect.PostgresDialect;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "calls")
@Getter
@Setter
public class Call implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "call_type_id")
    @Convert(converter = CallTypeConverter.class)
    private CallType callType;

    @Column(name = "start_time")
    private LocalDateTime startDateTime;

    @Column(name = "end_time")
    private LocalDateTime endDateTime;

    @Column
    //@Convert(converter = DurationConverter.class)
    private Duration duration;

    @Column
    private int cost;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "billing_period_id")
    private BillingPeriod billingPeriod;

    public Call() {
    }

    public Call(CallType callType, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.callType = callType;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.duration = Duration.between(startDateTime, endDateTime);
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
