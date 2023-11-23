package com.switzerlandbank.api.services.impls;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.switzerlandbank.api.entities.Costumer;
import com.switzerlandbank.api.repositories.CostumerRepository;
import com.switzerlandbank.api.services.AccountService;
import com.switzerlandbank.api.services.AddressService;
import com.switzerlandbank.api.services.CostumerService;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CostumerServiceImpl implements CostumerService {
	
	@Autowired
	private CostumerRepository costumerRepository;
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private AccountService accountService;
	
	@Override
	public List<Costumer> findAll() {
		return costumerRepository.findAll();
	}
	
	@Override
	public Costumer findById(Long id) {
		Optional<Costumer> obj = costumerRepository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	@Override
	@Transactional
	public Costumer insert(Costumer obj) {
		obj.getAddress().setCostumer(obj);
		Costumer savedClient = costumerRepository.save(obj);
		
		accountService.insert(savedClient);
		return savedClient;
	}
	
	@Override
	public void delete(Long id) {
		if (!costumerRepository.existsById(id)) {
			throw new ResourceNotFoundException(id);		
		}
		costumerRepository.deleteById(id);
	}
	
	@Override
	public Costumer update(Costumer newDataObj, Long id) {
		try {
			Costumer entity = costumerRepository.getReferenceById(id);
			updateData(entity, newDataObj);
			return costumerRepository.save(entity);			
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	
	@Override
	public void updateData(Costumer entity, Costumer newDataObj) {
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
