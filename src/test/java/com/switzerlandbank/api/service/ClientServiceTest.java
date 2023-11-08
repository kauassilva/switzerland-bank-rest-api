package com.switzerlandbank.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
		Address address1 = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000");
		Client client1 = new Client(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123", address1);
		Client client2 = new Client(null, "Ana Santos", "98765432100", "Beatriz Santos", LocalDate.parse("1990-02-20"), Gender.FEMALE, "anasantos@example.com", "AnaSantos123", address1);

		List<Client> expectedResult = new ArrayList<>();
		expectedResult.add(client1);
		expectedResult.add(client2);
		
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
	@DisplayName("Should get client by id successfully when everything is ok")
	void testFindByIdSuccess() {
		Client client = mock(Client.class);
		when(repository.findById(anyLong())).thenReturn(Optional.of(client));
		Client result = service.findById(1L);
		assertEquals(client, result);
	}

	@Test
	@DisplayName("Should throw ResourceNotFoundException when request an id inexistent")
	void testFindByIdThrowsException() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
	}
	
	@Test
	void testInsert() {
		Client client = mock(Client.class);
		when(repository.save(any(Client.class))).thenReturn(client);
		Client result = service.insert(client);
		assertEquals(client, result);
	}
	
	@Test
	void testDeleteSuccess() {
		when(repository.existsById(anyLong())).thenReturn(true);
		service.delete(1L);
		verify(repository, times(1)).deleteById(1L);
	}
	
	@Test
	void testDeleteThrowsException() {
		when(repository.existsById(anyLong())).thenReturn(false);
		assertThrows(ResourceNotFoundException.class, () -> service.delete(1L));
	}
	
	@Test
	void testUpdateSuccess() {
		Client client = mock(Client.class);
		when(repository.getReferenceById(anyLong())).thenReturn(client);
		when(repository.save(any(Client.class))).thenReturn(client);
		Client result = service.update(client, 1L);
		assertEquals(client, result);
	}
	
	@Test
	void testUpdateThrowsException() {
		Client client = mock(Client.class);
		when(repository.getReferenceById(anyLong())).thenThrow(EntityNotFoundException.class);
		assertThrows(ResourceNotFoundException.class, () -> service.update(client, 1L));
	}

}
