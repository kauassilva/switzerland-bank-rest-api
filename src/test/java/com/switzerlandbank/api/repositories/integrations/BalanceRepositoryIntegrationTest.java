package com.switzerlandbank.api.repositories.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;
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
import com.switzerlandbank.api.entities.Balance;
import com.switzerlandbank.api.entities.Customer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.BalanceRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class BalanceRepositoryIntegrationTest {
	
	@Autowired
	private BalanceRepository balanceRepository;
	
	private Balance balance;

	@BeforeEach
	void setUp() {
		Customer customer = new Customer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", customer);
		customer.setAddress(address);
		Account account = new Account(null, "123456", customer);
		customer.setAccount(account);
		balance = new Balance(null, new BigDecimal("10.00"), Instant.now(), account);
		account.setBalance(balance);
	}

	@Test
	void findById_Sucess() {
		Balance savedBalance = balanceRepository.save(balance);

		Optional<Balance> foundBalance = balanceRepository.findById(savedBalance.getId());

		assertTrue(foundBalance.isPresent());
	}
	
	@Test
	void testFindById_NotFound() {
		Balance savedBalance = balanceRepository.save(balance);

		Optional<Balance> foundBalance = balanceRepository.findById(savedBalance.getId() + 1L);

		assertTrue(foundBalance.isEmpty());
	}
	
	@Test
	void testSave_Success() {
		Balance savedBalance = balanceRepository.save(balance);

		assertEquals(balance, savedBalance);
	}
	
	@Test
	void testSave_ThrowsJpaSystemException() {
		Balance balance = new Balance(null, new BigDecimal("10.00"), Instant.now(), null);

		assertThrows(JpaSystemException.class, () -> balanceRepository.save(balance));
	}
	
	@Test
	void testDelete_Success() {
		Balance savedBalance = balanceRepository.save(balance);

		balanceRepository.deleteById(savedBalance.getId());

		Optional<Balance> foundBalance = balanceRepository.findById(savedBalance.getId());

		assertTrue(foundBalance.isEmpty());
	}

}
