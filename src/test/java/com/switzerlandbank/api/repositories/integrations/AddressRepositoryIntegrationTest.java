package com.switzerlandbank.api.repositories.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Costumer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.AddressRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class AddressRepositoryIntegrationTest {

	@Autowired
	private AddressRepository addressRepository;
	
	private Address address;
	private Costumer costumer;
	
	@BeforeEach
	void setUp() {
		costumer = new Costumer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		address = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", costumer);
		costumer.setAddress(address);
	}
	
	@Test
	void findById_Sucess() {
		Address savedAddress = addressRepository.save(address);

		Optional<Address> foundAddress = addressRepository.findById(savedAddress.getId());

		assertTrue(foundAddress.isPresent());
	}

	@Test
	void testFindById_NotFound() {
		Address savedAddress = addressRepository.save(address);

		Optional<Address> foundCostumer = addressRepository.findById(savedAddress.getId() + 1L);

		assertTrue(foundCostumer.isEmpty());
	}

	@Test
	void testSave_Success() {
		Address savedAddress = addressRepository.save(address);

		assertEquals(address, savedAddress);
	}

	@Test
	void testSave_ThrowsDataIntegrityViolationException() {
		Address wrongAddress = new Address(null, null, "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", costumer);

		assertThrows(DataIntegrityViolationException.class, () -> addressRepository.save(wrongAddress));
	}
	
	@Test
	void testSave_ThrowsJpaSystemException() {
		Address wrongAddress = new Address(null, null, "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", null);

		assertThrows(JpaSystemException.class, () -> addressRepository.save(wrongAddress));
	}

	@Test
	void testDelete_Success() {
		Address savedAddress = addressRepository.save(address);

		addressRepository.deleteById(savedAddress.getId());

		Optional<Address> foundAddress = addressRepository.findById(savedAddress.getId());

		assertTrue(foundAddress.isEmpty());
	}

}
