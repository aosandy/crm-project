package ru.aosandy.hrs.tariff;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tariffs")
@Getter
@Setter
public class Tariff {

    @Id
    private int id;

    @Column
    private String name;
}
