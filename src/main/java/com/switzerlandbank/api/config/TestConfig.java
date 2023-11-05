package com.switzerlandbank.api.config;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.switzerlandbank.api.entities.Client;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.repositories.ClientRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

	@Autowired
	private ClientRepository clientRepository;

	@Override
	public void run(String... args) throws Exception {
		
		Client client1 = new Client(null, "Elenor White", "12345678911", "Elisa White", LocalDate.parse("2023-11-05"), Gender.MALE, "elenor@gmail.com", "123456789");
		Client client2 = new Client(null, "Rayane Blue", "11987645321", "Nora Blue", LocalDate.parse("2014-01-01"), Gender.FEMALE, "rayane@gmail.com", "987654431");
		Client client3 = new Client(null, "Olave Green", "12345678911", "Alice Green", LocalDate.parse("2005-11-20"), Gender.OTHER, "Olave@gmail.com", "987321654");
		
		clientRepository.saveAll(Arrays.asList(client1, client2, client3));
		
	}

}
