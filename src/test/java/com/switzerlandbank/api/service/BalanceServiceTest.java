package com.switzerlandbank.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hibernate.mapping.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.switzerlandbank.api.entities.Balance;
import com.switzerlandbank.api.repositories.BalanceRepository;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;
import com.switzerlandbank.api.services.impls.BalanceServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BalanceServiceTest {

    @Mock
    private BalanceRepository repository;

    @InjectMocks
    @Autowired
    private BalanceServiceImpl service;

    private Balance balance;

    @BeforeEach
    void setUp() {
        balance = new Balance(1L, new BigDecimal(3), Instant.now(), null);
        MockitoAnnotations.openMocks(this);

       
    }

    @Test
    void testFindAllBalance() {
        List<Balance> balanceList = new ArrayList<>();
        balanceList.add(balance);

        when(repository.findAll()).thenReturn(balanceList);
        List<Balance> result = service.findAll();
        assertEquals(balanceList, result);
    }

    @Test
    void testFindAllBalanceButBalanceIsEmpty(){
        when(repository.findAll()).thenReturn(Collections.emptyList());
        List<Balance> result = service.findAll();
        assertTrue(result.isEmpty());
        

    }

    @Test
    void testFindById(){
        when(repository.findById(1L)).thenReturn(Optional.of(balance));
        Balance result = service.findById(1L);
        assertEquals(balance, result);

    }

    @Test
    void testFindByIdButTheresNoId(){
         Balance balanco = new Balance(1L, new BigDecimal(3), Instant.now(), null);

        when(repository.findById(1L)).thenReturn(Optional.of(balanco));
        assertThrows( ResourceNotFoundException.class, () -> 
            service.findById(6L));
    
        //assertThat(e.getMessage().contains("Não a id"));

    }
}
