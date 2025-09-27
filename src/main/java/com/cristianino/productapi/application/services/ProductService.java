package com.cristianino.productapi.application.services;

import com.cristianino.productapi.domain.model.Product;
import com.cristianino.productapi.domain.ports.CreateProductUseCase;
import com.cristianino.productapi.domain.ports.LoadProductPort;
import com.cristianino.productapi.domain.ports.SaveProductPort;

import java.time.LocalDateTime;

/**
 * Service implementation that implements the CreateProductUseCase
 * and orchestrates calls to output ports
 */
public class ProductService implements CreateProductUseCase {
    
    private final SaveProductPort saveProductPort;
    private final LoadProductPort loadProductPort;
    
    public ProductService(SaveProductPort saveProductPort, LoadProductPort loadProductPort) {
        this.saveProductPort = saveProductPort;
        this.loadProductPort = loadProductPort;
    }
    
    @Override
    public Product createProduct(Product product) {
        // Business logic can be added here (validation, rules, etc.)
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        
        return saveProductPort.save(product);
    }
}