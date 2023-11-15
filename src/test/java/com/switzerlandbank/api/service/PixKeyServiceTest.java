package com.switzerlandbank.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.beans.factory.annotation.Autowired;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Client;
import com.switzerlandbank.api.entities.PixKey;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.entities.enums.KeyType;
import com.switzerlandbank.api.repositories.AccountRepository;
import com.switzerlandbank.api.repositories.PixKeyRepository;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;
import com.switzerlandbank.api.services.impls.PixKeyServiceImpl;

class PixKeyServiceTest {

	@Mock
	private PixKeyRepository pixKeyRepository;

	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	@Autowired
	private PixKeyServiceImpl service;

	private PixKey pixKey;
	private Account account;

	@BeforeEach
	void setUp() {
		Client client1 = new Client(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
        Address address1 = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client1);
        client1.setAddress(address1);
        account = new Account(1L, "123456", client1);
		client1.setAccount(account);

		pixKey = new PixKey(1L, "1", KeyType.CPF, account);
		MockitoAnnotations.openMocks(this);

	}

	@Test
	void findAllPixKey() {
		List<PixKey> pixKeyList = new ArrayList<>();
		pixKeyList.add(pixKey);

		when(pixKeyRepository.findAll()).thenReturn(pixKeyList);
		List<PixKey> result = service.findAll();
		assertEquals(pixKeyList, result);
	}

	@Test
	void findAllPixKeyButPixKeyIsEmpty(){
		when(pixKeyRepository.findAll()).thenReturn(Collections.emptyList());
		List<PixKey> result = service.findAll();
		assertTrue(result.isEmpty());
	}

	@Test
	void findPixKeyById(){
		when(pixKeyRepository.findById(1L)).thenReturn(Optional.of(pixKey));
		PixKey result = service.findById(1L);
		assertEquals(pixKey, result);
	}

	@Test
	void findPixKeyByIdButIsEmpty(){
		when(pixKeyRepository.findById(1L)).thenReturn(Optional.of(pixKey));
		assertThrows(ResourceNotFoundException.class,() -> service.findById(6L));
	}

	@Test
	void findByAccountId() {
		List<PixKey> pixKeyList = new ArrayList<>();
		pixKeyList.add(pixKey);

		when(pixKeyRepository.findByAccountId(1L)).thenReturn(pixKeyList);
		List<PixKey> result = service.findByAccountId(1L);
		assertEquals(pixKeyList, result);
	}

	@Test
	void findByAccountIdButIsEmpty(){
		when(pixKeyRepository.findByAccountId(1L)).thenReturn(Collections.emptyList());
		List<PixKey> result = service.findByAccountId(1L);
		assertTrue(result.isEmpty());
	}

	@Test
	void PixKeyInsert(){
		when(accountRepository.getReferenceById(1L)).thenReturn(account);
		when(pixKeyRepository.save(pixKey)).thenReturn(pixKey);
		PixKey result = service.insert(pixKey);
		assertEquals(pixKey, result);
	}

}
