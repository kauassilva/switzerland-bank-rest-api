package com.switzerlandbank.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.switzerlandbank.api.entities.Costumer;

public interface CostumerRepository extends JpaRepository<Costumer, Long> {
}
