package com.switzerlandbank.api.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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

import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Client;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.ClientRepository;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ClientResourceIntegrationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private ClientRepository repository;

	private Client client;
	
	@BeforeEach
	void setUp() {
		client = new Client(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client);
		client.setAddress(address);
	}

	@Test
	void testFindAll_ReturnsStatusOk() {
		ResponseEntity<List<Client>> response = testRestTemplate
				.exchange("/api/clients", HttpMethod.GET, null, new ParameterizedTypeReference<List<Client>> () {});
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void testFindAll_ReturnsCorrectBody() {
		List<Client> expectedResponse = repository.findAll();
		
		ResponseEntity<List<Client>> response = testRestTemplate
				.exchange("/api/clients", HttpMethod.GET, null, new ParameterizedTypeReference<List<Client>> () {});
		
		assertEquals(expectedResponse, response.getBody());
	}
	
	@Test
	void testFindAll_ReturnsEmptyList() {
		List<Client> expectedResponse = Collections.emptyList();
		repository.deleteAll();
		
		ResponseEntity<List<Client>> response = testRestTemplate
				.exchange("/api/clients", HttpMethod.GET, null, new ParameterizedTypeReference<List<Client>> () {});
		
		assertEquals(expectedResponse, response.getBody());
	}
	
	@Test
	void testFindByID_ReturnsStatusOk() {
		Client newClient = repository.save(client);
		
		ResponseEntity<Client> response = testRestTemplate
				.exchange("/api/clients/" + newClient.getId(), HttpMethod.GET, null, Client.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void testFindById_ReturnsStatusNotFound() {
		ResponseEntity<Client> response = testRestTemplate
				.exchange("/api/clients/" + -1, HttpMethod.GET, null, Client.class);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	void testFindById_ReturnsCorrectContent() {
		Client newClient = repository.save(client);
		
		ResponseEntity<Client> response = testRestTemplate
				.exchange("/api/clients/" + newClient.getId(), HttpMethod.GET, null, Client.class);
		
		assertEquals(newClient, response.getBody());
	}
	
	@Test
	void testInsert_ReturnsStatusCreated() {
		HttpEntity<Client> httpEntity = new HttpEntity<>(client);
		
		ResponseEntity<Client> response = testRestTemplate
				.exchange("/api/clients", HttpMethod.POST, httpEntity, Client.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}
	
	@Test
	void testInsert_ReturnsStatusBadRequest() {
		Client wrongClient = new Client(null, null, null, null, null, Gender.OTHER, null, null);
		
		HttpEntity<Client> httpEntity = new HttpEntity<>(wrongClient);
		
		ResponseEntity<Client> response = testRestTemplate
				.exchange("/api/clients", HttpMethod.POST, httpEntity, Client.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	/*@Test
	void testInsert_ReturnsCorrectContent() {
		HttpEntity<Client> httpEntity = new HttpEntity<>(client);
		
		ResponseEntity<Client> response = testRestTemplate
				.exchange("/api/clients", HttpMethod.POST, httpEntity, Client.class);
		
		assertThat(client.getId()).isEqualTo(response.getBody().getId());
	}*/

}
