package com.pm.patientservice.exception;

import lombok.Getter;

@Getter
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
