package com.switzerlandbank.api.service.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Customer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.services.AddressService;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;

@SpringBootTest
class AddressServiceIntegrationTest {
	
	@Autowired
	private AddressService addressService;

	private Address address;


	@BeforeEach
	void setUp() throws Exception {
		Customer customer = new Customer(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", customer);
		customer.setAddress(address);
	}

	@Test
    void testFindById_ReturnCorrectContent() {
        Long id = 1L;

        Address result = addressService.findById(id);

        assertEquals(address, result);
    }

	@Test
    void testFindById_ThrowsResourceNotFoundException() {
        Long id = 0L;

        assertThrows(ResourceNotFoundException.class, () -> addressService.findById(id));
    }

	@Test
	void testUpdate_Success() {
	
		Customer customer = new Customer(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address newDataAddress = new Address(1L, "Av. Castelo Branco 2", "14162", "Centro 2", "Paraíso do Tocantins 2", "Tocantins 2", "00000000", customer);
		
		
		Address updatedAddress = addressService.update(address,newDataAddress);
		
		assertEquals(newDataAddress, updatedAddress);
	}


}
