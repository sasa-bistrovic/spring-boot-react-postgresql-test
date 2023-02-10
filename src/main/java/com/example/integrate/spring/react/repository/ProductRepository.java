package com.example.integrate.spring.react.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.integrate.spring.react.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByIsavailable(boolean isavailable);
	List<Product> findByNameContaining(String name);
}
