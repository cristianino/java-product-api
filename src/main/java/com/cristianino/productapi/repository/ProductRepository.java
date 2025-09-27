package com.cristianino.productapi.repository;

import com.cristianino.productapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    // Additional custom query methods can be added here if needed
}