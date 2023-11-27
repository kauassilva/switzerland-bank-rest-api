package com.switzerlandbank.api.repositories.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Costumer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.AccountRepository;
import com.switzerlandbank.api.repositories.CostumerRepository;
import com.switzerlandbank.api.services.AccountService;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AccountRepositoryIntegrationTest {

    @Autowired

    private AccountRepository accountRepository;

    private Account account;

    private AccountService accountService;

    @Autowired
    private CostumerRepository costumerRepository;

    private Costumer costumer;

    @BeforeEach
    void setUp() {
        costumer = new Costumer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"),
                Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
        Address address = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins",
                "77600000", costumer);
        costumer.setAddress(address);

        account = new Account(null, null, costumer);

        
    }

    @Test
    void findById_Sucess() {
        Account savedAccount = accountRepository.save(account);

        Optional<Account> foundAccount = accountRepository.findById(savedAccount.getId());

        assertTrue(foundAccount.isPresent());
    }

   



}
