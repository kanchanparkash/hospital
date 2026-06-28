package com.examples.hospital.view;

import java.util.List;

import com.examples.hospital.model.Patient;

public interface PatientView {

	void showAllPatients(List<Patient> patients);

	void showError(String message, Patient patient);

	void patientAdded(Patient patient);

}
