package com.switzerlandbank.api.resources.units;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.assertj.core.util.Lists;
import org.hibernate.mapping.Collection;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.switzerlandbank.api.entities.Balance;
import com.switzerlandbank.api.resources.BalanceResource;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;
import com.switzerlandbank.api.services.impls.BalanceServiceImpl;

@WebMvcTest(controllers = BalanceResource.class)
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
class BalanceResourceUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BalanceServiceImpl balanceService;

    @Autowired
    private JacksonTester<Balance> jsonBalance;

    @Autowired
    private JacksonTester<List<Balance>> jsonBalanceList;

    private Balance balance;

    @BeforeEach
    void setUp() {
        balance = new Balance(1L, new BigDecimal(3), Instant.now(), null);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll_ReturnsStatusOk() throws Exception {
        when(balanceService.findAll()).thenReturn(Lists.newArrayList(balance));
        MockHttpServletResponse response = mockMvc.perform(get("/api/balances").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void testFindAll_ReturnsContentSuccessfully() throws Exception {
         when(balanceService.findAll()).thenReturn(Lists.newArrayList(balance));
         MockHttpServletResponse response = mockMvc.perform(get("/api/balances").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
         response.setCharacterEncoding("UTF-8");
         assertThat(response.getContentAsString()).isEqualTo(jsonBalanceList.write(Lists.newArrayList(balance)).getJson());
    }

    @Test
    void testFindAll_ReturnsEmptyList() throws Exception {
        when(balanceService.findAll()).thenReturn(Collections.emptyList());
        MockHttpServletResponse response = mockMvc.perform(get("/api/balances").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        response.setCharacterEncoding("UTF-8");
        assertThat(response.getContentAsString()).isEqualTo(jsonBalanceList.write(Collections.emptyList()).getJson());
    }

    @Test
    void testFindByID_ReturnsStatusOk() throws Exception{
         when(balanceService.findById(1L)).thenReturn(balance);
         MockHttpServletResponse response = mockMvc.perform(get("/api/balances/1").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
         assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void testFindById_ReturnsCorrectContent() throws Exception{
        when(balanceService.findById(1L)).thenReturn(balance);
        MockHttpServletResponse response = mockMvc.perform(get("/api/balances/1").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertThat(response.getContentAsString()).isEqualTo(jsonBalance.write(balance).getJson());
    }

    @Test
    void testFindById_ReturnsStatusNotFound() throws Exception{
        when(balanceService.findById(2L)).thenThrow(ResourceNotFoundException.class);
        MockHttpServletResponse response = mockMvc.perform(get("/api/balances/2").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }






    
    
}
