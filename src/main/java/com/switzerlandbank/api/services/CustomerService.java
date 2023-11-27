package com.switzerlandbank.api.services;

import java.util.List;

import com.switzerlandbank.api.entities.Customer;

public interface CustomerService {
	
	List<Customer> findAll();
	Customer findById(Long id);
	Customer insert(Customer obj);
	void delete(Long id);
	Customer update(Customer obj, Long id);
	void updateData(Customer entity, Customer obj);

}
