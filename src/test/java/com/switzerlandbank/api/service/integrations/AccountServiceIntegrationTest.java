package com.switzerlandbank.api.service.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Costumer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.AccountRepository;
import com.switzerlandbank.api.repositories.CostumerRepository;
import com.switzerlandbank.api.services.AccountService;
import com.switzerlandbank.api.services.CostumerService;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;

@SpringBootTest
public class AccountServiceIntegrationTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    private Costumer newCostumer;
    private Costumer costumer;
    private Account account;
    private Account newAccount;

    @BeforeEach
    void setUp() {
        Costumer costumer = new Costumer(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"),
                Gender.MALE, "joaosilva@example.com", "JoaoSilva123");

        newCostumer = new Costumer(null, "João Silva 2", "22222222222", "Maria Silva 2", LocalDate.parse("1980-07-15"),
                Gender.MALE, "joaosilva2@example.com", "JoaoSilva222");

        Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins",
                "77600000", costumer);

        Address newAddress = new Address(null, "Av. Castelo Branco 2", "14162", "Centro 2", "Paraíso do Tocantins 2",
                "Tocantins 2", "00000000", newCostumer);

        newAccount = new Account(null, null, newCostumer);

        costumer.setAddress(address);
        newCostumer.setAddress(newAddress);
        account = new Account(1L, "123456", costumer);
        costumer.setAccount(account);
    }

    @Test
    void testFindAll_ReturnNonEmptyList() {
        List<Account> expectedResult = accountRepository.findAll();

        List<Account> result = accountService.findAll();

        assertEquals(expectedResult, result);
    }

    @Test
    void testFindById_ReturnAccount() {
        Long id = 1L;

        Account result = accountService.findById(id);

        assertEquals(account, result);
    }

    @Test
    void testFindById_ThrowsResourceNotFoundException() {
        Long id = 0L;

        assertThrows(ResourceNotFoundException.class, () -> accountService.findById(id));
    }

    
    @Test
    void testInsert() {
        Account result = accountService.insert(newCostumer);

        newAccount.setId(result.getId());
        assertEquals(newAccount, result);
    }

}
