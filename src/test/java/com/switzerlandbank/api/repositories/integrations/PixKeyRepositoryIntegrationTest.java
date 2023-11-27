package com.switzerlandbank.api.repositories.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.entities.Customer;
import com.switzerlandbank.api.entities.PixKey;
import com.switzerlandbank.api.entities.enums.Gender;
import com.switzerlandbank.api.entities.enums.KeyType;
import com.switzerlandbank.api.repositories.AccountRepository;
import com.switzerlandbank.api.repositories.PixKeyRepository;

import jakarta.validation.ConstraintViolationException;

@DataJpaTest
public class PixKeyRepositoryIntegrationTest {

    @Autowired
    private PixKeyRepository pixKeyRepository;

    @Autowired
    private AccountRepository accountRepository;

    private PixKey pixKeyEmpty;
    private Account account;

    @BeforeEach
    void setUp(){
        Customer customer1 = new Customer(null, "João Silva", "12345678910", "Maria Silva", LocalDate.parse("1980-07-15"), Gender.MALE, "joaosilva@example.com", "JoaoSilva123");
        Address address1 = new Address(null, "Av. Castelo Branco", "1416", "Centro", "Paraíso do Tocantins", "Tocantins", "77600000", customer1);
        customer1.setAddress(address1);
        account = new Account(null, "123456", customer1);
        customer1.setAccount(account);
        pixKeyEmpty = new PixKey(null, "1", KeyType.CPF, account);
    }

    @Test
    void findByAccountId(){
        accountRepository.save(account);
        PixKey savedPixKeys = pixKeyRepository.save(pixKeyEmpty);
        List<PixKey> findAccount = pixKeyRepository.findByAccountId(savedPixKeys.getId());
        assertTrue(!findAccount.isEmpty());
    }

    @Test
    void finddById(){
        accountRepository.save(account);
         PixKey savedPixKeys = pixKeyRepository.save(pixKeyEmpty);
         Optional<PixKey>  foundPixKey = pixKeyRepository.findById(savedPixKeys.getId());
         assertTrue(foundPixKey.isPresent());
    }

    @Test
    void finddByIdButIsNotFound(){
        accountRepository.save(account);
        PixKey savedPixKey = pixKeyRepository.save(pixKeyEmpty);
        Optional<PixKey> foundPixKey = pixKeyRepository.findById(savedPixKey.getId() +1L);
        assertTrue(foundPixKey.isEmpty());
    }

    @Test
    void testSave(){
        accountRepository.save(account);
        PixKey savedPixKey = pixKeyRepository.save(pixKeyEmpty);
        assertEquals(pixKeyEmpty, savedPixKey);
    }

    @Test
    void testSaveButAccountIsNull(){
         pixKeyEmpty = new PixKey(null, "1", KeyType.CPF, null);
         assertThrows(ConstraintViolationException.class, () -> pixKeyRepository.save(pixKeyEmpty));
    }

    @Test
    void testDeleteById(){
        accountRepository.save(account);
        PixKey savedPixKey = pixKeyRepository.save(pixKeyEmpty);
        pixKeyRepository.deleteById(savedPixKey.getId());
        Optional<PixKey> foundPixKey = pixKeyRepository.findById(savedPixKey.getId());
        assertTrue(foundPixKey.isEmpty());
    }
}
