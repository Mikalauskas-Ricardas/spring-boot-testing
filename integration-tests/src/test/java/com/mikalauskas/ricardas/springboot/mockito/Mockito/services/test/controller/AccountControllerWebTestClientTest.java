package com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.model.Account;
import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.model.TransactionDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerWebTestClientTest {
    private static final String ROOT_URL = "/api/accounts";

    @Autowired
    private WebTestClient webTestClient;

    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void transferTest() throws Exception{
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setOriginAccountId(1L);
        transactionDto.setDestinationAccountId(2L);
        transactionDto.setBankId(1L);
        transactionDto.setAmount(new BigDecimal("500"));

        webTestClient.post().uri(ROOT_URL + "/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transactionDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    JsonNode json = null;
                    try {
                        json = objectMapper.readTree(response.getResponseBody());
                        assertEquals("Transfer successful", json.path("message").asText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    @Order(2)
    void detailsTest() {
        webTestClient.get().uri(ROOT_URL + "/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Account.class)
                .consumeWith(resp -> {
                    Account account = resp.getResponseBody();
                    assertEquals("Ricky", account.getPerson());
                });
    }

    @Test
    @Order(3)
    void listTest() {
        webTestClient.get().uri(ROOT_URL + "/")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Account.class)
                .consumeWith(resp -> {
                    List<Account> accounts = resp.getResponseBody();
                    assertEquals("Ricky", accounts.get(0).getPerson());
                }).hasSize(2);
    }

    @Test
    @Order(4)
    void saveTest() throws Exception {
        Account account = new Account(null, "Pepe", new BigDecimal("500"));
        webTestClient.post().uri(ROOT_URL + "/save")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(account)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Account.class)
                .consumeWith(resp -> {
                    Account createdAccount = resp.getResponseBody();
                    assertEquals(3, createdAccount.getId());
                    assertEquals(account.getPerson(), createdAccount.getPerson());
                    assertEquals(account.getBalance(), createdAccount.getBalance());
                });
    }

    @Test
    @Order(5)
    void deleteTest() {
        webTestClient.delete().uri(ROOT_URL + "/delete/3")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();
    }
}