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
import com.switzerlandbank.api.entities.Costumer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.CostumerRepository;
import com.switzerlandbank.api.services.AccountService;
import com.switzerlandbank.api.services.AddressService;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;
import com.switzerlandbank.api.services.impls.CostumerServiceImpl;

import jakarta.persistence.EntityNotFoundException;

class CostumerServiceUnitTest {

	@Mock
	private CostumerRepository clientRepository;
	
	@Mock
	private AddressService addressService;
	
	@Mock
	private AccountService accountService;
	
	@InjectMocks
	private CostumerServiceImpl clientService;
	
	private Costumer costumer;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		costumer = new Costumer(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", costumer);
		costumer.setAddress(address);
	}
	
	@Test
	void testFindAll_ReturnNonEmptyList() {
		List<Costumer> expectedResult = new ArrayList<>();
		expectedResult.add(costumer);
		
		when(clientRepository.findAll()).thenReturn(expectedResult);
		
		List<Costumer> result = clientService.findAll();
		
		assertEquals(expectedResult, result);
	}
	
	@Test
	void testFindAll_ReturnEmptyList() {
		when(clientRepository.findAll()).thenReturn(Collections.emptyList());
		List<Costumer> result = clientService.findAll();
		assertTrue(result.isEmpty());
	}
	
	@Test
	void testFindById_ReturnClient() {
		when(clientRepository.findById(1L)).thenReturn(Optional.of(costumer));
		Costumer result = clientService.findById(1L);
		assertEquals(costumer, result);
	}

	@Test
	void testFindById_ThrowsResourceNotFoundException() {
		when(clientRepository.findById(1L)).thenReturn(Optional.of(costumer));
		assertThrows(ResourceNotFoundException.class, () -> clientService.findById(2L));
	}
	
	@Test
	void testInsert_Success() {
		when(clientRepository.save(costumer)).thenReturn(costumer);
		Costumer result = clientService.insert(costumer);
		assertEquals(costumer, result);
	}
	
	@Test
	void testDelete_Success() {
		when(clientRepository.existsById(1L)).thenReturn(true);
		clientService.delete(1L);
		verify(clientRepository, times(1)).deleteById(1L);
	}
	
	@Test
	void testDelete_ThrowsResourceNotFoundException() {
		when(clientRepository.existsById(1L)).thenReturn(true);
		assertThrows(ResourceNotFoundException.class, () -> clientService.delete(2L));
	}
	
	@Test
	void testUpdate_VerifySaveWithExistingClient() {
		Costumer newCostumer = new Costumer(1L, "Carlos Pereira", "11122233344", "Teresa Pereira", LocalDate.parse("1975-10-10"), Gender.OTHER, "carlospereira@gmail.com", "CarlosPereira123");
		Address address = new Address(1L, "R. Cento e Cinquenta e Dois", "196", "Laranjal", "Volta Redonda", "Rio de Janeiro", "27255020", newCostumer);
		newCostumer.setAddress(address);
		
		when(clientRepository.getReferenceById(1L)).thenReturn(costumer);

		clientService.update(newCostumer, 1L);
		
		// Verifica se o método save do repository foi chamado com o Client existente
		verify(clientRepository, times(1)).save(costumer);
	}
	
	@Test
	void testUpdate_VerifyUpdateWithAddress() {
		Costumer newCostumer = new Costumer(1L, "Carlos Pereira", "11122233344", "Teresa Pereira", LocalDate.parse("1975-10-10"), Gender.OTHER, "carlospereira@gmail.com", "CarlosPereira123");
		Address address = new Address(1L, "R. Cento e Cinquenta e Dois", "196", "Laranjal", "Volta Redonda", "Rio de Janeiro", "27255020", newCostumer);
		newCostumer.setAddress(address);
		
		when(clientRepository.getReferenceById(1L)).thenReturn(costumer);

		clientService.update(newCostumer, 1L);
		
		// Verifica se o método update do AddressService foi chamado com o Address do Client existente e o novo Address
		verify(addressService, times(1)).update(costumer.getAddress(), newCostumer.getAddress());
	}
	
	@Test
	void testUpdate_ThrowsResourceNotFoundException() {
		when(clientRepository.getReferenceById(2L)).thenThrow(EntityNotFoundException.class);
		assertThrows(ResourceNotFoundException.class, () -> clientService.update(costumer, 2L));
	}

}
