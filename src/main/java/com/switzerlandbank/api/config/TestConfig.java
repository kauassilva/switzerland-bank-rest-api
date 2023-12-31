package com.switzerlandbank.api.config;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Balance;
import com.switzerlandbank.api.entities.Customer;
import com.switzerlandbank.api.entities.PixKey;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.entities.enums.KeyType;
import com.switzerlandbank.api.repositories.AccountRepository;
import com.switzerlandbank.api.repositories.CustomerRepository;
import com.switzerlandbank.api.repositories.PixKeyRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private PixKeyRepository pixKeyRepository;

	@Override
	public void run(String... args) throws Exception {
		
		
		Customer customer1 = new Customer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Customer customer2 = new Customer(null, "Carlos Pereira", "11122233344", "Teresa Pereira", LocalDate.parse("1975-10-10"), Gender.OTHER, "carlospereira@gmail.com", "CarlosPereira123");
		Customer customer3 = new Customer(null, "Ana Santos", "98765432100", "Beatriz Santos", LocalDate.parse("1990-02-20"), Gender.FEMALE, "anasantos@example.com", "AnaSantos123");

		Address address1 = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", customer1);
		Address address2 = new Address(null, "R. Cento e Cinquenta e Dois", "196", "Laranjal", "Volta Redonda", "Rio de Janeiro", "27255020", customer2);
		Address address3 = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", customer3);

		customer1.setAddress(address1);
		customer2.setAddress(address2);
		customer3.setAddress(address3);
		
		Account account1 = new Account(null, "123456", customer1);
		Account account2 = new Account(null, "789012", customer2);
		Account account3 = new Account(null, "345678", customer3);
		
		customer1.setAccount(account1);
		customer2.setAccount(account2);
		customer3.setAccount(account3);

		customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));
		
		Balance balance1 = new Balance(null, new BigDecimal("10.00"), Instant.now(), account1);
		Balance balance2 = new Balance(null, new BigDecimal("10.00"), Instant.now(), account2);
		Balance balance3 = new Balance(null, new BigDecimal("10.00"), Instant.now(), account3);
		
		account1.setBalance(balance1);
		account2.setBalance(balance2);
		account3.setBalance(balance3);
		
		accountRepository.saveAll(Arrays.asList(account1, account2, account3));
		
		PixKey pixKey1 = new PixKey(null, "12345678910", KeyType.CPF, account1);
		PixKey pixKey2 = new PixKey(null, "joaosilva@example.com", KeyType.EMAIL, account1);
		PixKey pixKey3 = new PixKey(null, "aB3dEfgH45iJkL6mN7oPqR8sTuvWxYz0", KeyType.RANDOM, account1);
		
		PixKey pixKey4 = new PixKey(null, "11122233344", KeyType.CPF, account2);
		PixKey pixKey5 = new PixKey(null, "Zx9Yv8Uw7TlKj6Hg5FeD4CvBn2Mq1OpL", KeyType.RANDOM, account2);
		
		PixKey pixKey6 = new PixKey(null, "anasantos@example.com", KeyType.EMAIL, account3);
		
		pixKeyRepository.saveAll(Arrays.asList(pixKey1, pixKey2, pixKey3, pixKey4, pixKey5, pixKey6));
		
	}

}
