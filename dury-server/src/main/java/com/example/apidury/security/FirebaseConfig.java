package com.example.apidury.security;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException{
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource("dury-846a3-firebase-adminsdk-w7f8h-898439fbe8.json").getInputStream());
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(googleCredentials)
                .build();

//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials(GoogleCredentials.getApplicationDefault())
//                .build();

        return FirebaseApp.initializeApp(options);
    }
}
