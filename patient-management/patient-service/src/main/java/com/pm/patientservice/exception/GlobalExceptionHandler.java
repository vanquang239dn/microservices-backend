package com.pm.patientservice.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.pm.patientservice.dto.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

        // Method argument not valid exception
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ApiResponse<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                        HttpServletRequest request) {

                // Get error messages
                Map<String, String> errors = new HashMap<>();

                e.getBindingResult().getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

                return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Argument invalid", request.getRequestURI(),
                                errors);

        }

        // End point not found exception
        @ExceptionHandler(NoResourceFoundException.class)
        public ApiResponse<String> handleNoResourceFoundException(NoResourceFoundException e,
                        HttpServletRequest request) {

                return ApiResponse.error(HttpStatus.NOT_FOUND.value(), "Endpoint not found", request.getRequestURI(),
                                null);
        }

        // Duplicate Resource Exception
        @ExceptionHandler(DuplicateResourceException.class)
        public ApiResponse<String> handleDuplicateResourceException(DuplicateResourceException e,
                        HttpServletRequest request) {

                return ApiResponse.error(HttpStatus.CONFLICT.value(), e.getMessage(), request.getRequestURI(), null);

        }

        // Patient not found exception
        @ExceptionHandler(PatientNotFoundException.class)
        public ApiResponse<String> handlePatientNotFoundException(PatientNotFoundException e,
                        HttpServletRequest request) {

                return ApiResponse.error(HttpStatus.NOT_FOUND.value(), "Resource not found", request.getRequestURI(),
                                null);
        }
}
