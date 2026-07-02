package com.examples.hospital.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.examples.hospital.model.Patient;
import com.examples.hospital.repository.PatientRepository;
import com.examples.hospital.repository.mongo.PatientMongoRepository;
import com.examples.hospital.view.PatientView;
import com.mongodb.MongoClient;

public class HospitalControllerIT {

	@Mock
	private PatientView patientView;

	private PatientRepository patientRepository;

	private HospitalController hospitalController;

	private static final String HOSPITAL_DB_NAME = "hospital";
	private static final String PATIENT_COLLECTION_NAME = "patient";

	private AutoCloseable closeable;

	private static int mongoPort =
		Integer.parseInt(System.getProperty("mongo.port", "27017"));

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		patientRepository =
			new PatientMongoRepository(new MongoClient("localhost", mongoPort),
					HOSPITAL_DB_NAME, PATIENT_COLLECTION_NAME);
		for (Patient patient : patientRepository.findAll()) {
			patientRepository.delete(patient.getId());
		}
		hospitalController = new HospitalController(patientView, patientRepository);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testAllPatients() {
		Patient patient = new Patient("1", "Giuseppe Bianchi", "Cardiac problem",
				"2026-07-02");
		patientRepository.save(patient);
		hospitalController.allPatients();
		verify(patientView)
			.showAllPatients(asList(patient));
	}

}
