package com.switzerlandbank.api.services;

import java.util.List;

import com.switzerlandbank.api.entities.Address;

public interface AddressService {
	
	List<Address> findAll();
	Address findById(Long id);
	Address update(Address entity, Address newDataObj);

}
