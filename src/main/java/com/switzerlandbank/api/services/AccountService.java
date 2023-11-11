package com.switzerlandbank.api.services;

import java.util.List;

import com.switzerlandbank.api.entities.Account;

public interface AccountService {
	
	List<Account> findAll();
	Account findById(Long id);

}
