package com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.service;

import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.exception.AccountNotExists;
import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.exception.BankNotExists;
import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.model.Account;
import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.model.Bank;
import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.repository.AccountRepository;
import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public int reviewTotalTx(Long bankId) {
        Bank bank = bankRepository.findById(bankId).orElseThrow();
        return bank.getTotalTx();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal reviewBalance(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        return account.getBalance();
    }

    @Override
    @SneakyThrows
    @Transactional
    public void transfer(Long originAccountNumber, Long destinationAccountNumber, BigDecimal amount,
                         Long bankId) {
        Bank bank = bankRepository.findById(bankId).orElseThrow();
        bank.setTotalTx(bank.getTotalTx() + 1);
        bankRepository.save(bank);

        Account originAccount = accountRepository.findById(originAccountNumber).orElseThrow();
        originAccount.debit(amount);
        accountRepository.save(originAccount);

        Account destinationAccount = accountRepository.findById(destinationAccountNumber).orElseThrow();
        destinationAccount.credit(amount);
        accountRepository.save(destinationAccount);
    }
}
