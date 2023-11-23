package com.switzerlandbank.api.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.switzerlandbank.api.entities.PixKey;
import com.switzerlandbank.api.services.PixKeyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/pixkeys")
public class PixKeyResource {
	
	@Autowired
	private PixKeyService service;
	
	@GetMapping
	public ResponseEntity<List<PixKey>> findAll() {
		List<PixKey> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/account/{accountId}")
	public ResponseEntity<List<PixKey>> findByAccountId(@PathVariable Long accountId) {
		List<PixKey> list = service.findByAccountId(accountId);
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<PixKey> findById(@PathVariable Long id) {
		PixKey obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@PostMapping
	public ResponseEntity<PixKey> insert(@RequestBody @Valid PixKey obj) {
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
