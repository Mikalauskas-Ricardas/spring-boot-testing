package com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.model.Account;
import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.model.TransactionDto;
import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.service.AccountService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.util.DATA.ACCOUNT_001;
import static com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.util.DATA.ACCOUNT_002;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService accountService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void sampleTest() throws Exception{
        when(accountService.findById(1L)).thenReturn(ACCOUNT_001.orElseThrow());
        mvc.perform(MockMvcRequestBuilders.get("/api/accounts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.person").value("Ricky"));
        verify(accountService).findById(1L);
    }

    @Test
    void testTransfer() throws Exception {
        TransactionDto dto = new TransactionDto();
        dto.setOriginAccountId(1L);
        dto.setDestinationAccountId(2L);
        dto.setAmount(new BigDecimal("100"));
        dto.setBankId(1L);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("date", LocalDate.now().toString());
        expectedResponse.put("status", "OK");
        expectedResponse.put("message", "Transfer successful");
        expectedResponse.put("transaction", dto);

        mvc.perform(MockMvcRequestBuilders.post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    void accountsListTest() throws Exception{
        List<Account> accounts = Arrays.asList(ACCOUNT_001.orElseThrow(), ACCOUNT_002.orElseThrow());
        when(accountService.findAll()).thenReturn(accounts);

        mvc.perform(MockMvcRequestBuilders.get("/api/accounts/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(accounts)));
    }

    @Test
    void saveTest() throws Exception {
        Account account = new Account(null, "PEPE", new BigDecimal("1200"));
        when(accountService.save(any())).then(invocation -> {
            Account a = invocation.getArgument(0);
            a.setId(3L);
            return account;
        });

        mvc.perform(MockMvcRequestBuilders.post("/api/accounts/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(account)));
    }


}