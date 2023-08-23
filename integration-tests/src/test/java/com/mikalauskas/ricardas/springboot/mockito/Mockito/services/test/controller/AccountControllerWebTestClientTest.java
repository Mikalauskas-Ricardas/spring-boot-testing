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

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerWebTestClientTest {
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

        webTestClient.post().uri("/api/accounts/transfer")
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
        webTestClient.get().uri("/api/accounts/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Account.class)
                .consumeWith(resp -> {
                    Account account = resp.getResponseBody();
                    assertEquals("Ricky", account.getPerson());
                });
    }
}