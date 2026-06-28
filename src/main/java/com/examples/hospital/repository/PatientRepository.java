package com.examples.hospital.repository;

import java.util.List;

import com.examples.hospital.model.Patient;

public interface PatientRepository {
	public List<Patient> findAll();

	public Patient findById(String id);

	public void save(Patient patient);

	public void delete(String id);
}
