package com.switzerlandbank.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.switzerlandbank.api.entities.Balance;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
}
