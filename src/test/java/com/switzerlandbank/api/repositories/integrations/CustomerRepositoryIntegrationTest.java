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

import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Customer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.CustomerRepository;

import jakarta.validation.ConstraintViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CustomerRepositoryIntegrationTest {

	@Autowired
	private CustomerRepository customerRepository;

	private Customer customer;

	@BeforeEach
	void setUp() {
		customer = new Customer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", customer);
		customer.setAddress(address);
	}

	@Test
	void findById_Sucess() {
		Customer savedCustomer = customerRepository.save(customer);

		Optional<Customer> foundCustomer = customerRepository.findById(savedCustomer.getId());

		assertTrue(foundCustomer.isPresent());
	}

	@Test
	void testFindById_NotFound() {
		Customer savedCustomer = customerRepository.save(customer);

		Optional<Customer> foundCustomer = customerRepository.findById(savedCustomer.getId() + 1L);

		assertTrue(foundCustomer.isEmpty());
	}

	@Test
	void testSave_Success() {
		Customer savedCustomer = customerRepository.save(customer);

		assertEquals(customer, savedCustomer);
	}

	@Test
	void testSave_ThrowsConstraintViolationException() {
		Customer customer = new Customer(null, null, "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"),
				Gender.MALE, "joaosilva@example.com", "JoaoSilva123");

		assertThrows(ConstraintViolationException.class, () -> customerRepository.save(customer));
	}

	@Test
	void testDelete_Success() {
		Customer savedCustomer = customerRepository.save(customer);

		customerRepository.deleteById(savedCustomer.getId());

		Optional<Customer> foundCustomer = customerRepository.findById(savedCustomer.getId());

		assertTrue(foundCustomer.isEmpty());
	}

}
