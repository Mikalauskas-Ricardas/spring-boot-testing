package com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.repository;

import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT * FROM accounts a WHERE a.person = ?1", nativeQuery = true)
    Optional<Account> findByPerson(String person);
}
