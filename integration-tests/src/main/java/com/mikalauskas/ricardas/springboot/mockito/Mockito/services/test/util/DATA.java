package com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.util;

import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.model.Account;
import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.model.Bank;

import java.math.BigDecimal;
import java.util.Optional;

public class DATA {
    public static Optional<Account> ACCOUNT_001 = Optional.of(new Account(1L, "Ricky", new BigDecimal("1000000")));
    public static Optional<Account> ACCOUNT_002 = Optional.of(new Account(2L, "Joan", new BigDecimal("500000")));
    public static Optional<Bank> BANK = Optional.of(new Bank(1L, "BBVA", 0));
}
