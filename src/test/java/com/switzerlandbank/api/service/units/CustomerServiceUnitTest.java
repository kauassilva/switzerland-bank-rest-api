package com.switzerlandbank.api.service.units;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Customer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.CustomerRepository;
import com.switzerlandbank.api.services.AccountService;
import com.switzerlandbank.api.services.AddressService;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;
import com.switzerlandbank.api.services.impls.CustomerServiceImpl;

import jakarta.persistence.EntityNotFoundException;

class CustomerServiceUnitTest {

	@Mock
	private CustomerRepository customerRepository;
	
	@Mock
	private AddressService addressService;
	
	@Mock
	private AccountService accountService;
	
	@InjectMocks
	private CustomerServiceImpl customerService;
	
	private Customer customer;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		customer = new Customer(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", customer);
		customer.setAddress(address);
	}
	
	@Test
	void testFindAll_ReturnNonEmptyList() {
		List<Customer> expectedResult = new ArrayList<>();
		expectedResult.add(customer);
		
		when(customerRepository.findAll()).thenReturn(expectedResult);
		
		List<Customer> result = customerService.findAll();
		
		assertEquals(expectedResult, result);
	}
	
	@Test
	void testFindAll_ReturnEmptyList() {
		when(customerRepository.findAll()).thenReturn(Collections.emptyList());
		List<Customer> result = customerService.findAll();
		assertTrue(result.isEmpty());
	}
	
	@Test
	void testFindById_ReturnClient() {
		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
		Customer result = customerService.findById(1L);
		assertEquals(customer, result);
	}

	@Test
	void testFindById_ThrowsResourceNotFoundException() {
		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
		assertThrows(ResourceNotFoundException.class, () -> customerService.findById(2L));
	}
	
	@Test
	void testInsert_Success() {
		when(customerRepository.save(customer)).thenReturn(customer);
		Customer result = customerService.insert(customer);
		assertEquals(customer, result);
	}
	
	@Test
	void testDelete_Success() {
		when(customerRepository.existsById(1L)).thenReturn(true);
		customerService.delete(1L);
		verify(customerRepository, times(1)).deleteById(1L);
	}
	
	@Test
	void testDelete_ThrowsResourceNotFoundException() {
		when(customerRepository.existsById(1L)).thenReturn(true);
		assertThrows(ResourceNotFoundException.class, () -> customerService.delete(2L));
	}
	
	@Test
	void testUpdate_VerifySaveWithExistingClient() {
		Customer newCustomer = new Customer(1L, "Carlos Pereira", "11122233344", "Teresa Pereira", LocalDate.parse("1975-10-10"), Gender.OTHER, "carlospereira@gmail.com", "CarlosPereira123");
		Address address = new Address(1L, "R. Cento e Cinquenta e Dois", "196", "Laranjal", "Volta Redonda", "Rio de Janeiro", "27255020", newCustomer);
		newCustomer.setAddress(address);
		
		when(customerRepository.getReferenceById(1L)).thenReturn(customer);

		customerService.update(newCustomer, 1L);
		
		// Verifica se o método save do repository foi chamado com o Client existente
		verify(customerRepository, times(1)).save(customer);
	}
	
	@Test
	void testUpdate_VerifyUpdateWithAddress() {
		Customer newCustomer = new Customer(1L, "Carlos Pereira", "11122233344", "Teresa Pereira", LocalDate.parse("1975-10-10"), Gender.OTHER, "carlospereira@gmail.com", "CarlosPereira123");
		Address address = new Address(1L, "R. Cento e Cinquenta e Dois", "196", "Laranjal", "Volta Redonda", "Rio de Janeiro", "27255020", newCustomer);
		newCustomer.setAddress(address);
		
		when(customerRepository.getReferenceById(1L)).thenReturn(customer);

		customerService.update(newCustomer, 1L);
		
		// Verifica se o método update do AddressService foi chamado com o Address do Client existente e o novo Address
		verify(addressService, times(1)).update(customer.getAddress(), newCustomer.getAddress());
	}
	
	@Test
	void testUpdate_ThrowsResourceNotFoundException() {
		when(customerRepository.getReferenceById(2L)).thenThrow(EntityNotFoundException.class);
		assertThrows(ResourceNotFoundException.class, () -> customerService.update(customer, 2L));
	}

}
