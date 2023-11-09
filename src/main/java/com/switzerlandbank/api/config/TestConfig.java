package com.switzerlandbank.api.config;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.switzerlandbank.api.entities.Address;
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
		
		Client client1 = new Client(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Client client2 = new Client(null, "Carlos Pereira", "11122233344", "Teresa Pereira", LocalDate.parse("1975-10-10"), Gender.OTHER, "carlospereira@gmail.com", "CarlosPereira123");
		Client client3 = new Client(null, "Ana Santos", "98765432100", "Beatriz Santos", LocalDate.parse("1990-02-20"), Gender.FEMALE, "anasantos@example.com", "AnaSantos123");

		Address address1 = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client1);
		Address address2 = new Address(null, "R. Cento e Cinquenta e Dois", "196", "Laranjal", "Volta Redonda", "Rio de Janeiro", "27255020", client2);
		Address address3 = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client3);
		
		client1.setAddress(address1);
		client2.setAddress(address2);
		client3.setAddress(address3);
		
		clientRepository.saveAll(Arrays.asList(client1, client2, client3));
		
	}

}
