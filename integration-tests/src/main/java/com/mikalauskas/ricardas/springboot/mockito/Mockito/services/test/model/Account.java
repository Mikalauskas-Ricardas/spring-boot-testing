package com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.model;

import com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.exception.InsufficientMoneyException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String person;
    private BigDecimal balance;

    public void credit(BigDecimal amount) {
        this.balance = this.balance.add(amount);

    }

    public void debit(BigDecimal amount) throws InsufficientMoneyException {
        BigDecimal newBalance = this.balance.subtract(amount);
        if(newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientMoneyException("Insufficient Money in the account");
        }
        this.balance = newBalance;
    }
}
