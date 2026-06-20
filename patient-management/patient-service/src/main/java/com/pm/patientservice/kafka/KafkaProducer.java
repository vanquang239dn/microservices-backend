package com.pm.patientservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.pm.patientservice.model.Patient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import patient.events.PatientEvent;

@RequiredArgsConstructor
@Service
@Slf4j(topic = "PATIENT-KAFKA-SERVICE")
public class KafkaProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendEvent(Patient patient) {

        PatientEvent patientEvent = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED")
                .build();

        try {
            // Send patient created message to kafka
            kafkaTemplate.send("patient", patientEvent.toByteArray());
        } catch (Exception e) {
            log.error("Error sending Patient Created even: {}", patientEvent);
        }
    }
}
