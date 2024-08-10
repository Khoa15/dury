package com.example.apidury.repository;

import com.example.apidury.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface NoteRepository extends MongoRepository<Note, String> {
}
