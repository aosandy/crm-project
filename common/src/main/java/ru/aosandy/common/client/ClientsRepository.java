package ru.aosandy.common.client;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientsRepository extends JpaRepository<Client, Integer> {

    @Cacheable("clients")
    Optional<Client> findByNumber(String number);

    @Override
    @Cacheable("clients")
    List<Client> findAll();

    @CachePut("clients")
    @Query(value = "SELECT * FROM clients", nativeQuery = true)
    List<Client> updateCache();
}
