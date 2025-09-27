package com.cristianino.productapi.application.dto;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to build JSON:API compliant links
 */
public class JsonApiLinksBuilder {
    
    private final Map<String, String> links;
    
    public JsonApiLinksBuilder() {
        this.links = new HashMap<>();
    }
    
    public static JsonApiLinksBuilder builder() {
        return new JsonApiLinksBuilder();
    }
    
    /**
     * Add self link pointing to the current resource
     */
    public JsonApiLinksBuilder self(String path) {
        links.put("self", buildAbsoluteUrl(path));
        return this;
    }
    
    /**
     * Add related link pointing to related resources
     */
    public JsonApiLinksBuilder related(String path) {
        links.put("related", buildAbsoluteUrl(path));
        return this;
    }
    
    /**
     * Add collection link pointing to the collection this resource belongs to
     */
    public JsonApiLinksBuilder collection(String path) {
        links.put("collection", buildAbsoluteUrl(path));
        return this;
    }
    
    /**
     * Add first link for pagination
     */
    public JsonApiLinksBuilder first(String path) {
        links.put("first", buildAbsoluteUrl(path));
        return this;
    }
    
    /**
     * Add last link for pagination
     */
    public JsonApiLinksBuilder last(String path) {
        links.put("last", buildAbsoluteUrl(path));
        return this;
    }
    
    /**
     * Add prev link for pagination
     */
    public JsonApiLinksBuilder prev(String path) {
        links.put("prev", buildAbsoluteUrl(path));
        return this;
    }
    
    /**
     * Add next link for pagination
     */
    public JsonApiLinksBuilder next(String path) {
        links.put("next", buildAbsoluteUrl(path));
        return this;
    }
    
    /**
     * Add custom link
     */
    public JsonApiLinksBuilder custom(String rel, String path) {
        links.put(rel, buildAbsoluteUrl(path));
        return this;
    }
    
    /**
     * Build and return the links map
     */
    public Map<String, String> build() {
        return new HashMap<>(links);
    }
    
    /**
     * Build absolute URL from relative path
     */
    private String buildAbsoluteUrl(String path) {
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path;
        }
        
        try {
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(path)
                    .toUriString();
        } catch (Exception e) {
            // Fallback if no web context available (e.g., in tests)
            return path;
        }
    }
}