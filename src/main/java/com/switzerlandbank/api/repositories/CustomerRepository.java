package com.switzerlandbank.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.switzerlandbank.api.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
