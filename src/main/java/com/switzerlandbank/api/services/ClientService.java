package com.switzerlandbank.api.services;

import java.util.List;

import com.switzerlandbank.api.entities.Client;

public interface ClientService {
	
	List<Client> findAll();
	Client findById(Long id);
	Client insert(Client obj);
	void delete(Long id);
	Client update(Client obj, Long id);
	void updateData(Client entity, Client obj);

}
