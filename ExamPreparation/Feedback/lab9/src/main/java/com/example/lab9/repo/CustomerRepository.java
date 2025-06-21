package com.example.lab9.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lab9.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByName(String name);
}