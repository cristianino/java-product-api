package com.cristianino.productapi.application.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.ArrayList;

/**
 * Response DTO para comunicación entre microservicios
 * Formato simplificado para respuestas de listas de productos
 */
public class ProductListClientResponse {
    
    @JsonProperty("products")
    private List<ProductClientDto> products = new ArrayList<>();
    
    @JsonProperty("totalElements")
    private long totalElements;
    
    @JsonProperty("success")
    private boolean success = true;
    
    @JsonProperty("message")
    private String message;
    
    // Constructors
    public ProductListClientResponse() {}
    
    public ProductListClientResponse(List<ProductClientDto> products) {
        this.products = products != null ? products : new ArrayList<>();
        this.totalElements = this.products.size();
        this.success = true;
    }
    
    public ProductListClientResponse(List<ProductClientDto> products, long totalElements) {
        this.products = products != null ? products : new ArrayList<>();
        this.totalElements = totalElements;
        this.success = true;
    }
    
    // Factory methods
    public static ProductListClientResponse success(List<ProductClientDto> products) {
        return new ProductListClientResponse(products);
    }
    
    public static ProductListClientResponse success(List<ProductClientDto> products, long totalElements) {
        return new ProductListClientResponse(products, totalElements);
    }
    
    public static ProductListClientResponse error(String message) {
        ProductListClientResponse response = new ProductListClientResponse();
        response.success = false;
        response.message = message;
        return response;
    }
    
    // Getters and Setters
    public List<ProductClientDto> getProducts() {
        return products;
    }
    
    public void setProducts(List<ProductClientDto> products) {
        this.products = products != null ? products : new ArrayList<>();
        this.totalElements = this.products.size();
    }
    
    public long getTotalElements() {
        return totalElements;
    }
    
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "ProductListClientResponse{" +
                "products=" + products.size() + " items" +
                ", totalElements=" + totalElements +
                ", success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}