package com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.repository;

import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BankRepository extends JpaRepository<Bank, Long> {
}
