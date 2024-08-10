package com.example.apidury;

import com.example.apidury.model.Category;
import com.example.apidury.model.Note;
import com.example.apidury.repository.NoteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class ApiDuryApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiDuryApplication.class, args);
	}
}
