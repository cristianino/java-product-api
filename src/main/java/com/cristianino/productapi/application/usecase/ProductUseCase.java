package com.cristianino.productapi.application.usecase;

import com.cristianino.productapi.application.dto.ProductDto;
import com.cristianino.productapi.domain.model.Product;
import com.cristianino.productapi.domain.service.ProductDomainService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductUseCase {
    
    private final ProductDomainService productDomainService;
    
    public ProductUseCase(ProductDomainService productDomainService) {
        this.productDomainService = productDomainService;
    }
    
    public ProductDto createProduct(ProductDto productDto) {
        if (productDto == null) {
            throw new IllegalArgumentException("ProductDto cannot be null");
        }
        Product product = mapToProduct(productDto);
        Product savedProduct = productDomainService.createProduct(product);
        return mapToDto(savedProduct);
    }
    
    public Optional<ProductDto> getProductById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        return productDomainService.getProductById(id)
                .map(this::mapToDto);
    }
    
    public List<ProductDto> getAllProducts() {
        return productDomainService.getAllProducts()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<ProductDto> updateProduct(Long id, ProductDto productDto) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (productDto == null) {
            throw new IllegalArgumentException("ProductDto cannot be null");
        }
        Product product = mapToProduct(productDto);
        Product updatedProduct = productDomainService.updateProduct(id, product);
        return updatedProduct != null ? Optional.of(mapToDto(updatedProduct)) : Optional.empty();
    }
    
    public boolean deleteProduct(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        return productDomainService.deleteProduct(id);
    }
    
    private Product mapToProduct(ProductDto dto) {
        if (dto.getAttributes() == null) {
            throw new IllegalArgumentException("ProductDto attributes cannot be null");
        }
        return new Product(
                dto.getAttributes().getName(),
                dto.getAttributes().getPrice()
        );
    }
    
    private ProductDto mapToDto(Product product) {
        return new ProductDto(
                product.getId().toString(),
                product.getName(),
                product.getPrice()
        );
    }
}