package com.pm.patientservice.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.pm.patientservice.dto.response.ExceptionResponse;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

        // Method argument not valid exception
        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                                                        @ExampleObject(name = "Validation Error", summary = "Handle bad request", value = """
                                                                        {
                                                                            "timestamp": "2026-01-01T17:30:30.123+00:00",
                                                                            "status": 400,
                                                                            "path": "api/v1/...",
                                                                            "message": "Bad Request"
                                                                        }
                                                                        """)
                                        })
                        })
        })
        public ExceptionResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                        HttpServletRequest request) {

                // Get error messages
                Map<String, String> errors = new HashMap<>();

                e.getBindingResult().getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

                return buildExceptionResponse(HttpStatus.BAD_REQUEST, "Argument invalid", errors, request);

        }

        // End point not found exception
        @ExceptionHandler(NoResourceFoundException.class)
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "404", description = "Endpoint not found", content = {
                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                                                        @ExampleObject(name = "404 Response", summary = "Handle endpoint not found exception", value = """
                                                                        {
                                                                          "timestamp": "2026-01-01T17:30:30.123+00:00",
                                                                          "status": 404,
                                                                          "path": "/auth/patient-list",
                                                                          "message": "Endpoint not found",
                                                                          "details": null
                                                                        }
                                                                        """)
                                        })
                        })
        })
        public ExceptionResponse handleNoResourceFoundException(NoResourceFoundException e,
                        HttpServletRequest request) {

                return buildExceptionResponse(HttpStatus.NOT_FOUND, "Endpoint not found", null, request);
        }

        // Duplicate Resource Exception
        @ExceptionHandler(DuplicateResourceException.class)
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "409", description = "Duplicate resource", content = {
                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                                                        @ExampleObject(name = "409 Response", summary = "Handle duplicate resource exception", value = """
                                                                        {
                                                                          "timestamp": "2026-01-01T17:30:30.123+00:00",
                                                                          "status": 409,
                                                                          "path": "/patient/add",
                                                                          "message": "Username already exists"
                                                                        }
                                                                        """)
                                        })
                        })
        })
        public ExceptionResponse handleDuplicateResourceException(DuplicateResourceException e,
                        HttpServletRequest request) {

                return buildExceptionResponse(HttpStatus.CONFLICT, e.getMessage(), null, request);
        }

        // Patient not found exception
        @ExceptionHandler(PatientNotFoundException.class)
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "404", description = "Patient not found", content = {
                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                                                        @ExampleObject(name = "404 Response", summary = "Handle patient not found exception", value = """
                                                                        {
                                                                          "timestamp": "2026-01-01T17:30:30.123+00:00",
                                                                          "status": 404,
                                                                          "path": "/update/{patientId}",
                                                                          "message": "Patient not found",
                                                                          "details": null
                                                                        }
                                                                        """)
                                        })
                        })
        })
        public ExceptionResponse handlePatientNotFoundException(PatientNotFoundException e,
                        HttpServletRequest request) {

                return buildExceptionResponse(HttpStatus.NOT_FOUND, "Patient not found", null, request);
        }

        // Builder for exception response
        private ExceptionResponse buildExceptionResponse(HttpStatus status, String message, Object details,
                        HttpServletRequest request) {

                return ExceptionResponse.builder()
                                .timestamp(Instant.now())
                                .status(status.value())
                                .path(request.getRequestURI())
                                .message(message)
                                .details(details)
                                .build();
        }
}
