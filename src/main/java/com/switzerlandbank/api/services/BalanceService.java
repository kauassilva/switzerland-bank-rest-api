package com.switzerlandbank.api.services;

import java.util.List;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.Balance;

public interface BalanceService {

	List<Balance> findAll();
	Balance findById(Long id);
	void insert(Account savedAccount);
	
}
