package com.switzerlandbank.api.services.impls;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.repositories.AccountRepository;
import com.switzerlandbank.api.services.AccountService;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private AccountRepository repository;

	@Override
	public List<Account> findAll() {
		return repository.findAll();
	}

	@Override
	public Account findById(Long id) {
		Optional<Account> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}

}
