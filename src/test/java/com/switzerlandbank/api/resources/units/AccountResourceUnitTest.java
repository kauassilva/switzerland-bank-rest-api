package com.switzerlandbank.api.resources.units;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Customer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.resources.AccountResource;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;
import com.switzerlandbank.api.services.impls.AccountServiceImpl;

@WebMvcTest(controllers = AccountResource.class) //indicar que o teste esta focado nos controllers
@AutoConfigureJsonTesters //auto configura o json tests
@ExtendWith(MockitoExtension.class) //usa a extenção mockito para junit5.
class AccountResourceUnitTest {

	@Autowired //invejta
	private MockMvc mockMvc;

	@MockBean
	private AccountServiceImpl service;
	
	@Autowired
	private JacksonTester<Account> jsonAccount;
	
	@Autowired
	private JacksonTester<List<Account>> jsonAccountList;
	
	private Account account;
	
	@BeforeEach  //simula
	void setUp() {
		Customer customer = new Customer(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", customer);
		customer.setAddress(address);
		account = new Account(1L, "123456", customer);
		customer.setAccount(account);
	}
	// Verifica a chamada para /api/accounts retorna um status HTTP 200 (OK)
	// e se o corpo da resposta contém o JSON esperado.
	@Test
	void testFindAll_ReturnsStatusOk() throws Exception {
		when(service.findAll()).thenReturn(Lists.newArrayList(account));
		
		MockHttpServletResponse response = mockMvc.perform(get("/api/accounts")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	// verifica se o corpo contém a lista de contas no formato JSON.
	@Test
	void testFindAll_ReturnsCorrectBody() throws Exception {
		when(service.findAll()).thenReturn(Lists.newArrayList(account));
		
		MockHttpServletResponse response = mockMvc.perform(get("/api/accounts")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertEquals(jsonAccountList.write(Lists.newArrayList(account)).getJson(), response.getContentAsString());
	}

	//Testa retorno de lista de contas é vazia.

	@Test
	void testFindAll_ReturnsEmptyBody() throws Exception {
		when(service.findAll()).thenReturn(Collections.emptyList());
		
		MockHttpServletResponse response = mockMvc.perform(get("/api/accounts")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertEquals(jsonAccountList.write(Collections.emptyList()).getJson(), response.getContentAsString());
	}
	
	// Testa se a chamada para /api/accounts/1 retorna um status

	@Test
	void testFindById_ReturnsStatusOk() throws Exception {
		when(service.findById(anyLong())).thenReturn(account);
		
		MockHttpServletResponse response = mockMvc.perform(get("/api/accounts/1")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	// Testa se retorna um status HTTP 404 (Not Found) quando a conta não é encontrada.
	@Test
	void testFindById_ReturnsStatusNotFound() throws Exception {
		when(service.findById(1L)).thenThrow(ResourceNotFoundException.class);
		
		MockHttpServletResponse response = mockMvc.perform(get("/api/accounts/1")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
	}

	//Testa se o corpo da resposta contém o JSON da conta correta quando a conta é encontrada.
	@Test
	void testFindById_ReturnsCorrectContent() throws Exception {
		when(service.findById(1L)).thenReturn(account);
		
		MockHttpServletResponse response = mockMvc.perform(get("/api/accounts/1")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertEquals(jsonAccount.write(account).getJson(), response.getContentAsString());
	}

}
