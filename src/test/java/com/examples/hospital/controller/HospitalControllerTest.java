package com.examples.hospital.controller;

import static org.mockito.Mockito.*;
import static java.util.Arrays.asList;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.examples.hospital.model.Patient;
import com.examples.hospital.repository.PatientRepository;
import com.examples.hospital.view.PatientView;

public class HospitalControllerTest {

	@Mock
	private PatientRepository patientRepository;

	@Mock
	private PatientView patientView;

	@InjectMocks
	private HospitalController hospitalController;

	private AutoCloseable closeable;

	@Before
	public void setup() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testAllPatients() {
		List<Patient> patients = asList(new Patient("3", "Giuseppe Bianchi",
				"Cardiac problem", "2026-07-01"));
		when(patientRepository.findAll())
			.thenReturn(patients);
		hospitalController.allPatients();
		verify(patientView)
			.showAllPatients(patients);
	}

	@Test
	public void testNewPatientWhenPatientDoesNotAlreadyExist() {
		Patient patient = new Patient("4", "Viviana", "Cardiac problem", "2026-07-01");
		when(patientRepository.findById("4")).
			thenReturn(null);
		hospitalController.newPatient(patient);
		InOrder inOrder = inOrder(patientRepository, patientView);
		inOrder.verify(patientRepository).save(patient);
		inOrder.verify(patientView).patientAdded(patient);
	}

	@Test
	public void testNewPatientWhenPatientAlreadyExists() {
		Patient patientToAdd = new Patient("5", "Viviana", "Cardiac problem", "2026-07-01");
		Patient existingPatient = new Patient("5", "Giuseppe", "Cardiac problem", "2026-07-01");
		when(patientRepository.findById("5")).
			thenReturn(existingPatient);
		hospitalController.newPatient(patientToAdd);
		verify(patientView)
			.showError("Already existing patient with id 5", existingPatient);
		verifyNoMoreInteractions(ignoreStubs(patientRepository));
	}

	@Test
	public void testDeletePatientWhenPatientExists() {
		Patient patientToDelete = new Patient("6", "Marco", "Cardiac problem", "2026-07-01");
		when(patientRepository.findById("6")).
			thenReturn(patientToDelete);
		hospitalController.deletePatient(patientToDelete);
		InOrder inOrder = inOrder(patientRepository, patientView);
		inOrder.verify(patientRepository).delete("6");
		inOrder.verify(patientView).patientRemoved(patientToDelete);
	}

	@Test
	public void testDeletePatientWhenPatientDoesNotExist() {
		Patient patient = new Patient("7", "Giuseppe", "Cardiac problem", "2026-07-01");
		when(patientRepository.findById("7")).
			thenReturn(null);
		hospitalController.deletePatient(patient);
		verify(patientView)
			.showErrorPatientNotFound("No existing patient with id 7", patient);
		verifyNoMoreInteractions(ignoreStubs(patientRepository));
	}
}
