package com.example.apidury.model;

import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;




@Document(collection = "roles")
public class Role {
	@Id
	private String id;


	@Size(max = 20)
	private ERole name;

	public Role() {

	}

	public Role(ERole name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ERole getName() {
		return name;
	}

	public void setName(ERole name) {
		this.name = name;
	}
}