package com.switzerlandbank.api.services.impls;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.switzerlandbank.api.entities.Client;
import com.switzerlandbank.api.repositories.ClientRepository;
import com.switzerlandbank.api.services.AccountService;
import com.switzerlandbank.api.services.AddressService;
import com.switzerlandbank.api.services.ClientService;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientServiceImpl implements ClientService {
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private AccountService accountService;
	
	@Override
	public List<Client> findAll() {
		return clientRepository.findAll();
	}
	
	@Override
	public Client findById(Long id) {
		Optional<Client> obj = clientRepository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	@Override
	public Client insert(Client obj) {
		obj.getAddress().setClient(obj);
		Client savedClient = clientRepository.save(obj);
		
		accountService.insert(savedClient);
		return savedClient;
	}
	
	@Override
	public void delete(Long id) {
		if (!clientRepository.existsById(id)) {
			throw new ResourceNotFoundException(id);		
		}
		clientRepository.deleteById(id);
	}
	
	@Override
	public Client update(Client newDataObj, Long id) {
		try {
			Client entity = clientRepository.getReferenceById(id);
			updateData(entity, newDataObj);
			return clientRepository.save(entity);			
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	
	@Override
	public void updateData(Client entity, Client newDataObj) {
		entity.setName(newDataObj.getName());
		entity.setCpf(newDataObj.getCpf());
		entity.setMotherName(newDataObj.getMotherName());
		entity.setDateBirth(newDataObj.getDateBirth());
		entity.setGender(newDataObj.getGender());
		entity.setEmail(newDataObj.getEmail());
		entity.setPassword(newDataObj.getPassword());
		
		if (newDataObj.getAddress() != null) {
			addressService.update(entity.getAddress(), newDataObj.getAddress());
		}
	}

}
