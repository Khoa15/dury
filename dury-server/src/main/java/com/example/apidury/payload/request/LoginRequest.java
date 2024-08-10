package com.example.apidury.payload.request;


import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public class LoginRequest {
	@NotBlank
	@NonNull
	private String token;

	public @NotBlank @NonNull String getToken() {
		return token;
	}

	public void setToken(@NotBlank @NonNull String token) {
		this.token = token;
	}
}
