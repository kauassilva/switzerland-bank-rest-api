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
import com.switzerlandbank.api.repositories.AddressRepository;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;
import com.switzerlandbank.api.services.impls.AddressServiceImpl;

public class AddressServiceUnitTest {
    
    @Mock
    private AddressRepository addressRepository;

	@InjectMocks
	private AddressServiceImpl addressService;

	private Address address;

    @BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		Costumer costumer = new Costumer(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", costumer);
		costumer.setAddress(address);
	}

    @Test
	void testFindAll_ReturnNonEmptyList() {
		List<Address> expectedResult = new ArrayList<>();
		expectedResult.add(address);
		
		when(addressRepository.findAll()).thenReturn(expectedResult);
		
        List<Address> result = addressService.findAll();
		
		assertEquals(expectedResult, result);
	}

	@Test
	void testFindAll_ReturnEmptyList() {
		when(addressRepository.findAll()).thenReturn(Collections.emptyList());
		List<Address> result = addressService.findAll();
		assertTrue(result.isEmpty());
	}

	@Test
	void testFindById_ReturnAddress() {
		when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
		Address result = addressService.findById(1L);
		assertEquals(address, result);
	}

	@Test
	void testFindById_ThrowsResourceNotFoundException() {
		when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
		assertThrows(ResourceNotFoundException.class, () -> addressService.findById(2L));
	}
	
	@Test
	void testUpdate_VerifySaveWithExistingClient() {
		Costumer costumer = new Costumer(1L, "Carlos Pereira", "11122233344", "Teresa Pereira", LocalDate.parse("1975-10-10"), Gender.OTHER, "carlospereira@gmail.com", "CarlosPereira123");
		Address newAddress = new Address(1L, "R. Cento e Cinquenta e Dois", "196", "Laranjal", "Volta Redonda", "Rio de Janeiro", "27255020", costumer);
		costumer.setAddress(newAddress);
		
		when(addressRepository.getReferenceById(1L)).thenReturn(address);

		addressService.update(address, newAddress);
		
		// Verifica se o método save do repository foi chamado com o Client existente
		verify(addressRepository, times(1)).save(address);
	}

}
