package com.switzerlandbank.api.resources;

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
import com.switzerlandbank.api.entities.Costumer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.CostumerRepository;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class costumerResourceIntegrationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private CostumerRepository repository;

	private Costumer costumer;
	
	@BeforeEach
	void setUp() {
		costumer = new Costumer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", costumer);
		costumer.setAddress(address);
	}

	@Test
	void testFindAll_ReturnsStatusOk() {
		ResponseEntity<List<Costumer>> response = testRestTemplate
				.exchange("/api/costumers", HttpMethod.GET, null, new ParameterizedTypeReference<List<Costumer>> () {});
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void testFindAll_ReturnsCorrectBody() {
		List<Costumer> expectedResponse = repository.findAll();
		
		ResponseEntity<List<Costumer>> response = testRestTemplate
				.exchange("/api/costumers", HttpMethod.GET, null, new ParameterizedTypeReference<List<Costumer>> () {});
		
		assertEquals(expectedResponse, response.getBody());
	}
	
	@Test
	void testFindAll_ReturnsEmptyList() {
		List<Costumer> expectedResponse = Collections.emptyList();
		repository.deleteAll();
		
		ResponseEntity<List<Costumer>> response = testRestTemplate
				.exchange("/api/costumers", HttpMethod.GET, null, new ParameterizedTypeReference<List<Costumer>> () {});
		
		assertEquals(expectedResponse, response.getBody());
	}
	
	@Test
	void testFindByID_ReturnsStatusOk() {
		Costumer newCostumer = repository.save(costumer);
		
		ResponseEntity<Costumer> response = testRestTemplate
				.exchange("/api/costumers/" + newCostumer.getId(), HttpMethod.GET, null, Costumer.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void testFindById_ReturnsStatusNotFound() {
		ResponseEntity<Costumer> response = testRestTemplate
				.exchange("/api/costumers/" + -1, HttpMethod.GET, null, Costumer.class);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	void testFindById_ReturnsCorrectContent() {
		Costumer newCostumer = repository.save(costumer);
		
		ResponseEntity<Costumer> response = testRestTemplate
				.exchange("/api/costumers/" + newCostumer.getId(), HttpMethod.GET, null, Costumer.class);
		
		assertEquals(newCostumer, response.getBody());
	}
	
	@Test
	void testInsert_ReturnsStatusCreated() {
		HttpEntity<Costumer> httpEntity = new HttpEntity<>(costumer);
		
		ResponseEntity<Costumer> response = testRestTemplate
				.exchange("/api/costumers", HttpMethod.POST, httpEntity, Costumer.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}
	
	@Test
	void testInsert_ReturnsStatusBadRequest() {
		Costumer wrongCostumer = new Costumer(null, null, null, null, null, Gender.OTHER, null, null);
		
		HttpEntity<Costumer> httpEntity = new HttpEntity<>(wrongCostumer);
		
		ResponseEntity<Costumer> response = testRestTemplate
				.exchange("/api/costumers", HttpMethod.POST, httpEntity, Costumer.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	void testDelete_ReturnsStatusNoContent() {
		Costumer savedCostumer = repository.save(costumer);
		
		ResponseEntity<Void> response = testRestTemplate
				.exchange("/api/costumers/" + savedCostumer.getId(), HttpMethod.DELETE, null, Void.class);
		
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
	
	@Test
	void testDelete_ReturnsStatusNotFound() {
		Long id = -1L;
		
		ResponseEntity<Void> response = testRestTemplate
				.exchange("/api/costumers/" + id, HttpMethod.DELETE, null, Void.class);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	void testUpdate_ReturnsStatusOk() {
		Costumer costumer = repository.save(this.costumer);
		
		Costumer updatedCostumer = new Costumer(null, "Carlos Pereira", "11122233344", "Teresa Pereira", LocalDate.parse("1975-10-10"), Gender.OTHER, "carlospereira@gmail.com", "CarlosPereira123");
		Address updatedAddress = new Address(null, "R. Cento e Cinquenta e Dois", "196", "Laranjal", "Volta Redonda", "Rio de Janeiro", "27255020", costumer);
		updatedCostumer.setAddress(updatedAddress);
		
		HttpEntity<Costumer> httpEntity = new HttpEntity<>(updatedCostumer);
		
		ResponseEntity<Costumer> response = testRestTemplate
				.exchange("/api/costumers/" + costumer.getId(), HttpMethod.PUT, httpEntity, Costumer.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void testUpdate_ReturnsStatusNotFound() {
		Long id = -1L;
		
		Costumer updatedCostumer = new Costumer(null, "Carlos Pereira", "11122233344", "Teresa Pereira", LocalDate.parse("1975-10-10"), Gender.OTHER, "carlospereira@gmail.com", "CarlosPereira123");
		Address updatedAddress = new Address(null, "R. Cento e Cinquenta e Dois", "196", "Laranjal", "Volta Redonda", "Rio de Janeiro", "27255020", costumer);
		updatedCostumer.setAddress(updatedAddress);
		
		HttpEntity<Costumer> httpEntity = new HttpEntity<>(updatedCostumer);
		
		ResponseEntity<Costumer> response = testRestTemplate
				.exchange("/api/costumers/" + id, HttpMethod.PUT, httpEntity, Costumer.class);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	void testUpdate_ReturnsCorrectContent() {
		Costumer costumer = repository.save(this.costumer);
		
		Costumer updatedCostumer = new Costumer(4L, "Carlos Pereira", "11122233344", "Teresa Pereira", LocalDate.parse("1975-10-10"), Gender.OTHER, "carlospereira@gmail.com", "CarlosPereira123");
		Address updatedAddress = new Address(4L, "R. Cento e Cinquenta e Dois", "196", "Laranjal", "Volta Redonda", "Rio de Janeiro", "27255020", costumer);
		updatedCostumer.setAddress(updatedAddress);
		
		HttpEntity<Costumer> httpEntity = new HttpEntity<>(updatedCostumer);
		
		ResponseEntity<Costumer> response = testRestTemplate
				.exchange("/api/costumers/" + costumer.getId(), HttpMethod.PUT, httpEntity, Costumer.class);
		
		assertEquals(costumer, response.getBody());
	}

}
