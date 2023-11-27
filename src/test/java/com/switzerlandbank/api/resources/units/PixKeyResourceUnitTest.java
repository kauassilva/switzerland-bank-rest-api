package com.switzerlandbank.api.resources.units;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
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
import com.switzerlandbank.api.entities.PixKey;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.entities.enums.KeyType;
import com.switzerlandbank.api.resources.PixKeyResource;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;
import com.switzerlandbank.api.services.impls.PixKeyServiceImpl;

@WebMvcTest(controllers = PixKeyResource.class)
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
public class PixKeyResourceUnitTest {
    
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PixKeyServiceImpl service;
	
	@Autowired
	private JacksonTester<PixKey> jsonPixKey;
	
	@Autowired
	private JacksonTester<List<PixKey>> jsonPixKeyList;
	
    private Account account;
	private PixKey pixKey;
    private PixKey pixKeyEmpty;
    private PixKey pixKeyCPF;
    private PixKey pixKeyEmail;
    private PixKey pixKeyRandom;

    @BeforeEach
	void setUp() {


		Customer customer1 = new Customer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
        Address address1 = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", customer1);
        customer1.setAddress(address1);
        account = new Account(1L, "123456", customer1);
		customer1.setAccount(account);

		pixKey = new PixKey(1L, null, KeyType.CPF, account);
		pixKeyEmpty = new PixKey(2L, null, KeyType.CPF, account);
		pixKeyCPF = new PixKey(3L, null, KeyType.CPF, account);
		pixKeyEmail = new PixKey(4L, null, KeyType.EMAIL, account);
		pixKeyRandom = new PixKey(5L, null, KeyType.RANDOM, account);
		MockitoAnnotations.openMocks(this);

	}
    
    @Test
    void testFindAll_ReturnsStatusOk() throws Exception {

        when(service.findAll()).thenReturn(Lists.newArrayList(pixKey));
    
        MockHttpServletResponse response = mockMvc.perform(get("/api/pixkeys")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

	@Test
	void testFindAll_ReturnsCorrectContent() throws Exception {
        
        when(service.findAll()).thenReturn(Lists.newArrayList(pixKey));

        MockHttpServletResponse response = mockMvc.perform(get("/api/pixkeys")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");

        assertThat(response.getContentAsString()).isEqualTo(jsonPixKeyList.write(Lists.newArrayList(pixKey)).getJson());
    }

	@Test
	void testFindAll_ReturnEmptyContent() throws Exception {
		when(service.findAll()).thenReturn(Collections.emptyList());
		
        MockHttpServletResponse response = mockMvc.perform(get("/api/pixkeys")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");

        assertThat(response.getContentAsString()).isEqualTo(jsonPixKeyList.write(Collections.emptyList()).getJson());
	}

	@Test
	void testFindByAccountId_ReturnsStatusOk() throws Exception {

		List<PixKey> expectedPixKeys = Arrays.asList(pixKey, pixKeyEmpty);

		when(service.findByAccountId(1L)).thenReturn(expectedPixKeys);
		
        MockHttpServletResponse response = mockMvc.perform(get("/api/pixkeys/account/1")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getContentAsString()).isEqualTo(jsonPixKeyList.write(expectedPixKeys).getJson());
	}

	@Test
	void testFindByAccountId_ReturnsCorrectContent() throws Exception {

		List<PixKey> expectedPixKeys = Arrays.asList(pixKey, pixKeyEmpty);

        when(service.findByAccountId(1L)).thenReturn(expectedPixKeys);

        MockHttpServletResponse response = mockMvc.perform(get("/api/pixkeys/account/1")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getContentAsString()).isEqualTo(jsonPixKeyList.write(expectedPixKeys).getJson());
    }

	@Test
	void testFindByAccountId_ReturnsEmptyList() throws Exception {
		when(service.findByAccountId(1L)).thenReturn(Collections.emptyList());
		
        MockHttpServletResponse response = mockMvc.perform(get("/api/pixkeys/account/1")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");

        assertThat(response.getContentAsString()).isEqualTo(jsonPixKeyList.write(Collections.emptyList()).getJson());
	}

	@Test
	void testFindById_ReturnsStatusOk() throws Exception {
		when(service.findById(anyLong())).thenReturn(pixKey);
		
		MockHttpServletResponse response = mockMvc.perform(get("/api/pixkeys/1")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	void testFindById_ReturnsStatusNotFound() throws Exception {
		when(service.findById(1L)).thenThrow(ResourceNotFoundException.class);
		
		MockHttpServletResponse response = mockMvc.perform(get("/api/pixkeys/1")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
	}
	
	@Test
	void testFindById_ReturnsCorrectContent() throws Exception {
        when(service.findById(1L)).thenReturn(pixKey);

        MockHttpServletResponse response = mockMvc.perform(get("/api/pixkeys/1")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getContentAsString()).isEqualTo(jsonPixKey.write(pixKey).getJson());
    }

	@Test
	void testInsert_ReturnsStatusCreated() throws Exception {
		when(service.insert(pixKey)).thenReturn(pixKey);
		
		MockHttpServletResponse response = mockMvc.perform(post("/api/pixkeys")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonPixKey.write(pixKey).getJson()))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
	}

	@Test
	void testInsert_ReturnsStatusBadRequest() throws Exception {
		PixKey wrongPixKey= new PixKey(null, null, KeyType.CPF, null);
		
		when(service.insert(wrongPixKey)).thenReturn(wrongPixKey);
		
		MockHttpServletResponse response = mockMvc.perform(post("/api/pixkeys")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonPixKey.write(wrongPixKey).getJson()))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void testInsert_ReturnsCorrectContentPixKeyTypeCPF() throws Exception {
		when(service.insert(pixKeyCPF)).thenReturn(pixKeyCPF);
		
		MockHttpServletResponse response = mockMvc.perform(post("/api/pixkeys")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonPixKey.write(pixKeyCPF).getJson()))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getContentAsString()).isEqualTo(jsonPixKey.write(pixKeyCPF).getJson());
	}

	@Test
	void testInsert_ReturnsCorrectContentPixKeyTypeEmail() throws Exception {
		when(service.insert(pixKeyEmail)).thenReturn(pixKeyEmail);
		
		MockHttpServletResponse response = mockMvc.perform(post("/api/pixkeys")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonPixKey.write(pixKeyEmail).getJson()))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getContentAsString()).isEqualTo(jsonPixKey.write(pixKeyEmail).getJson());
	}

	@Test
	void testInsert_ReturnsCorrectContentPixKeyTypeRandom() throws Exception {
		when(service.insert(pixKeyRandom)).thenReturn(pixKeyRandom);
		
		MockHttpServletResponse response = mockMvc.perform(post("/api/pixkeys")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonPixKey.write(pixKeyRandom).getJson()))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getContentAsString()).isEqualTo(jsonPixKey.write(pixKeyRandom).getJson());
	}
	
	@Test
	void testDelete_ReturnsStatusNoContent() throws Exception {
		doNothing().when(service).delete(1L);
		
		MockHttpServletResponse response = mockMvc.perform(delete("/api/pixkeys/1"))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	@Test
	void testDelete_ReturnsStatusNotFound() throws Exception {
		doThrow(new ResourceNotFoundException(1L)).when(service).delete(1L);
		
		MockHttpServletResponse response = mockMvc.perform(delete("/api/pixkeys/1"))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}
}
