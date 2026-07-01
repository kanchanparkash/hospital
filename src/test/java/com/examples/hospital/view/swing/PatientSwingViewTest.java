package com.examples.hospital.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import javax.swing.DefaultListModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.examples.hospital.controller.HospitalController;
import com.examples.hospital.model.Patient;

@RunWith(GUITestRunner.class)
public class PatientSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	private PatientSwingView patientSwingView;

	@Mock
	private HospitalController hospitalController;

	private AutoCloseable closeable;

	@Override
	protected void onSetUp() {
		closeable = MockitoAnnotations.openMocks(this);
		GuiActionRunner.execute(() -> {
			patientSwingView = new PatientSwingView();
			patientSwingView.setHospitalController(hospitalController);
			return patientSwingView;
		});
		window = new FrameFixture(robot(), patientSwingView);
		window.show();
	}

	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

	@Test @GUITest
	public void testControlsInitialStates() {
		window.label(JLabelMatcher.withText("id"));
		window.textBox("idTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("patient name"));
		window.textBox("nameTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("problem"));
		window.textBox("problemTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("admit date"));
		window.textBox("admitDateTextBox").requireEnabled();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.list("patientList");
		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testWhenAllFieldsAreNonEmptyThenAddButtonShouldBeEnabled() {
		enterPatientData("1", "Giuseppe Bianchi", "Cardiac problem", "2026-07-01");
		window.button(JButtonMatcher.withText("Add")).requireEnabled();
	}

	@Test
	public void testWhenAnyFieldIsBlankThenAddButtonShouldBeDisabled() {
		String[][] patientDataWithOneBlankField = {
				{ " ", "Giuseppe Bianchi", "Cardiac problem", "2026-07-01" },
				{ "1", " ", "Cardiac problem", "2026-07-01" },
				{ "1", "Giuseppe Bianchi", " ", "2026-07-01" },
				{ "1", "Giuseppe Bianchi", "Cardiac problem", " " }
		};

		for (String[] patientData : patientDataWithOneBlankField) {
			enterPatientData(patientData[0], patientData[1],
					patientData[2], patientData[3]);
			window.button(JButtonMatcher.withText("Add")).requireDisabled();
			clearPatientData();
		}
	}

	@Test
	public void testDeleteButtonShouldBeEnabledOnlyWhenAPatientIsSelected() {
		GuiActionRunner.execute(
			() -> patientSwingView.getListPatientsModel().addElement(
					new Patient("1", "Giuseppe Bianchi",
							"Cardiac problem", "2026-07-01"))
		);
		window.list("patientList").selectItem(0);
		JButtonFixture deleteButton = window.button(JButtonMatcher.withText("Delete Selected"));
		deleteButton.requireEnabled();
		window.list("patientList").clearSelection();
		deleteButton.requireDisabled();
	}

	@Test
	public void testsShowAllPatientsShouldAddPatientDescriptionsToTheList() {
		Patient patient1 = new Patient("1", "Giuseppe Bianchi", "Cardiac problem", "2026-07-01");
		Patient patient2 = new Patient("2", "Anna Verdi", "Throat problem", "2026-07-02");
		GuiActionRunner.execute(
			() -> patientSwingView.showAllPatients(
					Arrays.asList(patient1, patient2))
		);
		String[] listContents = window.list().contents();
		assertThat(listContents)
			.containsExactly(
					"1 - Giuseppe Bianchi - Cardiac problem - 2026-07-01",
					"2 - Anna Verdi - Throat problem - 2026-07-02");
	}

	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLabel() {
		Patient patient = new Patient("1", "Giuseppe Bianchi",
				"Cardiac problem", "2026-07-01");
		GuiActionRunner.execute(
			() -> patientSwingView.showError("error message", patient)
		);
		window.label("errorMessageLabel")
			.requireText("error message: 1 - Giuseppe Bianchi - Cardiac problem - 2026-07-01");
	}

	@Test
	public void testShowErrorPatientNotFound() {
		Patient patient1 = new Patient("1", "Giuseppe Bianchi",
				"Cardiac problem", "2026-07-01");
		Patient patient2 = new Patient("2", "Anna Verdi",
				"Throat problem", "2026-07-02");
		GuiActionRunner.execute(
			() -> {
				DefaultListModel<Patient> listPatientsModel =
						patientSwingView.getListPatientsModel();
				listPatientsModel.addElement(patient1);
				listPatientsModel.addElement(patient2);
			}
		);
		GuiActionRunner.execute(
			() -> patientSwingView.showErrorPatientNotFound("error message", patient1)
		);
		window.label("errorMessageLabel")
			.requireText("error message: 1 - Giuseppe Bianchi - Cardiac problem - 2026-07-01");
		assertThat(window.list().contents())
			.containsExactly("2 - Anna Verdi - Throat problem - 2026-07-02");
	}

	@Test
	public void testPatientAddedShouldAddThePatientToTheListAndResetTheErrorLabel() {
		Patient patient = new Patient("1", "Giuseppe Bianchi",
				"Cardiac problem", "2026-07-01");
		GuiActionRunner.execute(
				() -> patientSwingView.patientAdded(patient)
				);
		String[] listContents = window.list().contents();
		assertThat(listContents)
			.containsExactly("1 - Giuseppe Bianchi - Cardiac problem - 2026-07-01");
		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testPatientRemovedShouldRemoveThePatientFromTheListAndResetTheErrorLabel() {
		Patient patient1 = new Patient("1", "Giuseppe Bianchi",
				"Cardiac problem", "2026-07-01");
		Patient patient2 = new Patient("2", "Anna Verdi",
				"Throat problem", "2026-07-02");
		GuiActionRunner.execute(
			() -> {
				DefaultListModel<Patient> listPatientsModel =
						patientSwingView.getListPatientsModel();
				listPatientsModel.addElement(patient1);
				listPatientsModel.addElement(patient2);
			}
		);
		GuiActionRunner.execute(
			() -> patientSwingView.patientRemoved(patient1)
		);
		String[] listContents = window.list().contents();
		assertThat(listContents)
			.containsExactly("2 - Anna Verdi - Throat problem - 2026-07-02");
		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testAddButtonShouldDelegateToHospitalControllerNewPatient() {
		enterPatientData("1", "Giuseppe Bianchi", "Cardiac problem", "2026-07-01");
		window.button(JButtonMatcher.withText("Add")).click();
		verify(hospitalController).newPatient(new Patient("1", "Giuseppe Bianchi",
				"Cardiac problem", "2026-07-01"));
	}

	private void enterPatientData(String id, String name, String problem, String admitDate) {
		window.textBox("idTextBox").enterText(id);
		window.textBox("nameTextBox").enterText(name);
		window.textBox("problemTextBox").enterText(problem);
		window.textBox("admitDateTextBox").enterText(admitDate);
	}

	private void clearPatientData() {
		window.textBox("idTextBox").setText("");
		window.textBox("nameTextBox").setText("");
		window.textBox("problemTextBox").setText("");
		window.textBox("admitDateTextBox").setText("");
	}
}
