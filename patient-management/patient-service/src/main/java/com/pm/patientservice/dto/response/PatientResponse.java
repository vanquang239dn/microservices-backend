package com.pm.patientservice.dto.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class PatientResponse implements Serializable {

    private String patientId;

    private String patientName;

    private String patientEmail;

    private String address;

    private String dateOfBirth;
}
