package com.examples.hospital.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

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

import com.examples.hospital.model.Patient;

@RunWith(GUITestRunner.class)
public class PatientSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	private PatientSwingView patientSwingView;

	@Override
	protected void onSetUp() {
		GuiActionRunner.execute(() -> {
			patientSwingView = new PatientSwingView();
			return patientSwingView;
		});
		window = new FrameFixture(robot(), patientSwingView);
		window.show();
	}

	@Test @GUITest
	public void testControlsInitialStates() {
		window.label(JLabelMatcher.withText("id"));
		window.textBox("idTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("name"));
		window.textBox("nameTextBox").requireEnabled();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.list("patientList");
		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testWhenIdAndNameAreNonEmptyThenAddButtonShouldBeEnabled() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("Giuseppe Bianchi");
		window.button(JButtonMatcher.withText("Add")).requireEnabled();
	}

	@Test
	public void testDeleteButtonShouldBeEnabledOnlyWhenAPatientIsSelected() {
		GuiActionRunner.execute(
			() -> patientSwingView.getListPatientsModel().addElement(new Patient("1", "Giuseppe Bianchi"))
		);
		window.list("patientList").selectItem(0);
		JButtonFixture deleteButton = window.button(JButtonMatcher.withText("Delete Selected"));
		deleteButton.requireEnabled();
		window.list("patientList").clearSelection();
		deleteButton.requireDisabled();
	}

	@Test
	public void testsShowAllPatientsShouldAddPatientDescriptionsToTheList() {
		Patient patient1 = new Patient("1", "Giuseppe Bianchi");
		Patient patient2 = new Patient("2", "Maria Rossi");
		GuiActionRunner.execute(
			() -> patientSwingView.showAllPatients(
					Arrays.asList(patient1, patient2))
		);
		String[] listContents = window.list().contents();
		assertThat(listContents)
			.containsExactly("1 - Giuseppe Bianchi", "2 - Maria Rossi");
	}
}