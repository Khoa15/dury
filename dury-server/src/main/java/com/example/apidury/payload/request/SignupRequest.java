package com.example.apidury.payload.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NonNull;

import java.util.Set;
 
public class SignupRequest {
    @NotBlank
    @NonNull
    private String token;
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    private Set<String> role;
    public @NotBlank @NonNull String getToken() {
        return token;
    }

    public void setToken(@NotBlank @NonNull String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Set<String> getRole() {
      return this.role;
    }
    
    public void setRole(Set<String> role) {
      this.role = role;
    }
}
