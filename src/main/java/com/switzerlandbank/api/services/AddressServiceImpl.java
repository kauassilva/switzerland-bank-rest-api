package com.switzerlandbank.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.repositories.AddressRepository;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	private AddressRepository repository;
	
	@Override
	public List<Address> findAll() {
		return repository.findAll();
	}
	
	@Override
	public Address findById(Long id) {
		Optional<Address> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	@Override
	public Address update(Address obj, Long id) {
		try {
			Address entity = repository.getReferenceById(id);
			updateData(entity, obj);
			return repository.save(entity);			
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	@Override
	public void updateData(Address entity, Address obj) {
		entity.setStreet(obj.getStreet());
		entity.setNeighborhood(obj.getNeighborhood());
		entity.setCity(obj.getCity());
		entity.setState(obj.getState());
		entity.setPostalCode(obj.getPostalCode());
	}

}
