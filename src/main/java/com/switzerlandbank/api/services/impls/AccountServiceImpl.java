package com.switzerlandbank.api.services.impls;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.Costumer;
import com.switzerlandbank.api.repositories.AccountRepository;
import com.switzerlandbank.api.services.AccountService;
import com.switzerlandbank.api.services.BalanceService;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private BalanceService balanceService;

	@Override
	public List<Account> findAll() {
		return accountRepository.findAll();
	}

	@Override
	public Account findById(Long id) {
		Optional<Account> obj = accountRepository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	@Transactional
	@Override
	public Account insert(Costumer savedClient) {
		Account account = new Account(null, generateAccountNumber(), savedClient);
		account = accountRepository.save(account);
	
		balanceService.insert(account);
		return account;
	}
	
	public String generateAccountNumber() {
		Random random = new Random();
		int n = random.nextInt(900000) + 100000;
		return String.valueOf(n);
	}

}
