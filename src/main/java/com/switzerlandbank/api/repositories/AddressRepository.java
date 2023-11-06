package com.switzerlandbank.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.switzerlandbank.api.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
