package com.switzerlandbank.api.services;

import java.util.List;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.Costumer;

public interface AccountService {
	
	List<Account> findAll();
	Account findById(Long id);
	Account insert(Costumer savedClient);

}
