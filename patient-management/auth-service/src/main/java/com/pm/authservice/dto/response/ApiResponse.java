package com.pm.authservice.dto.response;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(

        Instant timestamp,

        int status,

        String message,

        String requestUri,

        T result

) {

    public static <T> ApiResponse<T> success(String requestUri, T result) {
        return new ApiResponse<T>(Instant.now(), HttpStatus.OK.value(), "Success", requestUri, result);
    }

    public static <T> ApiResponse<T> error(int status, String message, String requestUri, T result) {
        return new ApiResponse<T>(Instant.now(), status, message, requestUri, result);
    }

}
