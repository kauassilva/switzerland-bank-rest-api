package com.switzerlandbank.api.resources.units;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
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
import com.switzerlandbank.api.entities.Costumer;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.resources.AddressResource;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;
import com.switzerlandbank.api.services.impls.AddressServiceImpl;

@WebMvcTest(controllers = AddressResource.class)
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
class AddressResourceUnitTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
	private AddressServiceImpl service;

    private Address address;

    @Autowired
	private JacksonTester<Address> jsonAddress;
	
	@Autowired
	private JacksonTester<List<Address>> jsonAddressList;
	
	@BeforeEach
	void setUp() {
		Costumer costumer = new Costumer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
		address = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", costumer);
		costumer.setAddress(address);
	}

    @Test
    void testFindAll_ReturnsStatusOk() throws Exception {

       when(service.findAll()).thenReturn(Lists.newArrayList(address));
    
        MockHttpServletResponse response = mockMvc.perform(get("/api/addresses")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
	void testFindAll_ReturnsContentSuccessfully() throws Exception {
        
        when(service.findAll()).thenReturn(Lists.newArrayList(address));

        MockHttpServletResponse response = mockMvc.perform(get("/api/addresses")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");

        assertThat(response.getContentAsString()).isEqualTo(jsonAddressList.write(Lists.newArrayList(address)).getJson());
    }

    @Test
	void testFindAll_ReturnsEmptyList() throws Exception {
        
        when(service.findAll()).thenReturn(Collections.emptyList());
		
        MockHttpServletResponse response = mockMvc.perform(get("/api/addresses")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");

        assertThat(response.getContentAsString()).isEqualTo(jsonAddressList.write(Collections.emptyList()).getJson());
    }

    @Test
	void testFindByID_ReturnsStatusOk() throws Exception {
        when(service.findById(1L)).thenReturn(address);

        MockHttpServletResponse response = mockMvc.perform(get("/api/addresses/1")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
	void testFindById_ReturnsStatusNotFound() throws Exception {
		when(service.findById(2L)).thenThrow(ResourceNotFoundException.class);
		
		MockHttpServletResponse response = mockMvc.perform(get("/api/addresses/2")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}

    @Test
	void testFindById_ReturnsCorrectContent() throws Exception {
        when(service.findById(1L)).thenReturn(address);

        MockHttpServletResponse response = mockMvc.perform(get("/api/addresses/1")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		response.setCharacterEncoding("UTF-8");
		
		assertThat(response.getContentAsString()).isEqualTo(jsonAddress.write(address).getJson());
    }
}
