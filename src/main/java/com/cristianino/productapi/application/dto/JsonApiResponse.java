package com.cristianino.productapi.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonApiResponse<T> {
    @JsonProperty("data")
    private T data;
    
    @JsonProperty("links")
    private Map<String, String> links;
    
    @JsonProperty("meta")
    private Map<String, Object> meta;
    
    @JsonProperty("errors")
    private List<JsonApiError> errors;
    
    public JsonApiResponse() {}
    
    public JsonApiResponse(T data) {
        this.data = data;
    }
    
    public JsonApiResponse(T data, Map<String, String> links, Map<String, Object> meta) {
        this.data = data;
        this.links = links;
        this.meta = meta;
    }
    
    public JsonApiResponse(List<JsonApiError> errors) {
        this.errors = errors;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public Map<String, String> getLinks() {
        return links;
    }
    
    public void setLinks(Map<String, String> links) {
        this.links = links;
    }
    
    public Map<String, Object> getMeta() {
        return meta;
    }
    
    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }
    
    public List<JsonApiError> getErrors() {
        return errors;
    }
    
    public void setErrors(List<JsonApiError> errors) {
        this.errors = errors;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonApiResponse<?> that = (JsonApiResponse<?>) o;
        return Objects.equals(data, that.data) &&
               Objects.equals(links, that.links) &&
               Objects.equals(meta, that.meta) &&
               Objects.equals(errors, that.errors);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(data, links, meta, errors);
    }
    
    @Override
    public String toString() {
        return "JsonApiResponse{" +
               "data=" + data +
               ", links=" + links +
               ", meta=" + meta +
               ", errors=" + errors +
               '}';
    }
}