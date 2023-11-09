package com.switzerlandbank.api.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.services.AddressServiceImpl;

@RestController
@RequestMapping(value = "/addresses")
public class AddressResource {
	
	@Autowired
	private AddressServiceImpl service;
	
	@GetMapping
	public ResponseEntity<List<Address>> findAll() {
		List<Address> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Address> findById(@PathVariable Long id) {
		Address obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Address> update(@RequestBody Address obj, @PathVariable Long id) {
		obj = service.update(obj, id);
		return ResponseEntity.ok().body(obj);
	}

}
