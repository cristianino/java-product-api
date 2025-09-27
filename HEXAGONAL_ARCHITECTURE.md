# Hexagonal Architecture - Domain Ports

This document describes the domain ports implemented for the Product API following the Hexagonal Architecture pattern.

## Overview

Hexagonal Architecture (also known as Ports and Adapters) separates the core business logic from external concerns through well-defined interfaces called "ports".

## Domain Ports

### Inbound Ports (Primary Ports)
These define what the application can do - the use cases available to external actors.

#### CreateProductUseCase
- **Purpose**: Defines the contract for creating new products
- **Location**: `com.cristianino.productapi.domain.ports.CreateProductUseCase`
- **Methods**:
  - `createProduct(Product product)`: Creates a new product and returns it with generated ID

### Outbound Ports (Secondary Ports)
These define what the application needs from external systems.

#### LoadProductPort
- **Purpose**: Defines the contract for loading products from persistence
- **Location**: `com.cristianino.productapi.domain.ports.LoadProductPort`
- **Methods**:
  - `loadById(Long id)`: Loads a product by ID
  - `loadAll()`: Loads all products
  - `existsById(Long id)`: Checks if a product exists

#### SaveProductPort
- **Purpose**: Defines the contract for saving products to persistence
- **Location**: `com.cristianino.productapi.domain.ports.SaveProductPort`
- **Methods**:
  - `save(Product product)`: Saves a product
  - `deleteById(Long id)`: Deletes a product by ID

## Domain Model

### Product
- **Location**: `com.cristianino.productapi.domain.model.Product`
- **Fields**:
  - `id`: Unique identifier
  - `name`: Product name
  - `description`: Product description
  - `price`: Product price
  - `createdAt`: Creation timestamp
  - `updatedAt`: Last update timestamp

## Implementation Example

The `ProductService` class demonstrates how an inbound port (use case) can be implemented by orchestrating calls to outbound ports:

```java
@Service
public class ProductService implements CreateProductUseCase {
    
    private final SaveProductPort saveProductPort;
    private final LoadProductPort loadProductPort;
    
    // Implementation uses the outbound ports to fulfill the use case
    @Override
    public Product createProduct(Product product) {
        // Business logic and validation
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        
        return saveProductPort.save(product);
    }
}
```

## Benefits

1. **Testability**: Easy to mock ports for unit testing
2. **Flexibility**: Infrastructure can be changed without affecting business logic
3. **Separation of Concerns**: Clear boundaries between layers
4. **Domain-Driven Design**: Business logic remains in the center
5. **Plugin Architecture**: External systems are pluggable adapters

## Next Steps

To complete the implementation, you would typically:

1. Create infrastructure adapters that implement the outbound ports
2. Create web controllers that use the inbound ports
3. Configure dependency injection to wire everything together
4. Add validation and error handling
5. Implement additional use cases as needed