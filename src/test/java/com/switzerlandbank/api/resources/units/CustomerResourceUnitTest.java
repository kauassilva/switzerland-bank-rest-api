package com.switzerlandbank.api.resources.units;

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

import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Customer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.resources.CustomerResource;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;
import com.switzerlandbank.api.services.impls.CustomerServiceImpl;

@WebMvcTest(controllers = CustomerResource.class)
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
class CustomerResourceUnitTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomerServiceImpl service;
	
	@Autowired
	private JacksonTester<Customer> jsonCostumer;
	
	@Autowired
	private JacksonTester<List<Customer>> jsonCostumerList;
	
	private Customer customer;
	
	@BeforeEach
	void setUp() {
		customer = new Customer(1L, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", customer);
		customer.setAddress(address);
	}

	@Test
	void testFindAll_ReturnsStatusOk() throws Exception {
		when(service.findAll()).thenReturn(Lists.newArrayList(customer));
		
		MockHttpServletResponse response = mockMvc.perform(get("/api/customers")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	void testFindAll_ReturnsContentSuccessfully() throws Exception {
		when(service.findAll()).thenReturn(Lists.newArrayList(customer));
		
		MockHttpServletResponse response = mockMvc.perform(get("/api/customers")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getContentAsString()).isEqualTo(jsonCostumerList.write(Lists.newArrayList(customer)).getJson());
	}
	
	@Test
	void testFindAll_ReturnsEmptyList() throws Exception {
		when(service.findAll()).thenReturn(Collections.emptyList());
		
		MockHttpServletResponse response = mockMvc.perform(get("/api/customers")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getContentAsString()).isEqualTo(jsonCostumerList.write(Collections.emptyList()).getJson());
	}
	
	@Test
	void testFindByID_ReturnsStatusOk() throws Exception {
		when(service.findById(1L)).thenReturn(customer);
		
		MockHttpServletResponse response = mockMvc.perform(get("/api/customers/1")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	void testFindById_ReturnsStatusNotFound() throws Exception {
		when(service.findById(2L)).thenThrow(ResourceNotFoundException.class);
		
		MockHttpServletResponse response = mockMvc.perform(get("/api/customers/2")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	void testFindById_ReturnsCorrectContent() throws Exception {
		when(service.findById(1L)).thenReturn(customer);
		
		MockHttpServletResponse response = mockMvc.perform(get("/api/customers/1")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getContentAsString()).isEqualTo(jsonCostumer.write(customer).getJson());
	}
	
	@Test
	void testInsert_ReturnsStatusCreated() throws Exception {
		when(service.insert(customer)).thenReturn(customer);
		
		MockHttpServletResponse response = mockMvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonCostumer.write(customer).getJson()))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
	}
	
	@Test
	void testInsert_ReturnsStatusBadRequest() throws Exception {
		Customer wrongCostumer = new Customer(1L, null, "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		Address address = new Address(1L, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", wrongCostumer);
		wrongCostumer.setAddress(address);
		
		when(service.insert(wrongCostumer)).thenReturn(wrongCostumer);
		
		MockHttpServletResponse response = mockMvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonCostumer.write(wrongCostumer).getJson()))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
	
	@Test
	void testInsert_ReturnsCorrectContent() throws Exception {
		when(service.insert(customer)).thenReturn(customer);
		
		MockHttpServletResponse response = mockMvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonCostumer.write(customer).getJson()))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getContentAsString()).isEqualTo(jsonCostumer.write(customer).getJson());
	}
	
	@Test
	void testDelete_ReturnsStatusNoContent() throws Exception {
		doNothing().when(service).delete(1L);
		
		MockHttpServletResponse response = mockMvc.perform(delete("/api/customers/1"))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
	
	@Test
	void testDelete_ReturnsStatusNotFound() throws Exception {
		doThrow(new ResourceNotFoundException(1L)).when(service).delete(1L);
		
		MockHttpServletResponse response = mockMvc.perform(delete("/api/customers/1"))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	void testUpdate_ReturnsStatusOk() throws Exception {
		when(service.update(customer, 1L)).thenReturn(customer);
		
		MockHttpServletResponse response = mockMvc.perform(put("/api/customers/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonCostumer.write(customer).getJson()))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	void testUpdate_ReturnsStatusNotFound() throws Exception {
		doThrow(new ResourceNotFoundException(2L)).when(service).update(customer, 2L);
		
		MockHttpServletResponse response = mockMvc.perform(put("/api/customers/2")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonCostumer.write(customer).getJson()))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	void testUpdate_ReturnsCorrectContent() throws Exception {
		when(service.update(customer, 1L)).thenReturn(customer);
		
		MockHttpServletResponse response = mockMvc.perform(put("/api/customers/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonCostumer.write(customer).getJson()))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getContentAsString()).isEqualTo(jsonCostumer.write(customer).getJson());
	}

}
