package com.example.mock2.common.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    private T payload;
    private String message;
    private String status;
    private Object error;
    private Object metadata;

    public static <T> Response<T> success(T data) {
        return new Response<T>()
                .setMessage("Success")
                .setPayload(data)
                .setStatus("success");
    }

    public static <T,V> Response<V> success(T data, Function<T, V> mapper) {
        return new Response<V>()
                .setMessage("Success")
                .setPayload(mapper.apply(data))
                .setStatus("success");
    }

    public static Response<Void> error(String message) {
        return new Response<Void>()
                .setMessage(message)
                .setStatus("error");
    }

    public static Response<Void> internalServerError(String message) {
        return new Response<Void>()
                .setMessage(message)
                .setStatus("error");
    }

    public static <T> ResponseBuilder<T> builder() {
        return new ResponseBuilder<T>();
    }

    public static <K> Response<List<K>> paging(Page<K> page) {
        return new Response<List<K>>()
                .setMetadata(new PageMetadata(page.getSize(), page.getTotalElements(), page.getTotalPages(), page.getNumber()))
                .setStatus("success")
                .setPayload(page.getContent());
    }

    public static <K,V> Response<List<V>> paging(Page<K> page, Function<K,V> mapper) {
        return new Response<List<V>>()
                .setMetadata(new PageMetadata(page.getSize(), page.getTotalElements(), page.getTotalPages(), page.getNumber()))
                .setStatus("success")
                .setPayload(page.getContent().stream().map(mapper).toList());
    }

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PageMetadata {
        private final int size;
        private final long totalElements;
        private final int totalPages;
        private final int number;

        public PageMetadata(int size, long totalElements, int totalPages, int number) {
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.number = number;
        }
    }
}
