package com.switzerlandbank.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.switzerlandbank.api.entities.Client;
import com.switzerlandbank.api.repositories.ClientRepository;
import com.switzerlandbank.api.services.exceptions.DatabaseException;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository repository;
	
	public List<Client> findAll() {
		return repository.findAll();
	}
	
	public Client findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	public Client insert(Client obj) {
		return repository.save(obj);
	}
	
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException(id);		
		}
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	public Client update(Client obj, Long id) {
		try {
			Client entity = repository.getReferenceById(id);
			updateData(entity, obj);
			return repository.save(entity);			
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	
	private void updateData(Client entity, Client obj) {
		entity.setName(obj.getName());
		entity.setCpf(obj.getCpf());
		entity.setMotherName(obj.getMotherName());
		entity.setDateBirth(obj.getDateBirth());
		entity.setGender(obj.getGender());
		entity.setEmail(obj.getEmail());
		entity.setPassword(obj.getPassword());
	}

}
