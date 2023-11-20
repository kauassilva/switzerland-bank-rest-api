package com.switzerlandbank.api.service;

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
import org.springframework.beans.factory.annotation.Autowired;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Costumer;
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
	private PixKeyServiceImpl pixKeyService;

	private PixKey pixKeyEmpty;
	private Account account;

	@BeforeEach
	void setUp() {
		Costumer costumer1 = new Costumer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
        Address address1 = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", costumer1);
        costumer1.setAddress(address1);
        account = new Account(1L, "123456", costumer1);
		costumer1.setAccount(account);

		pixKeyEmpty = new PixKey(1L, "1", KeyType.CPF, account);
		MockitoAnnotations.openMocks(this);

	}

	@Test
	void findAllPixKey() {
		List<PixKey> pixKeyList = new ArrayList<>();
		pixKeyList.add(pixKeyEmpty);

		when(pixKeyRepository.findAll()).thenReturn(pixKeyList);
		List<PixKey> result = pixKeyService.findAll();
		assertEquals(pixKeyList, result);
	}

	@Test
	void findAllPixKeyButPixKeyIsEmpty(){
		when(pixKeyRepository.findAll()).thenReturn(Collections.emptyList());
		List<PixKey> result = pixKeyService.findAll();
		assertTrue(result.isEmpty());
	}

	@Test
	void findPixKeyById(){
		when(pixKeyRepository.findById(1L)).thenReturn(Optional.of(pixKeyEmpty));
		PixKey result = pixKeyService.findById(1L);
		assertEquals(pixKeyEmpty, result);
	}

	@Test
	void findPixKeyByIdButIsEmpty(){
		when(pixKeyRepository.findById(1L)).thenReturn(Optional.of(pixKeyEmpty));
		assertThrows(ResourceNotFoundException.class,() -> pixKeyService.findById(6L));
	}

	@Test
	void findByAccountId() {
		List<PixKey> pixKeyList = new ArrayList<>();
		pixKeyList.add(pixKeyEmpty);

		when(pixKeyRepository.findByAccountId(1L)).thenReturn(pixKeyList);
		List<PixKey> result = pixKeyService.findByAccountId(1L);
		assertEquals(pixKeyList, result);
	}

	@Test
	void findByAccountIdButIsEmpty(){
		when(pixKeyRepository.findByAccountId(1L)).thenReturn(Collections.emptyList());
		List<PixKey> result = pixKeyService.findByAccountId(1L);
		assertTrue(result.isEmpty());
	}

	@Test
	void PixKeyInsert(){
		when(accountRepository.getReferenceById(1L)).thenReturn(account);
		when(pixKeyRepository.save(pixKeyEmpty)).thenReturn(pixKeyEmpty);
		PixKey result = pixKeyService.insert(pixKeyEmpty);
		assertEquals(pixKeyEmpty, result);
	}

	@Test
	void PixKeyInsertButIsEmpty(){
		PixKey pixKeyEmpty = new PixKey(1L, null, KeyType.CPF, null);
		when(pixKeyRepository.save(pixKeyEmpty)).thenReturn(pixKeyEmpty);

		assertThrows(NullPointerException.class,() -> pixKeyService.insert(pixKeyEmpty));

	}
	
	@Test
	void PixKeyDelete(){

		when(pixKeyRepository.existsById(1L)).thenReturn(true);
		pixKeyService.delete(1L);
		verify(pixKeyRepository, times(1)).deleteById(1L);
	}

	@Test
	void PixKeyDeleteFail(){
		when(pixKeyRepository.existsById(1L)).thenReturn(true);
		assertThrows(ResourceNotFoundException.class, () -> pixKeyService.delete(2L));
	}

	

}
