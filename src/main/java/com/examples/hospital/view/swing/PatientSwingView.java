package com.examples.hospital.view.swing;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.examples.hospital.model.Patient;
import com.examples.hospital.view.PatientView;

public class PatientSwingView extends JFrame implements PatientView {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField txtId;
	private JTextField txtName;
	private JButton btnAdd;
	private JList<Patient> listPatients;
	private JScrollPane scrollPane;
	private JButton btnDeleteSelected;
	private JLabel lblErrorMessage;

	private DefaultListModel<Patient> listPatientsModel;

	public PatientSwingView() {
		setTitle("Patient View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gblContentPane = new GridBagLayout();
		gblContentPane.columnWidths = new int[]{0, 0, 0};
		gblContentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gblContentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gblContentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
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
					!txtName.getText().trim().isEmpty()
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

		JLabel lblName = new JLabel("name");
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

		btnAdd = new JButton("Add");
		btnAdd.setEnabled(false);
		GridBagConstraints gbcBtnAdd = new GridBagConstraints();
		gbcBtnAdd.insets = new Insets(0, 0, 5, 0);
		gbcBtnAdd.gridwidth = 2;
		gbcBtnAdd.gridx = 0;
		gbcBtnAdd.gridy = 2;
		contentPane.add(btnAdd, gbcBtnAdd);

		scrollPane = new JScrollPane();
		GridBagConstraints gbcScrollPane = new GridBagConstraints();
		gbcScrollPane.insets = new Insets(0, 0, 5, 0);
		gbcScrollPane.fill = GridBagConstraints.BOTH;
		gbcScrollPane.gridwidth = 2;
		gbcScrollPane.gridx = 0;
		gbcScrollPane.gridy = 3;
		contentPane.add(scrollPane, gbcScrollPane);

		listPatientsModel = new DefaultListModel<>();
		listPatients = new JList<>(listPatientsModel);
		listPatients.setName("patientList");
		scrollPane.setViewportView(listPatients);

		btnDeleteSelected = new JButton("Delete Selected");
		btnDeleteSelected.setEnabled(false);
		GridBagConstraints gbcBtnDeleteSelected = new GridBagConstraints();
		gbcBtnDeleteSelected.insets = new Insets(0, 0, 5, 0);
		gbcBtnDeleteSelected.gridwidth = 2;
		gbcBtnDeleteSelected.gridx = 0;
		gbcBtnDeleteSelected.gridy = 4;
		contentPane.add(btnDeleteSelected, gbcBtnDeleteSelected);

		lblErrorMessage = new JLabel(" ");
		lblErrorMessage.setForeground(Color.RED);
		lblErrorMessage.setName("errorMessageLabel");
		GridBagConstraints gbcLblErrorMessage = new GridBagConstraints();
		gbcLblErrorMessage.gridwidth = 2;
		gbcLblErrorMessage.insets = new Insets(0, 0, 0, 5);
		gbcLblErrorMessage.gridx = 0;
		gbcLblErrorMessage.gridy = 5;
		contentPane.add(lblErrorMessage, gbcLblErrorMessage);
	}

	@Override
	public void showAllPatients(List<Patient> patients) {
	}

	@Override
	public void showError(String message, Patient patient) {
	}

	@Override
	public void patientAdded(Patient patient) {
	}

	@Override
	public void patientRemoved(Patient patient) {
	}

	@Override
	public void showErrorPatientNotFound(String message, Patient patient) {
	}
}
