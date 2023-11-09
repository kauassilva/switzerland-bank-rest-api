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
import org.springframework.beans.factory.annotation.Autowired;

import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Client;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.ClientRepository;
import com.switzerlandbank.api.services.ClientServiceImpl;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

class ClientServiceTest {

	@Mock
	private ClientRepository repository;
	
	@Autowired
	@InjectMocks
	private ClientServiceImpl service;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testFindAllReturnNonEmptyList() {
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
		
		when(repository.findAll()).thenReturn(expectedResult);
		
		List<Client> result = service.findAll();
		
		assertEquals(expectedResult, result);
	}
	
	@Test
	void testFindAllReturnEmptyList() {
		when(repository.findAll()).thenReturn(Collections.emptyList());
		List<Client> result = service.findAll();
		assertTrue(result.isEmpty());
	}
	
	@Test
	void testFindByIdSuccess() {
		Client expectedClient = new Client(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", expectedClient);
		expectedClient.setAddress(address);
		
		when(repository.findById(1L)).thenReturn(Optional.of(expectedClient));
		Client result = service.findById(1L);
		assertEquals(expectedClient, result);
	}

	@Test
	void testFindByIdThrowsException() {
		Client client = new Client(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client);
		client.setAddress(address);
		
		when(repository.findById(1L)).thenReturn(Optional.of(client));
		
		assertThrows(ResourceNotFoundException.class, () -> service.findById(2L));
	}
	
	@Test
	void testInsert() {
		Client expectedClient = new Client(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", expectedClient);
		expectedClient.setAddress(address);
		
		when(repository.save(expectedClient)).thenReturn(expectedClient);
		
		Client result = service.insert(expectedClient);
		
		assertEquals(expectedClient, result);
	}
	
	@Test
	void testDeleteSuccess() {
		when(repository.existsById(1L)).thenReturn(true);
		service.delete(1L);
		verify(repository, times(1)).deleteById(1L);
	}
	
	@Test
	void testDeleteThrowsException() {
		when(repository.existsById(1L)).thenReturn(true);
		assertThrows(ResourceNotFoundException.class, () -> service.delete(2L));
	}
	
	@Test
	void testUpdateSuccess() {
		Client updatedClient = new Client(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address1 = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", updatedClient);
		updatedClient.setAddress(address1);
		
		Client currentClient = new Client(1L, "Carlos Pereira", "11122233344", "Teresa Pereira", LocalDate.parse("1975-10-10"), Gender.OTHER, "carlospereira@gmail.com", "CarlosPereira123");
		Address address2 = new Address(1L, "R. Cento e Cinquenta e Dois", "196", "Laranjal", "Volta Redonda", "Rio de Janeiro", "27255020", currentClient);
		currentClient.setAddress(address2);
		
		when(repository.getReferenceById(1L)).thenReturn(currentClient);
		when(repository.save(updatedClient)).thenReturn(updatedClient);
		
		Client result = service.update(updatedClient, 1L);
		
		assertEquals(updatedClient, result);
	}
	
	@Test
	void testUpdateThrowsException() {
		Client client = new Client(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address1 = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client);
		client.setAddress(address1);
		
		when(repository.getReferenceById(2L)).thenThrow(EntityNotFoundException.class);
		
		assertThrows(ResourceNotFoundException.class, () -> service.update(client, 2L));
	}

}
