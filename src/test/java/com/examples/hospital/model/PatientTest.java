package com.examples.hospital.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PatientTest {

	@Test
	public void testToString() {
		Patient patient = new Patient("3", "Giuseppe Bianchi",
				"Cardiac problem", "2026-07-01");

		assertThat(patient)
			.hasToString("Patient [id=3, name=Giuseppe Bianchi, "
					+ "problem=Cardiac problem, admitDate=2026-07-01]");
	}

	@Test
	public void testEquals() {
		Patient patient = new Patient("1", "Giuseppe Bianchi",
				"Cardiac problem", "2026-07-01");
		Patient samePatient = new Patient("1", "Giuseppe Bianchi",
				"Cardiac problem", "2026-07-01");
		Patient differentPatient = new Patient("1", "Giuseppe Bianchi",
				"Throat problem", "2026-07-02");

		assertThat(patient)
			.isEqualTo(samePatient)
			.isNotEqualTo(differentPatient);
	}

	@Test
	public void testHashCode() {
		Patient patient = new Patient("2", "Giuseppe Bianchi",
				"Cardiac problem", "2026-07-01");
		Patient samePatient = new Patient("2", "Giuseppe Bianchi",
				"Cardiac problem", "2026-07-01");

		assertThat(patient)
			.hasSameHashCodeAs(samePatient);
	}
}
