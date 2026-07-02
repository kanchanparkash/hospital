package com.examples.hospital.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.examples.hospital.controller.HospitalController;
import com.examples.hospital.model.Patient;
import com.examples.hospital.repository.mongo.PatientMongoRepository;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

@RunWith(GUITestRunner.class)
public class PatientSwingViewIT extends AssertJSwingJUnitTestCase {
	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private MongoClient mongoClient;

	private FrameFixture window;
	private PatientSwingView patientSwingView;
	private HospitalController hospitalController;
	private PatientMongoRepository patientRepository;

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

	@Override
	protected void onSetUp() {
		mongoClient = new MongoClient(new ServerAddress(serverAddress));
		patientRepository =
			new PatientMongoRepository(mongoClient, HOSPITAL_DB_NAME,
					PATIENT_COLLECTION_NAME);
		for (Patient patient : patientRepository.findAll()) {
			patientRepository.delete(patient.getId());
		}
		GuiActionRunner.execute(() -> {
			patientSwingView = new PatientSwingView();
			hospitalController =
				new HospitalController(patientSwingView, patientRepository);
			patientSwingView.setHospitalController(hospitalController);
			return patientSwingView;
		});
		window = new FrameFixture(robot(), patientSwingView);
		window.show();
	}

	@Override
	protected void onTearDown() {
		mongoClient.close();
	}

	@Test @GUITest
	public void testAllPatients() {
		Patient patient1 = new Patient("1", "Giuseppe Bianchi",
				"Cardiac problem", "2026-07-01");
		Patient patient2 = new Patient("2", "Anna Verdi",
				"Throat problem", "2026-07-02");
		patientRepository.save(patient1);
		patientRepository.save(patient2);
		GuiActionRunner.execute(
			() -> hospitalController.allPatients());
		assertThat(window.list().contents())
			.containsExactly(
					"1 - Giuseppe Bianchi - Cardiac problem - 2026-07-01",
					"2 - Anna Verdi - Throat problem - 2026-07-02");
	}

	@Test @GUITest
	public void testAddButtonSuccess() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("Giuseppe Bianchi");
		window.textBox("problemTextBox").enterText("Cardiac problem");
		window.textBox("admitDateTextBox").enterText("2026-07-01");
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.list().contents())
			.containsExactly("1 - Giuseppe Bianchi - Cardiac problem - 2026-07-01");
	}

	@Test @GUITest
	public void testAddButtonError() {
		patientRepository.save(new Patient("1", "Anna Verdi",
				"Throat problem", "2026-07-02"));
		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("Giuseppe Bianchi");
		window.textBox("problemTextBox").enterText("Cardiac problem");
		window.textBox("admitDateTextBox").enterText("2026-07-01");
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.list().contents())
			.isEmpty();
		window.label("errorMessageLabel")
			.requireText("Already existing patient with id 1: "
					+ "1 - Anna Verdi - Throat problem - 2026-07-02");
	}

}
