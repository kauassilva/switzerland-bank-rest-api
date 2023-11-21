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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Costumer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.AddressRepository;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AddressResourceIntegration {
    
    
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private AddressRepository repository;

	private Address address;

    @BeforeEach
	void setUp() {
		Costumer costumer = new Costumer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		address = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", costumer);
		costumer.setAddress(address);
	}

    @Test
	void testFindAll_ReturnsStatusOk() {
		ResponseEntity<List<Address>> response = testRestTemplate
				.exchange("/api/addresses", HttpMethod.GET, null, new ParameterizedTypeReference<List<Address>> () {});
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

    @Test
	void testFindAll_ReturnsContentSuccessfully() {
		List<Address> expectedResponse = repository.findAll();
		
		ResponseEntity<List<Address>> response = testRestTemplate
				.exchange("/api/addresses", HttpMethod.GET, null, new ParameterizedTypeReference<List<Address>> () {});
		
		assertEquals(expectedResponse, response.getBody());
	}

    @Test
	void testFindByID_ReturnsStatusOk() {
		Long id = 1L;
		
		ResponseEntity<Address> response = testRestTemplate
				.exchange("/api/addresses/" + id, HttpMethod.GET, null, Address.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
    
    @Test
	void testFindById_ReturnsStatusNotFound() {
		Long id = 0L;
		
		ResponseEntity<Address> response = testRestTemplate
				.exchange("/api/addresses/" + id, HttpMethod.GET, null, Address.class);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

    @Test
	void testFindById_ReturnsCorrectContent() {
		Optional<Address> expectedResult = repository.findById(1L);
		
		ResponseEntity<Address> response = testRestTemplate
				.exchange("/api/addresses/" + expectedResult.get().getId(), HttpMethod.GET, null, Address.class);
		
		assertEquals(expectedResult.get(), response.getBody());
	}

    

}
