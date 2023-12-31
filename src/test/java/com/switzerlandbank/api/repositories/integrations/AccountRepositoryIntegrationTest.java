package com.switzerlandbank.api.repositories.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaSystemException;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Customer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.AccountRepository;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AccountRepositoryIntegrationTest {

    @Autowired

    private AccountRepository accountRepository;

    private Account account;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(null, "João Silva", "12345678910", "Maria Silva",
                LocalDate.parse("1980-07-15"),
                Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
        Address address = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins",
                "77600000", customer);
        customer.setAddress(address);

        account = new Account(null, null, customer);

    }

    @Test
    void findById_Sucess() {
        Account savedAccount = accountRepository.save(account);

        Optional<Account> foundAccount = accountRepository.findById(savedAccount.getId());

        assertTrue(foundAccount.isPresent());
    }

    @Test
    void testFindById_NotFound() {
        Account savedAccount = accountRepository.save(account);

        Optional<Account> foundAccount = accountRepository.findById(savedAccount.getId() + 1L);

        assertTrue(foundAccount.isEmpty());
    }

    @Test
    void testSave_Success() {
        Account savedAccount = accountRepository.save(account);

        assertEquals(account, savedAccount);
    }

    @Test
    void testSave_ThrowsConstraintViolationException() {
        Account account = new Account(null, null, null);

        assertThrows(JpaSystemException.class, () -> accountRepository.save(account));
    }

    @Test
    void testDelete_Success() {
        Account savedAccount = accountRepository.save(account);

        accountRepository.deleteById(savedAccount.getId());

        Optional<Account> foundAccount = accountRepository.findById(savedAccount.getId());

        assertTrue(foundAccount.isEmpty());
    }

}
