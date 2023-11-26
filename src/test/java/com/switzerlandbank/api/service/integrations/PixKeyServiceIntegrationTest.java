package com.switzerlandbank.api.service.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Costumer;
import com.switzerlandbank.api.entities.PixKey;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.entities.enums.KeyType;
import com.switzerlandbank.api.repositories.PixKeyRepository;
import com.switzerlandbank.api.services.PixKeyService;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;


@SpringBootTest
public class PixKeyServiceIntegrationTest {
    
    @Autowired
	private PixKeyService pixKeyService;

	@Autowired
	private PixKeyRepository repository;

    private Account account;
	
	private PixKey pixKey;

    private PixKey pixKeyCPF;

    private PixKey pixKeyEmail;

    private PixKey pixKeyRandom;

	@BeforeEach
	void setUp() {
		Costumer costumer1 = new Costumer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
        Address address1 = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", costumer1);
        costumer1.setAddress(address1);
        account = new Account(1L, "123456", costumer1);
		costumer1.setAccount(account);


        pixKey = new PixKey(null, null, KeyType.CPF, account);
		pixKeyCPF = new PixKey(null, null, KeyType.CPF, account);
		pixKeyEmail = new PixKey(null, null, KeyType.EMAIL, account);
		pixKeyRandom = new PixKey(null, null, KeyType.RANDOM, account);
	}

    @Test
	void testFindAll_ReturnsCorrectContent() {
		List<PixKey> expectedResult = repository.findAll();
		
		List<PixKey> result = pixKeyService.findAll();
		
		assertEquals(expectedResult, result);
	}

	@Test
	void testFindByAccountId_ReturnsCorrectContent() {

		List<PixKey> expectedResult = repository.findByAccountId(account.getId());

		List<PixKey> result = pixKeyService.findByAccountId(account.getId());

		assertEquals(expectedResult, result);
	}


    @Test
	void testFindById_ReturnCorrectContent() {
		Long id = 1L;

		pixKey = new PixKey(1L, null, KeyType.CPF, account);
		
		PixKey result = pixKeyService.findById(id);
		
		assertEquals(pixKey, result);
	}

	@Test
	void testFindById_ThrowsResourceNotFoundException() {
		Long id = 0L;
		
		assertThrows(ResourceNotFoundException.class, () -> pixKeyService.findById(id));
	}

	@Test
	void testInsert_CorrectContentKeyTypeCPF() {

		PixKey expectedResult = pixKeyService.insert(pixKeyCPF); 

		PixKey result = pixKeyService.findById(expectedResult.getId());

		assertEquals(expectedResult, result);

	}

	@Test
	void testInsert_CorrectContentKeyTypeEmail() {

		PixKey expectedResult = pixKeyService.insert(pixKeyEmail); 

		PixKey result = pixKeyService.findById(expectedResult.getId());

		assertEquals(expectedResult, result);

	}

	@Test
	void testInsert_CorrectContentKeyTypeRandom() {

		PixKey expectedResult = pixKeyService.insert(pixKeyRandom); 

		PixKey result = pixKeyService.findById(expectedResult.getId());

		assertEquals(expectedResult, result);

	}

	@Test
	void testDelete_Success() {

		PixKey pixKey = pixKeyService.insert(pixKeyEmail);

		pixKeyService.delete(pixKey.getId());
		
		assertThrows(ResourceNotFoundException.class, () -> pixKeyService.findById(pixKey.getId()));
	}
	
	@Test
	void testDelete_ThrowsResourceNotFoundException() {
		Long id = 0L;
		
		assertThrows(ResourceNotFoundException.class, () -> pixKeyService.delete(id));
	}

}
