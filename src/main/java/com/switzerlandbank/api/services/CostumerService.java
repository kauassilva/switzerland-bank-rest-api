package com.switzerlandbank.api.services;

import java.util.List;

import com.switzerlandbank.api.entities.Costumer;

public interface CostumerService {
	
	List<Costumer> findAll();
	Costumer findById(Long id);
	Costumer insert(Costumer obj);
	void delete(Long id);
	Costumer update(Costumer obj, Long id);
	void updateData(Costumer entity, Costumer obj);

}
