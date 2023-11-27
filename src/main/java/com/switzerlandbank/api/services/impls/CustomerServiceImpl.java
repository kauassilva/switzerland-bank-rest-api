package com.switzerlandbank.api.services.impls;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.switzerlandbank.api.entities.Customer;
import com.switzerlandbank.api.repositories.CustomerRepository;
import com.switzerlandbank.api.services.AccountService;
import com.switzerlandbank.api.services.AddressService;
import com.switzerlandbank.api.services.CustomerService;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private AccountService accountService;
	
	@Override
	public List<Customer> findAll() {
		return customerRepository.findAll();
	}
	
	@Override
	public Customer findById(Long id) {
		Optional<Customer> obj = customerRepository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	@Override
	@Transactional
	public Customer insert(Customer obj) {
		obj.getAddress().setCustomer(obj);
		Customer savedCustomer = customerRepository.save(obj);
		
		accountService.insert(savedCustomer);
		return savedCustomer;
	}
	
	@Override
	@Transactional
	public void delete(Long id) {
		if (!customerRepository.existsById(id)) {
			throw new ResourceNotFoundException(id);		
		}
		customerRepository.deleteById(id);
	}
	
	@Override
	@Transactional
	public Customer update(Customer newDataObj, Long id) {
		try {
			Customer entity = customerRepository.getReferenceById(id);
			updateData(entity, newDataObj);
			return customerRepository.save(entity);			
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	
	@Override
	public void updateData(Customer entity, Customer newDataObj) {
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
