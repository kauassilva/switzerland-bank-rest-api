package com.switzerlandbank.api.services;

import java.util.List;

import com.switzerlandbank.api.entities.PixKey;

public interface PixKeyService {

	List<PixKey> findAll();
	List<PixKey> findByAccountId(Long accountId);
	PixKey findById(Long id);
	PixKey insert(PixKey obj);
	void delete(Long id);
	
}
