package com.switzerlandbank.api.resources.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Customer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.AccountRepository;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AccountResourceIntegrationTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private AccountRepository accountRepository;

	private Account account;
	
	private Customer customer;
	
	@BeforeEach
	void setUp() {
		customer = new Customer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", customer);
		customer.setAddress(address);
		account = new Account(null, "123456", customer);
		customer.setAccount(account);
	}
	
	@Test
	void testFindAll_ReturnsStatusOk() {		
		ResponseEntity<List<Account>> response = testRestTemplate
				.exchange("/api/accounts", HttpMethod.GET, null, new ParameterizedTypeReference<List<Account>>() {});
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void testFindAll_ReturnsCorrectBody() {
		List<Account> expectedResponse = accountRepository.findAll();
		
		ResponseEntity<List<Account>> response = testRestTemplate
				.exchange("/api/accounts", HttpMethod.GET, null, new ParameterizedTypeReference<List<Account>>() {});
		
		assertEquals(expectedResponse, response.getBody());
	}
	
	@Test
	void testFindByID_ReturnsStatusOk() {
		Long id = 1L;
		ResponseEntity<Account> response = testRestTemplate
				.exchange("/api/accounts/" + id, HttpMethod.GET, null, Account.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void testFindById_ReturnsStatusNotFound() {
		Long id = 0L;
		ResponseEntity<Account> response = testRestTemplate
				.exchange("/api/accounts/" + id, HttpMethod.GET, null, Account.class);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	void testFindById_ReturnsCorrectContent() {
		Optional<Account> expectedAccount = accountRepository.findById(1L);
		
		ResponseEntity<Account> response = testRestTemplate
				.exchange("/api/accounts/" + expectedAccount.get().getId(), HttpMethod.GET, null, Account.class);
		
		assertEquals(expectedAccount.get(), response.getBody());
	}

}
