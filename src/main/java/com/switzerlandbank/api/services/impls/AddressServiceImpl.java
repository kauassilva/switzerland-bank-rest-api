package com.switzerlandbank.api.services.impls;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.switzerlandbank.api.entities.Address;
import com.switzerlandbank.api.repositories.AddressRepository;
import com.switzerlandbank.api.services.AddressService;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;

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
	public Address update(Address entity, Address newDataObj) {
		entity.setStreet(newDataObj.getStreet());
		entity.setNumber(newDataObj.getNumber());
		entity.setNeighborhood(newDataObj.getNeighborhood());
		entity.setCity(newDataObj.getCity());
		entity.setState(newDataObj.getState());
		entity.setPostalCode(newDataObj.getPostalCode());
		return repository.save(entity);
	}

}
