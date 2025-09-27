package com.cristianino.productapi.domain.ports;

import com.cristianino.productapi.domain.model.Product;

/**
 * Output port interface for saving products to persistence layer.
 * This is an outbound port (secondary port) that will be implemented by infrastructure adapters.
 */
public interface SaveProductPort {
    
    /**
     * Saves a product to the persistence layer
     * 
     * @param product The product to save
     * @return The saved product with generated/updated fields
     */
    Product save(Product product);
    
    /**
     * Deletes a product by its ID
     * 
     * @param id The product ID to delete
     * @return true if the product was deleted, false if it didn't exist
     */
    boolean deleteById(Long id);
}