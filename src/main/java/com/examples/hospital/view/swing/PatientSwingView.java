package com.examples.hospital.view.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import com.examples.hospital.model.Patient;
import com.examples.hospital.view.PatientView;

public class PatientSwingView extends JFrame implements PatientView {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField txtId;
	private JTextField txtName;
	private JTextField txtProblem;
	private JTextField txtAdmitDate;
	private JButton btnAdd;
	private JList<Patient> listPatients;
	private JScrollPane scrollPane;
	private JButton btnDeleteSelected;
	private JLabel lblErrorMessage;

	private DefaultListModel<Patient> listPatientsModel;

	DefaultListModel<Patient> getListPatientsModel() {
		return listPatientsModel;
	}

	public PatientSwingView() {
		setTitle("Patient View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gblContentPane = new GridBagLayout();
		gblContentPane.columnWidths = new int[]{0, 0, 0};
		gblContentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gblContentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gblContentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gblContentPane);

		JLabel lblId = new JLabel("id");
		GridBagConstraints gbcLblId = new GridBagConstraints();
		gbcLblId.insets = new Insets(0, 0, 5, 5);
		gbcLblId.anchor = GridBagConstraints.EAST;
		gbcLblId.gridx = 0;
		gbcLblId.gridy = 0;
		contentPane.add(lblId, gbcLblId);

		txtId = new JTextField();
		KeyAdapter btnAddEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnAdd.setEnabled(
					!txtId.getText().trim().isEmpty() &&
					!txtName.getText().trim().isEmpty() &&
					!txtProblem.getText().trim().isEmpty() &&
					!txtAdmitDate.getText().trim().isEmpty()
				);
			}
		};
		txtId.addKeyListener(btnAddEnabler);
		txtId.setName("idTextBox");
		GridBagConstraints gbcIdTextField = new GridBagConstraints();
		gbcIdTextField.insets = new Insets(0, 0, 5, 0);
		gbcIdTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcIdTextField.gridx = 1;
		gbcIdTextField.gridy = 0;
		contentPane.add(txtId, gbcIdTextField);
		txtId.setColumns(10);

		JLabel lblName = new JLabel("patient name");
		GridBagConstraints gbcLblName = new GridBagConstraints();
		gbcLblName.anchor = GridBagConstraints.EAST;
		gbcLblName.insets = new Insets(0, 0, 5, 5);
		gbcLblName.gridx = 0;
		gbcLblName.gridy = 1;
		contentPane.add(lblName, gbcLblName);

		txtName = new JTextField();
		txtName.addKeyListener(btnAddEnabler);
		txtName.setName("nameTextBox");
		GridBagConstraints gbcNameTextField = new GridBagConstraints();
		gbcNameTextField.insets = new Insets(0, 0, 5, 0);
		gbcNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcNameTextField.gridx = 1;
		gbcNameTextField.gridy = 1;
		contentPane.add(txtName, gbcNameTextField);
		txtName.setColumns(10);

		JLabel lblProblem = new JLabel("problem");
		GridBagConstraints gbcLblProblem = new GridBagConstraints();
		gbcLblProblem.anchor = GridBagConstraints.EAST;
		gbcLblProblem.insets = new Insets(0, 0, 5, 5);
		gbcLblProblem.gridx = 0;
		gbcLblProblem.gridy = 2;
		contentPane.add(lblProblem, gbcLblProblem);

		txtProblem = new JTextField();
		txtProblem.addKeyListener(btnAddEnabler);
		txtProblem.setName("problemTextBox");
		GridBagConstraints gbcProblemTextField = new GridBagConstraints();
		gbcProblemTextField.insets = new Insets(0, 0, 5, 0);
		gbcProblemTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcProblemTextField.gridx = 1;
		gbcProblemTextField.gridy = 2;
		contentPane.add(txtProblem, gbcProblemTextField);
		txtProblem.setColumns(10);

		JLabel lblAdmitDate = new JLabel("admit date");
		GridBagConstraints gbcLblAdmitDate = new GridBagConstraints();
		gbcLblAdmitDate.anchor = GridBagConstraints.EAST;
		gbcLblAdmitDate.insets = new Insets(0, 0, 5, 5);
		gbcLblAdmitDate.gridx = 0;
		gbcLblAdmitDate.gridy = 3;
		contentPane.add(lblAdmitDate, gbcLblAdmitDate);

		txtAdmitDate = new JTextField();
		txtAdmitDate.addKeyListener(btnAddEnabler);
		txtAdmitDate.setName("admitDateTextBox");
		GridBagConstraints gbcAdmitDateTextField = new GridBagConstraints();
		gbcAdmitDateTextField.insets = new Insets(0, 0, 5, 0);
		gbcAdmitDateTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcAdmitDateTextField.gridx = 1;
		gbcAdmitDateTextField.gridy = 3;
		contentPane.add(txtAdmitDate, gbcAdmitDateTextField);
		txtAdmitDate.setColumns(10);

		btnAdd = new JButton("Add");
		btnAdd.setEnabled(false);
		GridBagConstraints gbcBtnAdd = new GridBagConstraints();
		gbcBtnAdd.insets = new Insets(0, 0, 5, 0);
		gbcBtnAdd.gridwidth = 2;
		gbcBtnAdd.gridx = 0;
		gbcBtnAdd.gridy = 4;
		contentPane.add(btnAdd, gbcBtnAdd);

		scrollPane = new JScrollPane();
		GridBagConstraints gbcScrollPane = new GridBagConstraints();
		gbcScrollPane.insets = new Insets(0, 0, 5, 0);
		gbcScrollPane.fill = GridBagConstraints.BOTH;
		gbcScrollPane.gridwidth = 2;
		gbcScrollPane.gridx = 0;
		gbcScrollPane.gridy = 5;
		contentPane.add(scrollPane, gbcScrollPane);

		listPatientsModel = new DefaultListModel<>();
		listPatients = new JList<>(listPatientsModel);
		listPatients.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				Patient patient = (Patient) value;
				return super.getListCellRendererComponent(list,
					getDisplayString(patient),
					index, isSelected, cellHasFocus);
			}
		});
		listPatients.addListSelectionListener(
				e -> btnDeleteSelected.setEnabled(listPatients.getSelectedIndex() != -1));
		listPatients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listPatients.setName("patientList");
		scrollPane.setViewportView(listPatients);

		btnDeleteSelected = new JButton("Delete Selected");
		btnDeleteSelected.setEnabled(false);
		GridBagConstraints gbcBtnDeleteSelected = new GridBagConstraints();
		gbcBtnDeleteSelected.insets = new Insets(0, 0, 5, 0);
		gbcBtnDeleteSelected.gridwidth = 2;
		gbcBtnDeleteSelected.gridx = 0;
		gbcBtnDeleteSelected.gridy = 6;
		contentPane.add(btnDeleteSelected, gbcBtnDeleteSelected);

		lblErrorMessage = new JLabel(" ");
		lblErrorMessage.setForeground(Color.RED);
		lblErrorMessage.setName("errorMessageLabel");
		GridBagConstraints gbcLblErrorMessage = new GridBagConstraints();
		gbcLblErrorMessage.gridwidth = 2;
		gbcLblErrorMessage.insets = new Insets(0, 0, 0, 5);
		gbcLblErrorMessage.gridx = 0;
		gbcLblErrorMessage.gridy = 7;
		contentPane.add(lblErrorMessage, gbcLblErrorMessage);
	}

	@Override
	public void showAllPatients(List<Patient> patients) {
		patients.stream().forEach(listPatientsModel::addElement);
	}

	@Override
	public void showError(String message, Patient patient) {
		lblErrorMessage.setText(message + ": " + getDisplayString(patient));
	}

	@Override
	public void patientAdded(Patient patient) {
		listPatientsModel.addElement(patient);
		resetErrorLabel();
	}

	@Override
	public void patientRemoved(Patient patient) {
	}

	private void resetErrorLabel() {
		lblErrorMessage.setText(" ");
	}

	@Override
	public void showErrorPatientNotFound(String message, Patient patient) {
		lblErrorMessage.setText(message + ": " + getDisplayString(patient));
		listPatientsModel.removeElement(patient);
	}

	private String getDisplayString(Patient patient) {
		return patient.getId() + " - " + patient.getName() + " - " + patient.getProblem()
				+ " - " + patient.getAdmitDate();
	}
}
