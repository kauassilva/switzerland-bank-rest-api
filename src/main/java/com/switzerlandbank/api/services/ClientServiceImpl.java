package com.switzerlandbank.api.services;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.switzerlandbank.api.entities.Balance;
import com.switzerlandbank.api.entities.Client;
import com.switzerlandbank.api.repositories.ClientRepository;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientServiceImpl implements ClientService {
	
	@Autowired
	private ClientRepository repository;
	
	@Override
	public List<Client> findAll() {
		return repository.findAll();
	}
	
	@Override
	public Client findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public Client insert(Client obj) {
		Balance balance = new Balance(null, BigDecimal.TEN, Instant.now(), obj);
		obj.setBalance(balance);
		obj = repository.save(obj);
		obj = repository.getReferenceById(obj.getId());
		obj.setAddress(obj.getAddress());
		return obj;
	}
	
	@Override
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException(id);		
		}
		repository.deleteById(id);
	}
	
	@Override
	public Client update(Client obj, Long id) {
		try {
			Client entity = repository.getReferenceById(id);
			updateData(entity, obj);
			return repository.save(obj);			
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	
	@Override
	public void updateData(Client entity, Client obj) {
		entity.setName(obj.getName());
		entity.setCpf(obj.getCpf());
		entity.setMotherName(obj.getMotherName());
		entity.setDateBirth(obj.getDateBirth());
		entity.setGender(obj.getGender());
		entity.setEmail(obj.getEmail());
		entity.setPassword(obj.getPassword());
	}

}
