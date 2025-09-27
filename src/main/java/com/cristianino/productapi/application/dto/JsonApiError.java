package com.cristianino.productapi.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonApiError {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("code")
    private String code;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("detail")
    private String detail;
    
    @JsonProperty("source")
    private JsonApiErrorSource source;
    
    public JsonApiError() {}
    
    public JsonApiError(String status, String title, String detail) {
        this.status = status;
        this.title = title;
        this.detail = detail;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDetail() {
        return detail;
    }
    
    public void setDetail(String detail) {
        this.detail = detail;
    }
    
    public JsonApiErrorSource getSource() {
        return source;
    }
    
    public void setSource(JsonApiErrorSource source) {
        this.source = source;
    }
    
    public static class JsonApiErrorSource {
        @JsonProperty("pointer")
        private String pointer;
        
        @JsonProperty("parameter")
        private String parameter;
        
        public JsonApiErrorSource() {}
        
        public JsonApiErrorSource(String pointer) {
            this.pointer = pointer;
        }
        
        public String getPointer() {
            return pointer;
        }
        
        public void setPointer(String pointer) {
            this.pointer = pointer;
        }
        
        public String getParameter() {
            return parameter;
        }
        
        public void setParameter(String parameter) {
            this.parameter = parameter;
        }
    }
}