package com.switzerlandbank.api.services.impls;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.Balance;
import com.switzerlandbank.api.repositories.BalanceRepository;
import com.switzerlandbank.api.services.BalanceService;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;

@Service
public class BalanceServiceImpl implements BalanceService {

	@Autowired
	private BalanceRepository repository;
	
	@Override
	public List<Balance> findAll() {
		return repository.findAll();
	}

	@Override
	public Balance findById(Long id) {
		Optional<Balance> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	@Override
	public void insert(Account savedAccount) {
		Balance balance = new Balance(null, new BigDecimal(10), Instant.now(), savedAccount);
		repository.save(balance);
	}
	
}
