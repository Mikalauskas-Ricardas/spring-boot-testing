package com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test;

import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.model.Account;
import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class JPAIntegrationTest {
    @Autowired
    AccountRepository accountRepository;

    @Test
    void findById() {
        Optional<Account> account = accountRepository.findById(1L);
        assertEquals("Ricky", account.orElseThrow().getPerson());
    }

    @Test
    void findByPersona() {
        Optional<Account> account = accountRepository.findByPerson("Ricky");
        assertEquals("Ricky", account.orElseThrow().getPerson());
    }

    @Test
    void findByPersonaThrowException() {
        Optional<Account> account = accountRepository.findByPerson("Manolo");
        assertThrows(NoSuchElementException.class, account::orElseThrow);
        assertFalse(account.isPresent());
    }

    @Test
    void findAll() {
        List<Account> accounts = accountRepository.findAll();
        assertFalse(accounts.isEmpty());
        assertEquals(2, accounts.size());
    }

    @Test
    void save() {
        // given
        Account newAccount = new Account(null,"Pepe", new BigDecimal(1400));
        accountRepository.save(newAccount);

        // when
        Account account = accountRepository.findByPerson("Pepe").orElseThrow();

        // then
        assertEquals("Pepe", account.getPerson());
        assertEquals("1400", account.getBalance().toPlainString());
    }

    @Test
    void update() {
        // given
        Account newAccount = new Account(null,"Pepe", new BigDecimal(1400));
        accountRepository.save(newAccount);

        // when
        Account account = accountRepository.findByPerson("Pepe").orElseThrow();
        account.setBalance(new BigDecimal("1600"));
        accountRepository.save(account);

        // then
        assertEquals("Pepe", account.getPerson());
        assertEquals("1600", account.getBalance().toPlainString());
    }

    @Test
    void delete() {
        Account account = accountRepository.findById(1L).orElseThrow();
        accountRepository.delete(account);

        assertThrows(NoSuchElementException.class, () -> {
            accountRepository.findById(1L).orElseThrow();
        });
    }
}
