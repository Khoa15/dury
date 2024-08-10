package com.example.apidury.security.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {
    public FirebaseToken authentication(String idToken) throws Exception{
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    }
}
