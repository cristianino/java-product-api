package com.cristianino.productapi.application.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * DTO simplificado para comunicación entre microservicios
 * Usado cuando el microservicio de inventario necesita información básica del producto
 */
public class ProductClientDto {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("price")
    private BigDecimal price;
    
    @JsonProperty("available")
    private boolean available = true;
    
    // Constructors
    public ProductClientDto() {}
    
    public ProductClientDto(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.available = true;
    }
    
    public ProductClientDto(Long id, String name, BigDecimal price, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.available = available;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public boolean isAvailable() {
        return available;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    @Override
    public String toString() {
        return "ProductClientDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", available=" + available +
                '}';
    }
}