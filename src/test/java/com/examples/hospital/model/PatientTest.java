package com.examples.hospital.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PatientTest {

	@Test
	public void testEmptyConstructorAndSetters() {
		Patient patient = new Patient();
		patient.setId("1");
		patient.setName("Marco");

		assertThat(patient.getId())
			.isEqualTo("1");
		assertThat(patient.getName())
			.isEqualTo("Marco");
	}

	@Test
	public void testConstructorAndGetters() {
		Patient patient = new Patient("1", "Marco");

		assertThat(patient.getId())
			.isEqualTo("1");
		assertThat(patient.getName())
			.isEqualTo("Marco");
	}

	@Test
	public void testEquals() {
		Patient patient = new Patient("1", "Marco");
		Patient samePatient = new Patient("1", "Marco");

		assertThat(patient)
			.isEqualTo(samePatient);
	}

	@Test
	public void testHashCode() {
		Patient patient = new Patient("2", "Giuseppe");
		Patient samePatient = new Patient("2", "Giuseppe");

		assertThat(patient)
			.hasSameHashCodeAs(samePatient);
	}
}
