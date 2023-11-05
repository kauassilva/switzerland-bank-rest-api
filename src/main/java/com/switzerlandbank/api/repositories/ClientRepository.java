package com.switzerlandbank.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.switzerlandbank.api.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
