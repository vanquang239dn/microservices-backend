package com.pm.patientservice.exception;

import lombok.Getter;

@Getter
public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(String message) {
        super(message);
    }
}
