package com.pm.patientservice.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pm.patientservice.dto.request.PatientRequest;
import com.pm.patientservice.dto.response.PatientResponse;
import com.pm.patientservice.exception.DuplicateResourceException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.kafka.KafkaProducer;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import com.pm.patientservice.service.PatientService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PATIENT-SERVICE")
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    public List<PatientResponse> getPatients() {

        // Start getting all patient
        log.info("Start getting all patient");

        // Get all patients
        List<Patient> patients = patientRepository.findAll();

        // End getting all patient
        log.info("End getting all patient");

        // Convert patient entity to response then return
        return patients.stream().map(PatientMapper::toResponse).toList();
    }

    public PatientResponse createPatient(PatientRequest req) {

        // Start creating patient
        log.info("Start creating patient with name={}", req.getName());

        // Check if email is duplicated
        if (patientRepository.existsByEmail(req.getEmail())) {

            // Email is already exists
            log.warn("Email is already exists : ", req.getEmail());

            throw new DuplicateResourceException("Email is already exists : " + req.getEmail());
        }

        // Convert patient request to patient entity then save to DB
        Patient newPatient = patientRepository.save(PatientMapper.toEntity(req));

        // Create billing account via gRPC call to billing service
        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(), newPatient.getName(),
                newPatient.getEmail());

        // Send patient create event to kafka
        kafkaProducer.sendEvent(newPatient);

        // End creating patient
        log.info("End creating patient with name={}", req.getName());

        // Convert patient entity to response then return
        return PatientMapper.toResponse(newPatient);

    }

    public PatientResponse updatePatient(UUID patientId, PatientRequest req) {

        // Start updating patient
        log.info("Start updating patient with id = {}", patientId);

        // Check patient is exists or not
        Patient updatedPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));

        // Update patient info
        updatedPatient.setName(req.getName());
        updatedPatient.setEmail(req.getEmail());
        updatedPatient.setAddress(req.getAddress());
        updatedPatient.setDateOfBirth(LocalDate.parse(req.getDateOfBirth()));
        updatedPatient.setRegisteredDate(LocalDate.parse(req.getRegisteredDate()));

        // Save patient to DB
        patientRepository.save(updatedPatient);

        // End updating patient
        log.info("End updating patient with name={}", req.getName());

        // Convert patient entity to response then return
        return PatientMapper.toResponse(updatedPatient);

    }

    public PatientResponse deletePatient(UUID patientId) {

        // Start deleting patient
        log.info("Start deleting patient with id = {}", patientId);

        // Check patient is exists or not
        Patient deletePatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));

        // Delet patient from DB
        patientRepository.deleteById(patientId);

        // End deleting patient
        log.info("End deleting patient with id = {}", patientId);

        // Convert patient entity to response then return
        return PatientMapper.toResponse(deletePatient);

    }
}
