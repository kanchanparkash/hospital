package com.examples.hospital.repository.mongo;

import static org.assertj.core.api.Assertions.*;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.examples.hospital.model.Patient;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class PatientMongoRepositoryTest {

	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private MongoClient client;
	private PatientMongoRepository patientRepository;
	private MongoCollection<Document> patientCollection;

	private static final String HOSPITAL_DB_NAME = "hospital";
	private static final String PATIENT_COLLECTION_NAME = "patient";

	@BeforeClass
	public static void setupServer() {
		server = new MongoServer(new MemoryBackend());
		serverAddress = server.bind();
	}

	@AfterClass
	public static void shutdownServer() {
		server.shutdown();
	}

	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(serverAddress));
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
	public void testFindAllWhenDatabaseIsEmpty() {
		assertThat(patientRepository.findAll()).isEmpty();
	}

	@Test
	public void testFindAllWhenDatabaseIsNotEmpty() {
		addTestPatientToDatabase("8", "Marco", "Cardiac problem", "2026-07-01");
		addTestPatientToDatabase("9", "Viviana", "Cardiac problem", "2026-07-01");
		assertThat(patientRepository.findAll())
			.containsExactly(
				new Patient("8", "Marco", "Cardiac problem", "2026-07-01"),
				new Patient("9", "Viviana", "Cardiac problem", "2026-07-01"));
	}

	@Test
	public void testFindByIdNotFound() {
		assertThat(patientRepository.findById("10"))
			.isNull();
	}

	@Test
	public void testFindByIdFound() {
		addTestPatientToDatabase("10", "Giuseppe", "Cardiac problem", "2026-07-01");
		addTestPatientToDatabase("11", "Viviana", "Cardiac problem", "2026-07-01");
		assertThat(patientRepository.findById("11"))
			.isEqualTo(new Patient("11", "Viviana", "Cardiac problem", "2026-07-01"));
	}

	@Test
	public void testSave() {
		Patient patient = new Patient("12", "Marco", "Cardiac problem", "2026-07-01");
		patientRepository.save(patient);
		assertThat(readAllPatientsFromDatabase())
			.containsExactly(patient);
	}

	@Test
	public void testDelete() {
		addTestPatientToDatabase("13", "Giuseppe", "Cardiac problem", "2026-07-01");
		patientRepository.delete("13");
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
