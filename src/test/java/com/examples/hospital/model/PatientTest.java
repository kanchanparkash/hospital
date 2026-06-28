package com.examples.hospital.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PatientTest {

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
