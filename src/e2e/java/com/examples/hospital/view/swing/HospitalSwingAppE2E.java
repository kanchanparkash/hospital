package com.examples.hospital.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.Document;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;

@RunWith(GUITestRunner.class)
public class HospitalSwingAppE2E extends AssertJSwingJUnitTestCase {

	@ClassRule
	public static final MongoDBContainer mongo =
		new MongoDBContainer("mongo:5");

	private static final String DB_NAME = "test-db";
	private static final String COLLECTION_NAME = "test-collection";

	private static final String PATIENT_FIXTURE_1_ID = "1";
	private static final String PATIENT_FIXTURE_1_NAME = "Giuseppe Bianchi";
	private static final String PATIENT_FIXTURE_1_PROBLEM = "Cardiac problem";
	private static final String PATIENT_FIXTURE_1_ADMIT_DATE = "2026-07-01";
	private static final String PATIENT_FIXTURE_2_ID = "2";
	private static final String PATIENT_FIXTURE_2_NAME = "Anna Verdi";
	private static final String PATIENT_FIXTURE_2_PROBLEM = "Throat problem";
	private static final String PATIENT_FIXTURE_2_ADMIT_DATE = "2026-07-02";

	private MongoClient mongoClient;

	private FrameFixture window;

	@Override
	protected void onSetUp() {
		String containerIpAddress = mongo.getHost();
		Integer mappedPort = mongo.getFirstMappedPort();
		mongoClient = new MongoClient(containerIpAddress, mappedPort);
		mongoClient.getDatabase(DB_NAME).drop();
		addTestPatientToDatabase(PATIENT_FIXTURE_1_ID,
				PATIENT_FIXTURE_1_NAME, PATIENT_FIXTURE_1_PROBLEM,
				PATIENT_FIXTURE_1_ADMIT_DATE);
		addTestPatientToDatabase(PATIENT_FIXTURE_2_ID,
				PATIENT_FIXTURE_2_NAME, PATIENT_FIXTURE_2_PROBLEM,
				PATIENT_FIXTURE_2_ADMIT_DATE);
		application("com.examples.hospital.app.swing.HospitalSwingApp")
			.withArgs(
				"--mongo-host=" + containerIpAddress,
				"--mongo-port=" + mappedPort.toString(),
				"--db-name=" + DB_NAME,
				"--db-collection=" + COLLECTION_NAME
			)
			.start();
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Patient View".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());
		window.focus();
	}

	@Override
	protected void onTearDown() {
		mongoClient.close();
	}

	@Test @GUITest
	public void testOnStartAllDatabaseElementsAreShown() {
		assertThat(window.list().contents())
			.anySatisfy(e -> assertThat(e).contains(PATIENT_FIXTURE_1_ID,
					PATIENT_FIXTURE_1_NAME))
			.anySatisfy(e -> assertThat(e).contains(PATIENT_FIXTURE_2_ID,
					PATIENT_FIXTURE_2_NAME));
	}

	@Test @GUITest
	public void testAddButtonSuccess() {
		window.textBox("idTextBox").enterText("10");
		window.textBox("nameTextBox").enterText("Paolo Neri");
		window.textBox("problemTextBox").enterText("Back pain");
		window.textBox("admitDateTextBox").enterText("2026-07-03");
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.list().contents())
			.anySatisfy(e -> assertThat(e).contains("10", "Paolo Neri",
					"Back pain", "2026-07-03"));
	}

	@Test @GUITest
	public void testAddButtonError() {
		window.textBox("idTextBox").enterText(PATIENT_FIXTURE_1_ID);
		window.textBox("nameTextBox").enterText("Paolo Neri");
		window.textBox("problemTextBox").enterText("Back pain");
		window.textBox("admitDateTextBox").enterText("2026-07-03");
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.label("errorMessageLabel").text())
			.contains(PATIENT_FIXTURE_1_ID, PATIENT_FIXTURE_1_NAME);
	}

	@Test @GUITest
	public void testDeleteButtonSuccess() {
		window.list("patientList")
			.selectItem(Pattern.compile(".*" + PATIENT_FIXTURE_1_NAME + ".*"));
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		assertThat(window.list().contents())
			.noneMatch(e -> e.contains(PATIENT_FIXTURE_1_NAME));
	}

	@Test @GUITest
	public void testDeleteButtonError() {
		window.list("patientList")
			.selectItem(Pattern.compile(".*" + PATIENT_FIXTURE_1_NAME + ".*"));
		removeTestPatientFromDatabase(PATIENT_FIXTURE_1_ID);
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		assertThat(window.label("errorMessageLabel").text())
			.contains(PATIENT_FIXTURE_1_ID, PATIENT_FIXTURE_1_NAME);
	}

	private void addTestPatientToDatabase(String id, String name, String problem,
			String admitDate) {
		mongoClient
			.getDatabase(DB_NAME)
			.getCollection(COLLECTION_NAME)
			.insertOne(
				new Document()
					.append("id", id)
					.append("name", name)
					.append("problem", problem)
					.append("admitDate", admitDate));
	}

	private void removeTestPatientFromDatabase(String id) {
		mongoClient
			.getDatabase(DB_NAME)
			.getCollection(COLLECTION_NAME)
			.deleteOne(Filters.eq("id", id));
	}
}
