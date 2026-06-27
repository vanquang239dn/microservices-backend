package com.pm.patientservice.dto.response;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(

        Instant timestamp,

        String message,

        int status,

        String requestUri,

        T result

) {

    public static <T> ApiResponse<T> success(String requestUri, T result) {
        return new ApiResponse<T>(Instant.now(), "Success", HttpStatus.OK.value(), requestUri, result);
    }

    public static <T> ApiResponse<T> error(int errorStatus, String message, String requestUri, T result) {
        return new ApiResponse<T>(Instant.now(), message, errorStatus, requestUri, result);
    }

}