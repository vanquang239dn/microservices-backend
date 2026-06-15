CREATE TABLE patients (

    patient_id UUID PRIMARY KEY,

    patient_name VARCHAR(255) NOT NULL,

    patient_email VARCHAR(255) NOT NULL,

    patient_address VARCHAR(255) NOT NULL,

    date_of_birth DATE NOT NULL,

    registered_date DATE NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_patients_patient_email UNIQUE(patient_email)
);

CREATE INDEX idx_patients_patient_name
    ON patients(patient_name);

CREATE INDEX idx_patients_patient_email
    ON patients(patient_email);