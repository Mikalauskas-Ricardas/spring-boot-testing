package com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test;

import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.model.Account;
import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.model.Bank;
import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.repository.AccountRepository;
import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.repository.BankRepository;
import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.service.AccountService;
import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.service.AccountServiceImpl;
import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.util.DATA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MockitoServicesTestApplicationTests {
	@Mock
	AccountRepository accountRepository;

	@Mock
	BankRepository bankRepository;

	@InjectMocks
	AccountServiceImpl accountService;

	@Test
	void contextLoads() {
		when(accountRepository.findById(1L)).thenReturn(DATA.ACCOUNT_001);
		when(accountRepository.findById(2L)).thenReturn(DATA.ACCOUNT_002);
		when(bankRepository.findById(1L)).thenReturn(DATA.BANK);

		BigDecimal originBalance = accountService.reviewBalance(1L);
		BigDecimal destinationBalance = accountService.reviewBalance(2L);

		assertEquals("1000000", originBalance.toPlainString());
		assertEquals("500000", destinationBalance.toPlainString());

		accountService.transfer(1L, 2L, new BigDecimal("100000"), 1L);

		originBalance = accountService.reviewBalance(1L);
		destinationBalance = accountService.reviewBalance(2L);

		assertEquals("900000", originBalance.toPlainString());
		assertEquals("600000", destinationBalance.toPlainString());

		int totalTx = accountService.reviewTotalTx(1L);
		assertEquals(1, totalTx);

		verify(accountRepository, times(3)).findById(1L);
		verify(accountRepository, times(3)).findById(2L);
		verify(accountRepository, atLeast(2)).save(any(Account.class));

		verify(bankRepository, atLeast(2)).findById(1L);
		verify(bankRepository, atLeast(1)).save(any(Bank.class));

	}



}
