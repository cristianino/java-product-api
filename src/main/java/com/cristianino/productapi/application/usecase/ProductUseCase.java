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
        Product product = mapToProduct(productDto);
        Product savedProduct = productDomainService.createProduct(product);
        return mapToDto(savedProduct);
    }
    
    public Optional<ProductDto> getProductById(Long id) {
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
        Product product = mapToProduct(productDto);
        Product updatedProduct = productDomainService.updateProduct(id, product);
        return updatedProduct != null ? Optional.of(mapToDto(updatedProduct)) : Optional.empty();
    }
    
    public boolean deleteProduct(Long id) {
        return productDomainService.deleteProduct(id);
    }
    
    private Product mapToProduct(ProductDto dto) {
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