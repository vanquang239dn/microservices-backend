package com.pm.patientservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pm.patientservice.common.PatientServiceBaseController;
import com.pm.patientservice.dto.request.PatientRequest;
import com.pm.patientservice.dto.response.ApiResponse;
import com.pm.patientservice.dto.response.PatientResponse;
import com.pm.patientservice.service.PatientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patient")
@Slf4j(topic = "PATIENT-CONTROLLER")
@Tag(name = "Patient Controller", description = "API for managing patient")
public class PatientController extends PatientServiceBaseController {

    private final PatientService patientService;

    // API for fetching patient list
    @Operation(summary = "Get patient list", description = "Returns a list of patient")
    @GetMapping("/list")
    public ApiResponse<List<PatientResponse>> getPatient() {

        List<PatientResponse> patients = patientService.getPatients();

        return createSuccessResponse(patients);
    }

    // API for creating a new patient
    @Operation(summary = "Create a new patient", description = "Returns a new patient")
    @PostMapping("/create")
    public ApiResponse<String> createPatient(@Valid @RequestBody PatientRequest req) {

        patientService.createPatient(req);

        return createSuccessResponse("User created successfully");
    }

    // API for updating a new patient
    @Operation(summary = "Update an exists patient with patient id", description = "Returns an updated patient")
    @PutMapping("/update/{patientId}")
    public ApiResponse<String> updatePatient(@PathVariable UUID patientId,
            @Valid @RequestBody PatientRequest req) {

        patientService.updatePatient(patientId, req);

        return createSuccessResponse("User updated successfully");
    }

    // API for updating a new patient
    @Operation(summary = "Delete an exists patient with patient id", description = "Returns a deleted patient")
    @PutMapping("/delete/{patientId}")
    public ApiResponse<String> updatePatient(@PathVariable UUID patientId) {

        patientService.deletePatient(patientId);

        return createSuccessResponse("User deleted successfully");
    }
}
