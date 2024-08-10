package com.example.apidury.service;

import com.example.apidury.model.Category;
import com.example.apidury.model.Note;
import com.example.apidury.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    public List<Note> loadAll(){
        return noteRepository.findAll();
    }
    public List<Note> loadAllByUserId(String id) {
        Criteria criteria = Criteria.where("category.user.id").is(id);
        Query query = new Query(criteria);
        List<Note> notes = mongoTemplate.find(query, Note.class);
        return notes;
    }
    public List<Note> loadAllByCategoryId(String categoryId){
        Criteria criteria = Criteria.where("category.id").is(categoryId);
        Query query = new Query(criteria);
        List<Note> notes = mongoTemplate.find(query, Note.class);
        return notes;
    }
    public Note createNote(Note note){
        return noteRepository.save(note);
    }

    public Note updateNote(Note note){
        Optional<Note> notedb = this.noteRepository.findById(note.getId());

        if(notedb.isPresent()){
            Note noteUpdate = notedb.get();
            noteUpdate.setId(note.getId());
            noteUpdate.setNote(note.getNote());
            noteUpdate.setCategory(note.getCategory());
            noteUpdate.setPriority(note.getPriority());
            noteRepository.save(noteUpdate);
            return noteUpdate;
        }
        else{
            throw new RuntimeException("Note not found!");
        }
    }

    public void deleteNote(String note){
        try{
            noteRepository.deleteById(note);
        }catch(Exception e){
            throw new IllegalStateException("Delete have some error!");
        }
    }

    public Optional<Note> get(String id) {
        return  noteRepository.findById(id);
    }

}
