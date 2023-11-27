package com.switzerlandbank.api.service.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.orm.jpa.JpaSystemException;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Balance;
import com.switzerlandbank.api.entities.Customer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.BalanceRepository;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;
import com.switzerlandbank.api.services.impls.BalanceServiceImpl;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

public class BalanceServiceIntegrationTest {

    @Autowired
    private BalanceServiceImpl balanceService;


    @Autowired
    private BalanceRepository balanceRepository;

    private Balance balanceEmpty;
    private Account accountEmpty;
    private Account account;
    private Balance balance;
    @BeforeEach
    void setUp(){
        Customer customerEmpty = new Customer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
        Address addressEmpty = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", customerEmpty);
        customerEmpty.setAddress(addressEmpty);
        
        Customer customer1 = new Customer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
        Address address1 = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", customer1);
        customer1.setAddress(address1);

        accountEmpty = new Account(null, "123456", customerEmpty);
        account = new Account(null, "123456", customer1);
		customer1.setAccount(accountEmpty);
         balanceEmpty = new Balance(null, new BigDecimal("10.00"), Instant.now(), accountEmpty);
         balance = new Balance(1L, new BigDecimal("10.00"), Instant.now(), account);


    }

    @Test
    void testFindAllBalance(){
        List<Balance> balanceList = balanceRepository.findAll();
        List<Balance> result = balanceService.findAll();     
        assertEquals(balanceList, result);
    }
  
    @Test
    void testFindById(){
        Balance expectedBalanceId = balance;
        Balance result = balanceService.findById(1L);
        assertEquals(expectedBalanceId, result); 

    }

    @Test
    void testFindByIdButIsEmpty(){
        assertThrows(ResourceNotFoundException.class,() -> balanceService.findById(5L));
    }

    @Test
    void testInsert(){
        
        Balance result = balanceService.insert(accountEmpty);
        balanceEmpty.setId(result.getId()); 
        assertEquals(balanceEmpty, result);
    }

    @Test
    void TestInsertButIsNull(){
        assertThrows(JpaSystemException.class, () -> balanceService.insert(null));
    }


    
}
