package ru.aosandy.common.client;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.aosandy.common.BillingPeriod;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "clients")
@NoArgsConstructor
@Getter
@Setter
public class Client implements Serializable, UserDetails {

    private static final String ROLE_NAME = "CLIENT";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String number;

    @Column
    private String password;

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

    public Client(String number, String password, int tariffId, int balance) {
        this.number = number;
        this.password = password;
        this.tariffId = tariffId;
        this.balance = balance;
    }

    public void incrementOperationsCount() {
        operationsCount += 1;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ROLE_NAME);
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return number;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
