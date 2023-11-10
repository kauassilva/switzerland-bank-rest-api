package com.switzerlandbank.api.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.time.LocalDate;
import java.util.Collections;
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
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;

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
	void testFindAll_ReturnsContentSuccessfully() throws Exception {
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
	
	@Test
	void testFindAll_ReturnsEmptyList() throws Exception {
		when(service.findAll()).thenReturn(Collections.emptyList());
		
		MockHttpServletResponse response = mockMvc.perform(get("/clients")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getContentAsString()).isEqualTo(jsonClientList.write(Collections.emptyList()).getJson());
	}
	
	@Test
	void testFindByID_ReturnsStatusOk() throws Exception {
		Client client = new Client(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client);
		client.setAddress(address);
		
		when(service.findById(1L)).thenReturn(client);
		
		MockHttpServletResponse response = mockMvc.perform(get("/clients/1")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	void testFindById_ReturnsStatusNotFound() throws Exception {
		when(service.findById(2L)).thenThrow(ResourceNotFoundException.class);
		
		MockHttpServletResponse response = mockMvc.perform(get("/clients/2")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	void testFindById_ReturnsCorrectContent() throws Exception {
		Client client = new Client(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client);
		client.setAddress(address);
		
		when(service.findById(1L)).thenReturn(client);
		
		MockHttpServletResponse response = mockMvc.perform(get("/clients/1")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getContentAsString()).isEqualTo(jsonClient.write(client).getJson());
	}
	
	@Test
	void testInsert_ReturnsStatusCreated() throws Exception {
		Client client = new Client(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client);
		client.setAddress(address);
		
		when(service.insert(client)).thenReturn(client);
		
		MockHttpServletResponse response = mockMvc.perform(post("/clients")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonClient.write(client).getJson()))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
	}
	
	@Test
	void testInsert_ReturnsStatusBadRequest() throws Exception {
		Client client = new Client(1L, null, "12345678910", null, LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com",null);
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client);
		client.setAddress(address);
		
		when(service.insert(client)).thenReturn(client);
		
		MockHttpServletResponse response = mockMvc.perform(post("/clients")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonClient.write(client).getJson()))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
	
	@Test
	void testInsert_ReturnsCorrectContent() throws Exception {
		Client client = new Client(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client);
		client.setAddress(address);
		
		when(service.insert(client)).thenReturn(client);
		
		MockHttpServletResponse response = mockMvc.perform(post("/clients")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonClient.write(client).getJson()))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getContentAsString()).isEqualTo(jsonClient.write(client).getJson());
	}
	
	@Test
	void testDelete_ReturnsStatusNoContent() throws Exception {
		doNothing().when(service).delete(1L);
		
		MockHttpServletResponse response = mockMvc.perform(delete("/clients/1"))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
	
	@Test
	void testDelete_ReturnsStatusNotFound() throws Exception {
		doThrow(new ResourceNotFoundException(1L)).when(service).delete(1L);
		
		MockHttpServletResponse response = mockMvc.perform(delete("/clients/1"))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	void testUpdate_ReturnsStatusOk() throws Exception {
		Client client = new Client(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client);
		client.setAddress(address);
		
		when(service.update(client, 1L)).thenReturn(client);
		
		MockHttpServletResponse response = mockMvc.perform(put("/clients/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonClient.write(client).getJson()))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	void testUpdate_ReturnsStatusNotFound() throws Exception {
		Client client = new Client(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client);
		client.setAddress(address);
		
		doThrow(new ResourceNotFoundException(2L)).when(service).update(client, 2L);
		
		MockHttpServletResponse response = mockMvc.perform(put("/clients/2")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonClient.write(client).getJson()))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	void testUpdate_ReturnsCorrectContent() throws Exception {
		Client client = new Client(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", client);
		client.setAddress(address);
		
		when(service.update(client, 1L)).thenReturn(client);
		
		MockHttpServletResponse response = mockMvc.perform(put("/clients/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonClient.write(client).getJson()))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getContentAsString()).isEqualTo(jsonClient.write(client).getJson());
	}
	
}
