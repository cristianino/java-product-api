package com.cristianino.productapi.infrastructure.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for building JSON:API compliant links
 */
public class JsonApiLinksBuilder {
    
    private final Map<String, String> links;
    private final String baseUrl;
    
    private JsonApiLinksBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
        this.links = new HashMap<>();
    }
    
    public static JsonApiLinksBuilder forResource(String resourcePath) {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(resourcePath)
                .toUriString();
        return new JsonApiLinksBuilder(baseUrl);
    }
    
    public static JsonApiLinksBuilder forResourceWithId(String resourcePath, Object id) {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(resourcePath)
                .path("/{id}")
                .buildAndExpand(id)
                .toUriString();
        return new JsonApiLinksBuilder(baseUrl);
    }
    
    public JsonApiLinksBuilder self() {
        links.put("self", baseUrl);
        return this;
    }
    
    public JsonApiLinksBuilder collection(String collectionPath) {
        String collectionUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(collectionPath)
                .toUriString();
        links.put("collection", collectionUrl);
        return this;
    }
    
    public JsonApiLinksBuilder first(String resourcePath) {
        String firstUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(resourcePath)
                .queryParam("page[offset]", 0)
                .toUriString();
        links.put("first", firstUrl);
        return this;
    }
    
    public JsonApiLinksBuilder next(String resourcePath, int offset, int limit) {
        String nextUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(resourcePath)
                .queryParam("page[offset]", offset + limit)
                .queryParam("page[limit]", limit)
                .toUriString();
        links.put("next", nextUrl);
        return this;
    }
    
    public JsonApiLinksBuilder prev(String resourcePath, int offset, int limit) {
        if (offset > 0) {
            int prevOffset = Math.max(0, offset - limit);
            String prevUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(resourcePath)
                    .queryParam("page[offset]", prevOffset)
                    .queryParam("page[limit]", limit)
                    .toUriString();
            links.put("prev", prevUrl);
        }
        return this;
    }
    
    public JsonApiLinksBuilder last(String resourcePath, int totalCount, int limit) {
        int lastOffset = Math.max(0, ((totalCount - 1) / limit) * limit);
        String lastUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(resourcePath)
                .queryParam("page[offset]", lastOffset)
                .queryParam("page[limit]", limit)
                .toUriString();
        links.put("last", lastUrl);
        return this;
    }
    
    public JsonApiLinksBuilder related(String relationPath) {
        String relatedUrl = baseUrl + "/" + relationPath;
        links.put("related", relatedUrl);
        return this;
    }
    
    public JsonApiLinksBuilder customLink(String rel, String href) {
        links.put(rel, href);
        return this;
    }
    
    public Map<String, String> build() {
        return new HashMap<>(links);
    }
}