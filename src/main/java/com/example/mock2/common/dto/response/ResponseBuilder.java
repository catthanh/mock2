package com.example.mock2.common.dto.response;

public class ResponseBuilder<T> {
    private Response<T> response;

    public ResponseBuilder<T> message(String message) {
        response.setMessage(message);
        return this;
    }

    public ResponseBuilder<T> status(String status) {
        response.setStatus(status);
        return this;
    }

    public ResponseBuilder<T> payload(T payload) {
        response.setPayload(payload);
        return this;
    }

    public ResponseBuilder<T> error(Object error) {
        response.setError(error);
        return this;
    }

    public ResponseBuilder<T> metadata(Object metadata) {
        response.setMetadata(metadata);
        return this;
    }

    public Response<T> build() {
        return response;
    }
}
