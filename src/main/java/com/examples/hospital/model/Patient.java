package com.examples.hospital.model;

import java.util.Objects;

public class Patient {
	private String id;
	private String name;
	private String problem;
	private String admitDate;

	public Patient() {

	}

	public Patient(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Patient(String id, String name, String problem, String admitDate) {
		this.id = id;
		this.name = name;
		this.problem = problem;
		this.admitDate = admitDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Patient other = (Patient) obj;
		return Objects.equals(id, other.id)
				&& Objects.equals(name, other.name)
				&& Objects.equals(problem, other.problem)
				&& Objects.equals(admitDate, other.admitDate);
	}

	@Override
	public String toString() {
		return "Patient [id=" + id + ", name=" + name + ", problem=" + problem
				+ ", admitDate=" + admitDate + "]";
	}

}
