package com.examples.hospital.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PatientTest {

	@Test
	public void testEmptyConstructorAndSetters() {
		Patient patient = new Patient();
		patient.setId("1");
		patient.setName("Mario Rossi");

		assertThat(patient.getId())
			.isEqualTo("1");
		assertThat(patient.getName())
			.isEqualTo("Mario Rossi");
	}
}
