package com.switzerlandbank.api.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.switzerlandbank.api.entities.Balance;
import com.switzerlandbank.api.services.BalanceService;

@RestController
@RequestMapping(value = "/api/balances")
public class BalanceResource {

	@Autowired
	private BalanceService service;
	
	@GetMapping
	public ResponseEntity<List<Balance>> findAll() {
		List<Balance> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Balance> findById(@PathVariable Long id) {
		Balance obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
}
