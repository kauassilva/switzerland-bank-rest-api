package com.switzerlandbank.api.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.services.AccountService;

@RestController
@RequestMapping(value = "/api/accounts")
public class AccountResource {
	
	@Autowired
	private AccountService service;
	
	@GetMapping
	public ResponseEntity<List<Account>> findAll() {
		List<Account> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Account> findById(@PathVariable Long id) {
		Account obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}

}
