package com.examples.hospital.repository.mongo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.examples.hospital.model.Patient;
import com.examples.hospital.repository.PatientRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

public class PatientMongoRepository implements PatientRepository {

	private MongoCollection<Document> patientCollection;

	public PatientMongoRepository(MongoClient client, String databaseName, String collectionName) {
		patientCollection = client
			.getDatabase(databaseName)
			.getCollection(collectionName);
	}

	@Override
	public List<Patient> findAll() {
		return StreamSupport.
				stream(patientCollection.find().spliterator(), false)
				.map(this::fromDocumentToPatient)
				.collect(Collectors.toList());
	}

	private Patient fromDocumentToPatient(Document d) {
		return new Patient(""+d.get("id"), ""+d.get("name"));
	}

	@Override
	public Patient findById(String id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void save(Patient patient) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(String id) {
		throw new UnsupportedOperationException();
	}

}
