package com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.service;

import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.exception.InsufficientMoneyException;
import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    List<Account> findAll();
    Account findById(Long id);
    Account save(Account account);
    int reviewTotalTx(Long bankId);
    BigDecimal reviewBalance(Long accountId);
    void transfer(Long originAccountNumber, Long destinationAccountNumber, BigDecimal amount, Long bankId);
}
