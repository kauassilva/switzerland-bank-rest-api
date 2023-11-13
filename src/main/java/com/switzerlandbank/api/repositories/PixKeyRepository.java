package com.switzerlandbank.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.switzerlandbank.api.entities.PixKey;

public interface PixKeyRepository extends JpaRepository<PixKey, Long> {
	
	List<PixKey> findByAccountId(Long accountId);
	
}
