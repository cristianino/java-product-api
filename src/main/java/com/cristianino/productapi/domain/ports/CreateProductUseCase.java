package com.cristianino.productapi.domain.ports;

import com.cristianino.productapi.domain.model.Product;

/**
 * Use case interface for creating a product.
 * This is an inbound port (primary port) that defines the application's business logic.
 */
public interface CreateProductUseCase {
    
    /**
     * Creates a new product
     * 
     * @param product The product to create
     * @return The created product with generated ID
     */
    Product createProduct(Product product);
}