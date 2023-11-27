package com.switzerlandbank.api.service.units;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Customer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.AccountRepository;
import com.switzerlandbank.api.repositories.CustomerRepository;
import com.switzerlandbank.api.services.BalanceService;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;
import com.switzerlandbank.api.services.impls.AccountServiceImpl;

public class AccountServiceUnitTest {

	@Mock
	private BalanceService balanceService;

	@Mock
	private CustomerRepository costumerRepository;

	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	@Autowired
	private AccountServiceImpl accountService;

	private Account account;

	private Customer customer;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		customer = new Customer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"),
				Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins",
				"77600000", customer);
		customer.setAddress(address);
		account = new Account(null, "123456", customer);
		customer.setAccount(account);
	}

	@Test
	void testFindById_ReturnAccount() {
		when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
		Account result = accountService.findById(1L);
		assertEquals(account, result);
	}

	@Test
	void testFindById_ThrowsResourceNotFoundException() {
		when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
		assertThrows(ResourceNotFoundException.class, () -> accountService.findById(2L));
	}

	@Test
	void testFindAll_ReturnNonEmptyList() {
		List<Account> expectedResult = new ArrayList<>();
		expectedResult.add(account);

		when(accountRepository.findAll()).thenReturn(expectedResult);

		List<Account> result = accountService.findAll();

		assertEquals(expectedResult, result);
	}

	@Test
	void testFindAll_ReturnEmptyList() {
		when(accountRepository.findAll()).thenReturn(Collections.emptyList());
		List<Account> result = accountService.findAll();
		assertTrue(result.isEmpty());
	}

	@Test
	void testInsert() {
		when(accountRepository.save(account)).thenReturn(account);
		Account result = accountService.insert(customer);
		assertEquals(account, result);
	}

  
    @Test
	void generateAccountNumber(){
		
		assertNotNull(accountService.generateAccountNumber());
	}


}
