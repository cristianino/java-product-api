package com.cristianino.productapi.domain.ports;

import com.cristianino.productapi.domain.model.Product;
import java.util.List;
import java.util.Optional;

/**
 * Output port interface for loading products from persistence layer.
 * This is an outbound port (secondary port) that will be implemented by infrastructure adapters.
 */
public interface LoadProductPort {
    
    /**
     * Loads a product by its ID
     * 
     * @param id The product ID
     * @return Optional containing the product if found, empty otherwise
     */
    Optional<Product> loadById(Long id);
    
    /**
     * Loads all products
     * 
     * @return List of all products
     */
    List<Product> loadAll();
    
    /**
     * Checks if a product exists by ID
     * 
     * @param id The product ID
     * @return true if the product exists, false otherwise
     */
    boolean existsById(Long id);
}