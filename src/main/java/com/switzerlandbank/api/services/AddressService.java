package com.switzerlandbank.api.services;

import java.util.List;

import com.switzerlandbank.api.entities.Address;

public interface AddressService {
	
	List<Address> findAll();
	Address findById(Long id);
	Address update(Address obj, Long id);
	void updateData(Address entity, Address obj);

}
