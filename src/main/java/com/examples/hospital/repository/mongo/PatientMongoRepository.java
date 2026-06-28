package com.examples.hospital.repository.mongo;

import java.util.Collections;
import java.util.List;

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
		return Collections.emptyList();
	}

	@Override
	public Patient findById(String id) {
		return null;
	}

	@Override
	public void save(Patient patient) {

	}

	@Override
	public void delete(String id) {

	}

}
