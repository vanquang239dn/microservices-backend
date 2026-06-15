package com.pm.patientservice.mapper;

import java.time.LocalDate;

import com.pm.patientservice.dto.request.PatientRequest;
import com.pm.patientservice.dto.response.PatientResponse;
import com.pm.patientservice.model.Patient;

public class PatientMapper {

    public static PatientResponse toResponse(Patient patient) {
        return PatientResponse.builder()
                .patientId(patient.getId().toString())
                .patientName(patient.getName())
                .patientEmail(patient.getEmail())
                .address(patient.getAddress())
                .dateOfBirth(patient.getDateOfBirth().toString())
                .build();
    }

    public static Patient toEntity(PatientRequest patientRequest) {
        return Patient.builder()
                .name(patientRequest.getName())
                .email(patientRequest.getEmail())
                .address(patientRequest.getAddress())
                .dateOfBirth(LocalDate.parse(patientRequest.getDateOfBirth()))
                .registeredDate(LocalDate.parse(patientRequest.getRegisteredDate()))
                .build();
    }

}
