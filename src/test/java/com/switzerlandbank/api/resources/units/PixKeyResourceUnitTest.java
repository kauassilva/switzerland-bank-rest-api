package com.switzerlandbank.api.resources.units;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Costumer;
import com.switzerlandbank.api.entities.PixKey;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.entities.enums.KeyType;
import com.switzerlandbank.api.services.impls.AddressServiceImpl;
import com.switzerlandbank.api.services.impls.PixKeyServiceImpl;

public class PixKeyResourceUnitTest {
    
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PixKeyServiceImpl service;
	
	private PixKey pixKey;
	
	private JacksonTester<PixKey> jsonPixKey;
	
	private JacksonTester<List<Address>> jsonPixKeyList;

    private Account account;
    private PixKey pixKeyEmpty;
    private PixKey pixKeyCPF;
    private PixKey pixKeyEmail;
    private PixKey pixKeyRandom;

    @BeforeEach
	void setUp() {


		Costumer costumer1 = new Costumer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
        Address address1 = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", costumer1);
        costumer1.setAddress(address1);
        account = new Account(1L, "123456", costumer1);
		costumer1.setAccount(account);

		pixKeyEmpty = new PixKey(1L, "1", KeyType.CPF, account);
		pixKeyCPF = new PixKey(1L, null, KeyType.CPF, account);
		pixKeyEmail = new PixKey(1L, null, KeyType.EMAIL, account);
		pixKeyRandom = new PixKey(1L, null, KeyType.RANDOM, account);
		MockitoAnnotations.openMocks(this);

	}
    
    @Test
    void testFindAll_ReturnsStatusOk() {

        

    }


}
