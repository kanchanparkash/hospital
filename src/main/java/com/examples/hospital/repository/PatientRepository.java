package com.examples.hospital.repository;

import java.util.List;

import com.examples.hospital.model.Patient;

public interface PatientRepository {
	public List<Patient> findAll();
}
