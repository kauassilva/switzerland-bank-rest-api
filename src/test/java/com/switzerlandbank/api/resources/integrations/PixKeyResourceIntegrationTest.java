package com.switzerlandbank.api.resources.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Customer;
import com.switzerlandbank.api.entities.PixKey;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.entities.enums.KeyType;
import com.switzerlandbank.api.repositories.PixKeyRepository;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PixKeyResourceIntegrationTest {
    
    @Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private PixKeyRepository repository;

    private Account account;
	
	private PixKey pixKey;

    private PixKey pixKeyCPF;

    private PixKey pixKeyEmail;

    private PixKey pixKeyRandom;

    
    @BeforeEach
	void setUp() {

		Customer costumer1 = new Customer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
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
	void testFindAll_ReturnsStatusOk() {
		ResponseEntity<List<PixKey>> response = testRestTemplate
				.exchange("/api/pixkeys", HttpMethod.GET, null, new ParameterizedTypeReference<List<PixKey>> () {});
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

    @Test
	void testFindAll_ReturnsCorrectContent() {
		List<PixKey> expectedResponse = repository.findAll();
		
		ResponseEntity<List<PixKey>> response = testRestTemplate
				.exchange("/api/pixkeys", HttpMethod.GET, null, new ParameterizedTypeReference<List<PixKey>> () {});
		
		assertEquals(expectedResponse, response.getBody());
	}

    @Test
	void testFindByAccountId_ReturnsStatusOk() {
		
		Long accountId = 1L;
		
		ResponseEntity<List<PixKey>> response = testRestTemplate
				.exchange("/api/pixkeys/account/" + accountId, HttpMethod.GET, null, new ParameterizedTypeReference<List<PixKey>> () {});
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void testFindByAccountId_ReturnsCorrectContent() {

		Long accountId = 1L;

		List<PixKey> expectedResponse = repository.findByAccountId(1L);
		
		ResponseEntity<List<PixKey>> response = testRestTemplate
				.exchange("/api/pixkeys/account/"+ accountId, HttpMethod.GET, null, new ParameterizedTypeReference<List<PixKey>> () {});
		
		assertEquals(expectedResponse, response.getBody());

	}

	@Test
	void testFindByID_ReturnsStatusOk() {
		Long id = 1L;
		
		ResponseEntity<PixKey> response = testRestTemplate
				.exchange("/api/pixkeys/" + id, HttpMethod.GET, null, PixKey.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testFindById_ReturnsStatusNotFound() {
		Long id = 0L;
		
		ResponseEntity<PixKey> response = testRestTemplate
				.exchange("/api/pixkeys/" + id, HttpMethod.GET, null, PixKey.class);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void testFindById_ReturnsCorrectContent() {
		Optional<PixKey> expectedResult = repository.findById(1L);
		
		ResponseEntity<PixKey> response = testRestTemplate
				.exchange("/api/pixkeys/" + expectedResult.get().getId(), HttpMethod.GET, null, PixKey.class);
		
		assertEquals(expectedResult.get(), response.getBody());
	}

	@Test
	void testInsert_ReturnsCorrectContentPixKeyTypeCPF() {
		HttpEntity<PixKey> httpEntity = new HttpEntity<>(pixKeyCPF);
		
		ResponseEntity<PixKey> response = testRestTemplate
				.exchange("/api/pixkeys", HttpMethod.POST, httpEntity, PixKey.class);
		
		Optional<PixKey> expectedPixKey = repository.findById(response.getBody().getId());

		assertEquals(expectedPixKey.get(), response.getBody());
	}

	@Test
	void testInsert_ReturnsCorrectContentPixKeyTypeEmail() {
		HttpEntity<PixKey> httpEntity = new HttpEntity<>(pixKeyEmail);
		
		ResponseEntity<PixKey> response = testRestTemplate
				.exchange("/api/pixkeys", HttpMethod.POST, httpEntity, PixKey.class);
		
		Optional<PixKey> expectedPixKey = repository.findById(response.getBody().getId());

		assertEquals(expectedPixKey.get(), response.getBody());
	}

	@Test
	void testInsert_ReturnsCorrectContentPixKeyTypeRandom() {
		HttpEntity<PixKey> httpEntity = new HttpEntity<>(pixKeyRandom);
		
		ResponseEntity<PixKey> response = testRestTemplate
				.exchange("/api/pixkeys", HttpMethod.POST, httpEntity, PixKey.class);
		
		Optional<PixKey> expectedPixKey = repository.findById(response.getBody().getId());

		assertEquals(expectedPixKey.get(), response.getBody());
	}

	@Test
	void testInsert_ReturnsStatusCreated() {
		HttpEntity<PixKey> httpEntity = new HttpEntity<>(pixKey);
		
		ResponseEntity<PixKey> response = testRestTemplate
				.exchange("/api/pixkeys", HttpMethod.POST, httpEntity, PixKey.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	void testInsert_ReturnsStatusBadRequest() {
		PixKey wrongPixKey= new PixKey(null, null, KeyType.CPF, null);
		
		HttpEntity<PixKey> httpEntity = new HttpEntity<>(wrongPixKey);
		
		ResponseEntity<PixKey> response = testRestTemplate
				.exchange("/api/pixkeys", HttpMethod.POST, httpEntity, PixKey.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void testDelete_ReturnsStatusNoContent() {
		PixKey savedPixKey = repository.save(pixKey);
		
		ResponseEntity<Void> response = testRestTemplate
				.exchange("/api/pixkeys/" + savedPixKey.getId(), HttpMethod.DELETE, null, Void.class);
		
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	void testDelete_ReturnsStatusNotFound() {
		Long id = -1L;
		
		ResponseEntity<Void> response = testRestTemplate
				.exchange("/api/pixkeys/" + id, HttpMethod.DELETE, null, Void.class);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

}
