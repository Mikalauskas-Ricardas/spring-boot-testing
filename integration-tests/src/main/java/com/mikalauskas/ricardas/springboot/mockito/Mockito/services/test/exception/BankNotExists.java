package com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.exception;

public class BankNotExists extends Throwable {
    public BankNotExists(String message) {
        super(message);
    }
}
