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
import com.switzerlandbank.api.entities.Costumer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.CostumerRepository;
import com.switzerlandbank.api.services.CostumerService;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;

@SpringBootTest
class CostumerServiceIntegrationTest {
	
	@Autowired
	private CostumerService costumerService;
	
	@Autowired
	private CostumerRepository costumerRepository;
	
	private Costumer costumer;
	private Costumer newCostumer;

	@BeforeEach
	void setUp() {
		costumer = new Costumer(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		newCostumer = new Costumer(null, "João Silva 2", "22222222222", "Maria Silva 2", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva2@example.com", "JoaoSilva222");
		
		Address address1 = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", costumer);
		Address newAddress = new Address(null, "Av. Castelo Branco 2", "14162", "Centro 2", "Paraíso do Tocantins 2", "Tocantins 2", "00000000", newCostumer);

		costumer.setAddress(address1);
		newCostumer.setAddress(newAddress);
	}
	
	@Test
	void testFindAll_ReturnNonEmptyList() {
		List<Costumer> expectedResult = costumerRepository.findAll();
		
		List<Costumer> result = costumerService.findAll();
		
		assertEquals(expectedResult, result);
	}
	
	@Test
	void testFindById_ReturnClient() {
		Long id = 1L;
		
		Costumer result = costumerService.findById(id);
		
		assertEquals(costumer, result);
	}
	
	@Test
	void testFindById_ThrowsResourceNotFoundException() {
		Long id = 0L;
		
		assertThrows(ResourceNotFoundException.class, () -> costumerService.findById(id));
	}

	@Test
	void testInsert_Success() {		
		Costumer result = costumerService.insert(newCostumer);

		assertEquals(newCostumer, result);
	}
	
	@Test
	void testDelete_Success() {
		Costumer savedCostumer = costumerService.insert(newCostumer);
		
		costumerService.delete(savedCostumer.getId());
		
		assertThrows(ResourceNotFoundException.class, () -> costumerService.findById(savedCostumer.getId()));
	}
	
	@Test
	void testDelete_ThrowsResourceNotFoundException() {
		Long id = 0L;
		
		assertThrows(ResourceNotFoundException.class, () -> costumerService.delete(id));
	}
	
	@Test
	void testUpdate_Success() {
		Costumer savedCostumer = costumerService.insert(newCostumer);
		
		Costumer newDataCostumer = new Costumer(savedCostumer.getId(), "João Silva 2", "22222222222", "Maria Silva 2", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva2@example.com", "JoaoSilva222");
		Address newDataAddress = new Address(savedCostumer.getId(), "Av. Castelo Branco 2", "14162", "Centro 2", "Paraíso do Tocantins 2", "Tocantins 2", "00000000", newCostumer);
		newDataCostumer.setAddress(newDataAddress);
		
		Costumer updatedCostumer = costumerService.update(newDataCostumer, savedCostumer.getId());

		Costumer foundCostumer = costumerService.findById(savedCostumer.getId());
		
		assertEquals(foundCostumer, updatedCostumer);
	}
	
	@Test
	void testUpdate_ThrowsResourceNotFoundException() {
		Costumer savedCostumer = costumerService.insert(newCostumer);
		
		Costumer newDataCostumer = new Costumer(savedCostumer.getId(), "João Silva 2", "22222222222", "Maria Silva 2", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva2@example.com", "JoaoSilva222");
		Address newDataAddress = new Address(savedCostumer.getId(), "Av. Castelo Branco 2", "14162", "Centro 2", "Paraíso do Tocantins 2", "Tocantins 2", "00000000", newCostumer);
		newDataCostumer.setAddress(newDataAddress);

		assertThrows(ResourceNotFoundException.class, () -> costumerService.update(newDataCostumer, savedCostumer.getId() + 1));
	}

}
