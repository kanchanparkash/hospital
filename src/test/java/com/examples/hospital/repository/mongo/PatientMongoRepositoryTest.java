package com.examples.hospital.repository.mongo;

import static org.assertj.core.api.Assertions.*;

import java.net.InetSocketAddress;

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
		addTestPatientToDatabase("8", "Marco");
		addTestPatientToDatabase("9", "Viviana");
		assertThat(patientRepository.findAll())
			.containsExactly(
				new Patient("8", "Marco"),
				new Patient("9", "Viviana"));
	}

	@Test
	public void testFindByIdNotFound() {
		assertThat(patientRepository.findById("10"))
			.isNull();
	}

	private void addTestPatientToDatabase(String id, String name) {
		patientCollection.insertOne(
				new Document()
					.append("id", id)
					.append("name", name));
	}
}
