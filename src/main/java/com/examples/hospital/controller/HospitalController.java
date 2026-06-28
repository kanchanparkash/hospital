package com.examples.hospital.controller;

import com.examples.hospital.model.Patient;
import com.examples.hospital.repository.PatientRepository;
import com.examples.hospital.view.PatientView;

public class HospitalController {
	private PatientView patientView;
	private PatientRepository patientRepository;

	public HospitalController(PatientView patientView, PatientRepository patientRepository) {
		this.patientView = patientView;
		this.patientRepository = patientRepository;
	}

	public void allPatients() {
		patientView.showAllPatients(patientRepository.findAll());
	}

	public void newPatient(Patient patient) {
		Patient existingPatient = patientRepository.findById(patient.getId());
		if (existingPatient != null) {
			patientView.showError("Already existing patient with id " + patient.getId(),
					existingPatient);
			return;
		}

		patientRepository.save(patient);
		patientView.patientAdded(patient);
	}

	public void deletePatient(Patient patient) {
		if (patientRepository.findById(patient.getId()) != null) {
			patientRepository.delete(patient.getId());
			patientView.patientRemoved(patient);
		}
	}

}
