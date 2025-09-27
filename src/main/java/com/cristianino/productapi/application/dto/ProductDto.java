package com.cristianino.productapi.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductDto {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("type")
    private String type = "products";
    
    @JsonProperty("attributes")
    private ProductAttributes attributes;
    
    public ProductDto() {}
    
    public ProductDto(String id, String name, BigDecimal price) {
        this.id = id;
        this.attributes = new ProductAttributes(name, price);
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public ProductAttributes getAttributes() {
        return attributes;
    }
    
    public void setAttributes(ProductAttributes attributes) {
        this.attributes = attributes;
    }
    
    public static class ProductAttributes {
        @NotBlank(message = "Name is required")
        @JsonProperty("name")
        private String name;
        
        @PositiveOrZero(message = "Price must be non-negative")
        @JsonProperty("price")
        private BigDecimal price;
        
        public ProductAttributes() {}
        
        public ProductAttributes(String name, BigDecimal price) {
            this.name = name;
            this.price = price;
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
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProductAttributes that = (ProductAttributes) o;
            return Objects.equals(name, that.name) &&
                   Objects.equals(price, that.price);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(name, price);
        }
        
        @Override
        public String toString() {
            return "ProductAttributes{" +
                   "name='" + name + '\'' +
                   ", price=" + price +
                   '}';
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDto that = (ProductDto) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(type, that.type) &&
               Objects.equals(attributes, that.attributes);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, type, attributes);
    }
    
    @Override
    public String toString() {
        return "ProductDto{" +
               "id='" + id + '\'' +
               ", type='" + type + '\'' +
               ", attributes=" + attributes +
               '}';
    }
}