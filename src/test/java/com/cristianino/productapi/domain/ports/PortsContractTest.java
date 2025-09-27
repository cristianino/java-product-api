package com.cristianino.productapi.domain.ports;

import com.cristianino.productapi.domain.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to validate that the port interfaces are properly defined
 */
class PortsContractTest {

    @Test
    void createProductUseCaseShouldHaveCorrectSignature() {
        // This test verifies that the CreateProductUseCase interface is properly defined
        CreateProductUseCase useCase = new TestCreateProductUseCase();
        
        Product product = new Product("Test Product", "Test Description", BigDecimal.valueOf(10.99));
        Product result = useCase.createProduct(product);
        
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
    }

    @Test
    void loadProductPortShouldHaveCorrectSignature() {
        // This test verifies that the LoadProductPort interface is properly defined
        LoadProductPort loadPort = new TestLoadProductPort();
        
        Optional<Product> result = loadPort.loadById(1L);
        assertTrue(result.isPresent());
        
        List<Product> allProducts = loadPort.loadAll();
        assertNotNull(allProducts);
        
        boolean exists = loadPort.existsById(1L);
        assertTrue(exists);
    }

    @Test
    void saveProductPortShouldHaveCorrectSignature() {
        // This test verifies that the SaveProductPort interface is properly defined
        SaveProductPort savePort = new TestSaveProductPort();
        
        Product product = new Product("Test Product", "Test Description", BigDecimal.valueOf(10.99));
        Product saved = savePort.save(product);
        assertNotNull(saved);
        
        boolean deleted = savePort.deleteById(1L);
        assertTrue(deleted);
    }

    // Test implementations
    private static class TestCreateProductUseCase implements CreateProductUseCase {
        @Override
        public Product createProduct(Product product) {
            product.setId(1L);
            return product;
        }
    }

    private static class TestLoadProductPort implements LoadProductPort {
        @Override
        public Optional<Product> loadById(Long id) {
            Product product = new Product("Test Product", "Test Description", BigDecimal.valueOf(10.99));
            product.setId(id);
            return Optional.of(product);
        }

        @Override
        public List<Product> loadAll() {
            return Arrays.asList(new Product("Product 1", "Description 1", BigDecimal.valueOf(10.99)));
        }

        @Override
        public boolean existsById(Long id) {
            return true;
        }
    }

    private static class TestSaveProductPort implements SaveProductPort {
        @Override
        public Product save(Product product) {
            product.setId(1L);
            return product;
        }

        @Override
        public boolean deleteById(Long id) {
            return true;
        }
    }
}