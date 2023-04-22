package ru.aosandy.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.aosandy.common.Client;

@Repository
public interface ClientsRepository extends JpaRepository<Client, Integer> {
    Client getClientByNumber(String number);
}
