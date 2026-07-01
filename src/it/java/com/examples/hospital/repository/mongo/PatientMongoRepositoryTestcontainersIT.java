package com.examples.hospital.repository.mongo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import com.examples.hospital.model.Patient;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class PatientMongoRepositoryTestcontainersIT {

	@ClassRule
	public static final MongoDBContainer mongo =
		new MongoDBContainer("mongo:5");

	private MongoClient client;
	private PatientMongoRepository patientRepository;
	private MongoCollection<Document> patientCollection;

	private static final String HOSPITAL_DB_NAME = "hospital";
	private static final String PATIENT_COLLECTION_NAME = "patient";

	@Before
	public void setup() {
		client = new MongoClient(
			new ServerAddress(
				mongo.getHost(),
				mongo.getFirstMappedPort()));
		patientRepository =
			new PatientMongoRepository(client, HOSPITAL_DB_NAME, PATIENT_COLLECTION_NAME);
		MongoDatabase database = client.getDatabase(HOSPITAL_DB_NAME);
		database.drop();
		patientCollection = database.getCollection(PATIENT_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		client.close();
	}

	@Test
	public void testFindAll() {
		addTestPatientToDatabase("14", "Marco", "Cardiac problem", "2026-07-01");
		addTestPatientToDatabase("15", "Viviana", "Cardiac problem", "2026-07-01");
		assertThat(patientRepository.findAll())
			.containsExactly(
				new Patient("14", "Marco", "Cardiac problem", "2026-07-01"),
				new Patient("15", "Viviana", "Cardiac problem", "2026-07-01"));
	}

	@Test
	public void testFindById() {
		addTestPatientToDatabase("16", "Giuseppe", "Cardiac problem", "2026-07-01");
		addTestPatientToDatabase("17", "Viviana", "Cardiac problem", "2026-07-01");
		assertThat(patientRepository.findById("17"))
			.isEqualTo(new Patient("17", "Viviana", "Cardiac problem", "2026-07-01"));
	}

	@Test
	public void testSave() {
		Patient patient = new Patient("18", "Marco", "Cardiac problem", "2026-07-01");
		patientRepository.save(patient);
		assertThat(readAllPatientsFromDatabase())
			.containsExactly(patient);
	}

	@Test
	public void testDelete() {
		addTestPatientToDatabase("19", "Giuseppe", "Cardiac problem", "2026-07-01");
		patientRepository.delete("19");
		assertThat(readAllPatientsFromDatabase())
			.isEmpty();
	}

	private void addTestPatientToDatabase(String id, String name, String problem,
			String admitDate) {
		patientCollection.insertOne(
				new Document()
					.append("id", id)
					.append("name", name)
					.append("problem", problem)
					.append("admitDate", admitDate));
	}

	private List<Patient> readAllPatientsFromDatabase() {
		return StreamSupport.
			stream(patientCollection.find().spliterator(), false)
				.map(d -> new Patient(""+d.get("id"), ""+d.get("name"),
						""+d.get("problem"), ""+d.get("admitDate")))
				.collect(Collectors.toList());
	}
}
