package com.switzerlandbank.api.service.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Customer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.CustomerRepository;
import com.switzerlandbank.api.services.CustomerService;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;

@SpringBootTest
class CustomerServiceIntegrationTest {
	
	@Autowired
	private CustomerService costumerService;
	
	@Autowired
	private CustomerRepository costumerRepository;
	
	private Customer customer;
	private Customer newCustomer;

	@BeforeEach
	void setUp() {
		customer = new Customer(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		newCustomer = new Customer(null, "João Silva 2", "22222222222", "Maria Silva 2", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva2@example.com", "JoaoSilva222");
		
		Address address1 = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", customer);
		Address newAddress = new Address(null, "Av. Castelo Branco 2", "14162", "Centro 2", "Paraíso do Tocantins 2", "Tocantins 2", "00000000", newCustomer);

		customer.setAddress(address1);
		newCustomer.setAddress(newAddress);
	}
	
	@Test
	void testFindAll_ReturnNonEmptyList() {
		List<Customer> expectedResult = costumerRepository.findAll();
		
		List<Customer> result = costumerService.findAll();
		
		assertEquals(expectedResult, result);
	}
	
	@Test
	void testFindById_ReturnClient() {
		Long id = 1L;
		
		Customer result = costumerService.findById(id);
		
		assertEquals(customer, result);
	}
	
	@Test
	void testFindById_ThrowsResourceNotFoundException() {
		Long id = 0L;
		
		assertThrows(ResourceNotFoundException.class, () -> costumerService.findById(id));
	}

	@Test
	void testInsert_Success() {		
		Customer result = costumerService.insert(newCustomer);

		assertEquals(newCustomer, result);
	}
	
	@Test
	void testDelete_Success() {
		Customer savedCustomer = costumerService.insert(newCustomer);
		
		costumerService.delete(savedCustomer.getId());
		
		assertThrows(ResourceNotFoundException.class, () -> costumerService.findById(savedCustomer.getId()));
	}
	
	@Test
	void testDelete_ThrowsResourceNotFoundException() {
		Long id = 0L;
		
		assertThrows(ResourceNotFoundException.class, () -> costumerService.delete(id));
	}
	
	@Test
	void testUpdate_Success() {
		Customer savedCustomer = costumerService.insert(newCustomer);
		
		Customer newDataCustomer = new Customer(savedCustomer.getId(), "João Silva 2", "22222222222", "Maria Silva 2", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva2@example.com", "JoaoSilva222");
		Address newDataAddress = new Address(savedCustomer.getId(), "Av. Castelo Branco 2", "14162", "Centro 2", "Paraíso do Tocantins 2", "Tocantins 2", "00000000", newCustomer);
		newDataCustomer.setAddress(newDataAddress);
		
		Customer updatedCustomer = costumerService.update(newDataCustomer, savedCustomer.getId());

		Customer foundCustomer = costumerService.findById(savedCustomer.getId());
		
		assertEquals(foundCustomer, updatedCustomer);
	}
	
	@Test
	void testUpdate_ThrowsResourceNotFoundException() {
		Customer savedCustomer = costumerService.insert(newCustomer);
		
		Customer newDataCustomer = new Customer(savedCustomer.getId(), "João Silva 2", "22222222222", "Maria Silva 2", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva2@example.com", "JoaoSilva222");
		Address newDataAddress = new Address(savedCustomer.getId(), "Av. Castelo Branco 2", "14162", "Centro 2", "Paraíso do Tocantins 2", "Tocantins 2", "00000000", newCustomer);
		newDataCustomer.setAddress(newDataAddress);

		assertThrows(ResourceNotFoundException.class, () -> costumerService.update(newDataCustomer, savedCustomer.getId() + 1));
	}

}
