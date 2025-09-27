package com.cristianino.productapi.infrastructure.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;

public class JsonApiRequest<T> {
    @Valid
    @JsonProperty("data")
    private T data;
    
    public JsonApiRequest() {}
    
    public JsonApiRequest(T data) {
        this.data = data;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
}