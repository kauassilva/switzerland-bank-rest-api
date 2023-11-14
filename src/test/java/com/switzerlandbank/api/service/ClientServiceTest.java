package com.switzerlandbank.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Client;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.ClientRepository;
import com.switzerlandbank.api.services.AddressService;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;
import com.switzerlandbank.api.services.impls.ClientServiceImpl;

import jakarta.persistence.EntityNotFoundException;

class ClientServiceTest {

	@Mock
	private ClientRepository clientRepository;
	
	@Mock
	private AddressService addressService;
	
	@InjectMocks
	private ClientServiceImpl clientService;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testFindAll_ReturnNonEmptyList() {
		Client client1 = new Client(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Client client2 = new Client(2L, "Carlos Pereira", "11122233344", "Teresa Pereira", LocalDate.parse("1975-10-10"), Gender.OTHER, "carlospereira@gmail.com", "CarlosPereira123");
		Client client3 = new Client(3L, "Ana Santos", "98765432100", "Beatriz Santos", LocalDate.parse("1990-02-20"), Gender.FEMALE, "anasantos@example.com", "AnaSantos123");

		Address address1 = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client1);
		Address address2 = new Address(2L, "R. Cento e Cinquenta e Dois", "196", "Laranjal", "Volta Redonda", "Rio de Janeiro", "27255020", client2);
		Address address3 = new Address(3L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client3);
		
		client1.setAddress(address1);
		client2.setAddress(address2);
		client3.setAddress(address3);

		List<Client> expectedResult = new ArrayList<>();
		expectedResult.addAll(Arrays.asList(client1, client2, client3));
		
		when(clientRepository.findAll()).thenReturn(expectedResult);
		
		List<Client> result = clientService.findAll();
		
		assertEquals(expectedResult, result);
	}
	
	@Test
	void testFindAll_ReturnEmptyList() {
		when(clientRepository.findAll()).thenReturn(Collections.emptyList());
		List<Client> result = clientService.findAll();
		assertTrue(result.isEmpty());
	}
	
	@Test
	void testFindById_ReturnClient() {
		Client expectedClient = new Client(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", expectedClient);
		expectedClient.setAddress(address);
		
		when(clientRepository.findById(1L)).thenReturn(Optional.of(expectedClient));
		Client result = clientService.findById(1L);
		assertEquals(expectedClient, result);
	}

	@Test
	void testFindById_ThrowsResourceNotFoundException() {
		Client client = new Client(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client);
		client.setAddress(address);
		
		when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
		
		assertThrows(ResourceNotFoundException.class, () -> clientService.findById(2L));
	}
	
	@Test
	void testInsert_Success() {
		Client expectedClient = new Client(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", expectedClient);
		expectedClient.setAddress(address);
		
		when(clientRepository.save(expectedClient)).thenReturn(expectedClient);
		
		Client result = clientService.insert(expectedClient);
		
		assertEquals(expectedClient, result);
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
		Client existingClient = new Client(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address1 = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", existingClient);
		existingClient.setAddress(address1);
		
		Client newClient = new Client(1L, "Carlos Pereira", "11122233344", "Teresa Pereira", LocalDate.parse("1975-10-10"), Gender.OTHER, "carlospereira@gmail.com", "CarlosPereira123");
		Address address2 = new Address(1L, "R. Cento e Cinquenta e Dois", "196", "Laranjal", "Volta Redonda", "Rio de Janeiro", "27255020", newClient);
		newClient.setAddress(address2);
		
		when(clientRepository.getReferenceById(1L)).thenReturn(existingClient);

		clientService.update(newClient, 1L);
		
		// Verifica se o método save do repository foi chamado com o Client existente
		verify(clientRepository, times(1)).save(existingClient);
	}
	
	@Test
	void testUpdate_VerifyUpdateWithAddress() {
		Client existingClient = new Client(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address1 = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", existingClient);
		existingClient.setAddress(address1);
		
		Client newClient = new Client(1L, "Carlos Pereira", "11122233344", "Teresa Pereira", LocalDate.parse("1975-10-10"), Gender.OTHER, "carlospereira@gmail.com", "CarlosPereira123");
		Address address2 = new Address(1L, "R. Cento e Cinquenta e Dois", "196", "Laranjal", "Volta Redonda", "Rio de Janeiro", "27255020", newClient);
		newClient.setAddress(address2);
		
		when(clientRepository.getReferenceById(1L)).thenReturn(existingClient);

		clientService.update(newClient, 1L);
		
		// Verifica se o método update do AddressService foi chamado com o Address do Client existente e o novo Address
		verify(addressService, times(1)).update(existingClient.getAddress(), newClient.getAddress());
	}
	
	@Test
	void testUpdate_ThrowsResourceNotFoundException() {
		Client client = new Client(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address1 = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client);
		client.setAddress(address1);
		
		when(clientRepository.getReferenceById(2L)).thenThrow(EntityNotFoundException.class);
		
		assertThrows(ResourceNotFoundException.class, () -> clientService.update(client, 2L));
	}

}
