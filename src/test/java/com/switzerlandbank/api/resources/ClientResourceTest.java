package com.switzerlandbank.api.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.util.Lists;
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

import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Client;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.services.ClientServiceImpl;

@WebMvcTest(controllers = ClientResource.class)
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
class ClientResourceTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ClientServiceImpl service;
	
	@Autowired
	private JacksonTester<Client> jsonClient;
	
	@Autowired
	private JacksonTester<List<Client>> jsonClientList;

	@Test
	void testFindAll_ReturnsStatusOk() throws Exception {
		Client client = new Client(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client);
		client.setAddress(address);
		
		when(service.findAll()).thenReturn(Lists.newArrayList(client));
		
		MockHttpServletResponse response = mockMvc.perform(get("/clients")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	void testFindAll_ReturnsContentSuccessffuly() throws Exception {
		Client client = new Client(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client);
		client.setAddress(address);
		
		when(service.findAll()).thenReturn(Lists.newArrayList(client));
		
		MockHttpServletResponse response = mockMvc.perform(get("/clients")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getContentAsString()).isEqualTo(jsonClientList.write(Lists.newArrayList(client)).getJson());
	}

}
