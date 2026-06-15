package com.pm.patientservice.service;

import java.util.List;
import java.util.UUID;

import com.pm.patientservice.dto.request.PatientRequest;
import com.pm.patientservice.dto.response.PatientResponse;

public interface PatientService {

    public List<PatientResponse> getPatients();

    public PatientResponse createPatient(PatientRequest patientRequest);

    public PatientResponse updatePatient(UUID patientId, PatientRequest patientRequest);

    public PatientResponse deletePatient(UUID patientId);
}
