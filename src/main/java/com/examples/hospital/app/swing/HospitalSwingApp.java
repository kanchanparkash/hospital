package com.examples.hospital.app.swing;

import java.awt.EventQueue;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.examples.hospital.controller.HospitalController;
import com.examples.hospital.repository.mongo.PatientMongoRepository;
import com.examples.hospital.view.swing.PatientSwingView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class HospitalSwingApp implements Callable<Void> {

	@Option(names = { "--mongo-host" }, description = "MongoDB host address")
	private String mongoHost = "localhost";

	@Option(names = { "--mongo-port" }, description = "MongoDB host port")
	private int mongoPort = 27017;

	@Option(names = { "--db-name" }, description = "Database name")
	private String databaseName = "hospital";

	@Option(names = { "--db-collection" }, description = "Collection name")
	private String collectionName = "patient";

	public static void main(String[] args) {
		new CommandLine(new HospitalSwingApp()).execute(args);
	}

	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				PatientMongoRepository patientRepository = new PatientMongoRepository(
						new MongoClient(new ServerAddress(mongoHost, mongoPort)),
						databaseName, collectionName);
				PatientSwingView patientView = new PatientSwingView();
				HospitalController hospitalController =
						new HospitalController(patientView, patientRepository);
				patientView.setHospitalController(hospitalController);
				patientView.setVisible(true);
				hospitalController.allPatients();
			} catch (Exception e) {
				Logger.getLogger(getClass().getName())
					.log(Level.SEVERE, "Exception", e);
			}
		});
		return null;
	}

}
