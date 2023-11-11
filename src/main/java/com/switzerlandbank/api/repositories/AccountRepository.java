package com.switzerlandbank.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.switzerlandbank.api.entities.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
