package com.cristianino.productapi.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

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
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JsonApiErrorSource that = (JsonApiErrorSource) o;
            return Objects.equals(pointer, that.pointer) &&
                   Objects.equals(parameter, that.parameter);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(pointer, parameter);
        }
        
        @Override
        public String toString() {
            return "JsonApiErrorSource{" +
                   "pointer='" + pointer + '\'' +
                   ", parameter='" + parameter + '\'' +
                   '}';
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonApiError that = (JsonApiError) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(status, that.status) &&
               Objects.equals(code, that.code) &&
               Objects.equals(title, that.title) &&
               Objects.equals(detail, that.detail) &&
               Objects.equals(source, that.source);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, status, code, title, detail, source);
    }
    
    @Override
    public String toString() {
        return "JsonApiError{" +
               "id='" + id + '\'' +
               ", status='" + status + '\'' +
               ", code='" + code + '\'' +
               ", title='" + title + '\'' +
               ", detail='" + detail + '\'' +
               ", source=" + source +
               '}';
    }
}