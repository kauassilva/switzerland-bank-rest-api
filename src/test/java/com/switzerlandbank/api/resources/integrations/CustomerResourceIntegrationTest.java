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

import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Customer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.CustomerRepository;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CustomerResourceIntegrationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private CustomerRepository repository;

	private Customer costumer;
	
	@BeforeEach
	void setUp() {
		costumer = new Customer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", costumer);
		costumer.setAddress(address);
	}

	@Test
	void testFindAll_ReturnsStatusOk() {
		ResponseEntity<List<Customer>> response = testRestTemplate
				.exchange("/api/customers", HttpMethod.GET, null, new ParameterizedTypeReference<List<Customer>> () {});
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void testFindAll_ReturnsCorrectBody() {
		List<Customer> expectedResponse = repository.findAll();
		
		ResponseEntity<List<Customer>> response = testRestTemplate
				.exchange("/api/customers", HttpMethod.GET, null, new ParameterizedTypeReference<List<Customer>> () {});
		
		assertEquals(expectedResponse, response.getBody());
	}
	
	@Test
	void testFindByID_ReturnsStatusOk() {
		Long id = 1L;
		
		ResponseEntity<Customer> response = testRestTemplate
				.exchange("/api/customers/" + id, HttpMethod.GET, null, Customer.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void testFindById_ReturnsStatusNotFound() {
		Long id = 0L;
		
		ResponseEntity<Customer> response = testRestTemplate
				.exchange("/api/customers/" + id, HttpMethod.GET, null, Customer.class);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	void testFindById_ReturnsCorrectContent() {
		Optional<Customer> expectedResult = repository.findById(1L);
		
		ResponseEntity<Customer> response = testRestTemplate
				.exchange("/api/customers/" + expectedResult.get().getId(), HttpMethod.GET, null, Customer.class);
		
		assertEquals(expectedResult.get(), response.getBody());
	}
	
	@Test
	void testInsert_ReturnsStatusCreated() {
		HttpEntity<Customer> httpEntity = new HttpEntity<>(costumer);
		
		ResponseEntity<Customer> response = testRestTemplate
				.exchange("/api/customers", HttpMethod.POST, httpEntity, Customer.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}
	
	@Test
	void testInsert_ReturnsStatusBadRequest() {
		Customer wrongCostumer = new Customer(null, null, null, null, null, Gender.OTHER, null, null);
		
		HttpEntity<Customer> httpEntity = new HttpEntity<>(wrongCostumer);
		
		ResponseEntity<Customer> response = testRestTemplate
				.exchange("/api/customers", HttpMethod.POST, httpEntity, Customer.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	void testDelete_ReturnsStatusNoContent() {
		Customer savedCostumer = repository.save(costumer);
		
		ResponseEntity<Void> response = testRestTemplate
				.exchange("/api/customers/" + savedCostumer.getId(), HttpMethod.DELETE, null, Void.class);
		
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
	
	@Test
	void testDelete_ReturnsStatusNotFound() {
		Long id = -1L;
		
		ResponseEntity<Void> response = testRestTemplate
				.exchange("/api/customers/" + id, HttpMethod.DELETE, null, Void.class);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	void testUpdate_ReturnsStatusOk() {
		Customer costumer = repository.save(this.costumer);
		
		Customer updatedCostumer = new Customer(null, "Carlos Pereira", "11122233344", "Teresa Pereira", LocalDate.parse("1975-10-10"), Gender.OTHER, "carlospereira@gmail.com", "CarlosPereira123");
		Address updatedAddress = new Address(null, "R. Cento e Cinquenta e Dois", "196", "Laranjal", "Volta Redonda", "Rio de Janeiro", "27255020", costumer);
		updatedCostumer.setAddress(updatedAddress);
		
		HttpEntity<Customer> httpEntity = new HttpEntity<>(updatedCostumer);
		
		ResponseEntity<Customer> response = testRestTemplate
				.exchange("/api/customers/" + costumer.getId(), HttpMethod.PUT, httpEntity, Customer.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void testUpdate_ReturnsStatusNotFound() {
		Long id = -1L;
		
		Customer updatedCostumer = new Customer(null, "Carlos Pereira", "11122233344", "Teresa Pereira", LocalDate.parse("1975-10-10"), Gender.OTHER, "carlospereira@gmail.com", "CarlosPereira123");
		Address updatedAddress = new Address(null, "R. Cento e Cinquenta e Dois", "196", "Laranjal", "Volta Redonda", "Rio de Janeiro", "27255020", costumer);
		updatedCostumer.setAddress(updatedAddress);
		
		HttpEntity<Customer> httpEntity = new HttpEntity<>(updatedCostumer);
		
		ResponseEntity<Customer> response = testRestTemplate
				.exchange("/api/customers/" + id, HttpMethod.PUT, httpEntity, Customer.class);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	void testUpdate_ReturnsCorrectContent() {
		Customer costumer = repository.save(this.costumer);
		
		Customer updatedCostumer = new Customer(4L, "Carlos Pereira", "11122233344", "Teresa Pereira", LocalDate.parse("1975-10-10"), Gender.OTHER, "carlospereira@gmail.com", "CarlosPereira123");
		Address updatedAddress = new Address(4L, "R. Cento e Cinquenta e Dois", "196", "Laranjal", "Volta Redonda", "Rio de Janeiro", "27255020", costumer);
		updatedCostumer.setAddress(updatedAddress);
		
		HttpEntity<Customer> httpEntity = new HttpEntity<>(updatedCostumer);
		
		ResponseEntity<Customer> response = testRestTemplate
				.exchange("/api/customers/" + costumer.getId(), HttpMethod.PUT, httpEntity, Customer.class);
		
		assertEquals(costumer, response.getBody());
	}

}
