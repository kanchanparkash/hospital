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
		List<Patient> patients = asList(new Patient());
		when(patientRepository.findAll())
			.thenReturn(patients);
		hospitalController.allPatients();
		verify(patientView)
			.showAllPatients(patients);
	}

	@Test
	public void testNewPatientWhenPatientDoesNotAlreadyExist() {
		Patient patient = new Patient("4", "Viviana");
		when(patientRepository.findById("4")).
			thenReturn(null);
		hospitalController.newPatient(patient);
		InOrder inOrder = inOrder(patientRepository, patientView);
		inOrder.verify(patientRepository).save(patient);
		inOrder.verify(patientView).patientAdded(patient);
	}
}
