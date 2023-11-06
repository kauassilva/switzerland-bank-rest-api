package com.switzerlandbank.api.config;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Balance;
import com.switzerlandbank.api.entities.Client;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.AddressRepository;
import com.switzerlandbank.api.repositories.ClientRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private AddressRepository addressRepository;

	@Override
	public void run(String... args) throws Exception {
		
		Address address1 = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000");
		Address address2 = new Address(null, "R. Cento e Cinquenta e Dois", "196", "Laranjal", "Volta Redonda", "Rio de Janeiro", "27255020");
		
		Client client1 = new Client(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123", address1);
		Client client2 = new Client(null, "Carlos Pereira", "11122233344", "Teresa Pereira", LocalDate.parse("1975-10-10"), Gender.OTHER, "carlospereira@gmail.com", "CarlosPereira123", address2);
		Client client3 = new Client(null, "Ana Santos", "98765432100", "Beatriz Santos", LocalDate.parse("1990-02-20"), Gender.FEMALE, "anasantos@example.com", "AnaSantos123", address1);
		
		Balance balance1 = new Balance(null, BigDecimal.TEN, Instant.now(), client1);
		Balance balance2 = new Balance(null, BigDecimal.TEN, Instant.now(), client2);
		Balance balance3 = new Balance(null, BigDecimal.TEN, Instant.now(), client3);
		
		client1.setBalance(balance1);
		client2.setBalance(balance2);
		client3.setBalance(balance3);
		
		addressRepository.saveAll(Arrays.asList(address1, address2));
		clientRepository.saveAll(Arrays.asList(client1, client2, client3));
		
	}

}
