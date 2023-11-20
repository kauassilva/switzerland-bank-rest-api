package com.switzerlandbank.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hibernate.id.IdentifierGenerationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Balance;
import com.switzerlandbank.api.entities.Costumer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.AccountRepository;
import com.switzerlandbank.api.repositories.BalanceRepository;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;
import com.switzerlandbank.api.services.impls.BalanceServiceImpl;

class BalanceServiceTest {

	@Mock
    private BalanceRepository repository;

    @Mock
	private AccountRepository accountRepository;


    @InjectMocks
    @Autowired
    private BalanceServiceImpl service;

    private Balance balance;
    private Account account;

    @BeforeEach
    void setUp() {
        Costumer client1 = new Costumer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
        Address address1 = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client1);
        client1.setAddress(address1);
        account = new Account(1L, "123456", client1);
		client1.setAccount(account);
        balance = new Balance(1L, new BigDecimal(3), Instant.now(), null);
        MockitoAnnotations.openMocks(this);

       
    }

    @Test
    void testFindAllBalance() {
        List<Balance> balanceList = new ArrayList<>();
        balanceList.add(balance);

        when(repository.findAll()).thenReturn(balanceList);
        List<Balance> result = service.findAll();
        assertEquals(balanceList, result);
    }

    @Test
    void testFindAllBalanceButBalanceIsEmpty(){
        when(repository.findAll()).thenReturn(Collections.emptyList());
        List<Balance> result = service.findAll();
        assertTrue(result.isEmpty());
        

    }

    @Test
    void testFindById(){
        when(repository.findById(1L)).thenReturn(Optional.of(balance));
        Balance result = service.findById(1L);
        assertEquals(balance, result);

    }

    @Test
    void testFindByIdButTheresNoId(){
        when(repository.findById(1L)).thenReturn(Optional.of(balance));
        assertThrows(ResourceNotFoundException.class, () -> service.findById(6L));
    }

    @Test
    void insertBalance(){
        when(accountRepository.getReferenceById(1L)).thenReturn(account);
        when(service.insert(account)).thenReturn(balance);
        Balance result = service.insert(account);
        assertEquals(balance, result);
    }


    @Test
    void insertBalanceButIsNull(){
        Balance balance = new Balance(null, null, Instant.now(), null );
        when(repository.save(balance)).thenThrow(IdentifierGenerationException.class);
        assertThrows(IdentifierGenerationException.class, () -> service.insert(account));
    }
}
