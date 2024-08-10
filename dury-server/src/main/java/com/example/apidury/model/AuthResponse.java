package com.example.apidury.model;

public class AuthResponse {
    String uid;
    String token;

    public AuthResponse(String uid, String token) {
        this.uid = uid;
        this.token = token;
    }
}
