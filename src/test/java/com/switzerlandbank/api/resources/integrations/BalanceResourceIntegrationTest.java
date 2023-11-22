package com.switzerlandbank.api.resources.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.switzerlandbank.api.entities.Balance;
import com.switzerlandbank.api.repositories.BalanceRepository;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BalanceResourceIntegrationTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private BalanceRepository balanceRepository;

	@Test
	void testFindAll_ReturnsStatusOk() {
		ResponseEntity<List<Balance>> response = testRestTemplate
				.exchange("/api/balances", HttpMethod.GET, null, new ParameterizedTypeReference<List<Balance>> () {});
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void testFindAll_ReturnsContentSuccessfully() {
		List<Balance> expectedResponse = balanceRepository.findAll();
		
		ResponseEntity<List<Balance>> response = testRestTemplate
				.exchange("/api/balances", HttpMethod.GET, null, new ParameterizedTypeReference<List<Balance>> () {});
		
		assertEquals(expectedResponse, response.getBody());
	}
	
	@Test
	void testFindByID_ReturnsStatusOk() {
		Long id = 1L;
		
		ResponseEntity<Balance> response = testRestTemplate
				.exchange("/api/balances/" + id, HttpMethod.GET, null, Balance.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void testFindById_ReturnsCorrectContent() {
		Long id = 1L;
		Optional<Balance> expectedResponse = balanceRepository.findById(id);
		
		ResponseEntity<Balance> response = testRestTemplate
				.exchange("/api/balances/" + id, HttpMethod.GET, null, Balance.class);
		
		assertEquals(expectedResponse.get(), response.getBody());
	}
	
	@Test
	void testFindById_ReturnsStatusNotFound() {
		Long id = -1L;
		
		ResponseEntity<Balance> response = testRestTemplate
				.exchange("/api/balances/" + id, HttpMethod.GET, null, Balance.class);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

}
