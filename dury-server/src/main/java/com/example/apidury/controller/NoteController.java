package com.example.apidury.controller;
import com.example.apidury.model.Category;
import com.example.apidury.model.Note;
import com.example.apidury.repository.NoteRepository;
import com.example.apidury.service.NoteService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @GetMapping
    public List<Note> getAllNotes(){
        List<Note> notes = (List<Note>) noteService.loadAll();
        return notes;
    }

    @GetMapping("/u/{id}")
    public List<Note> getAllNotesByUserId(@PathVariable("id") String id){
        List<Note> notes = (List<Note>) noteService.loadAllByUserId(id);
        return notes;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> get(@PathVariable("id") String id){
        Note category = noteService.get(id).orElseThrow(() -> new RuntimeException("Not found"));
        return ResponseEntity.ok().body(category);
    }

    @PostMapping
    public Note addNote(@RequestBody Note note){
        return noteService.createNote(note);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteCategory(@PathVariable(value = "id") String id){
        noteService.deleteNote(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @PutMapping
    public ResponseEntity<Note> updateCategory(@RequestBody Note note){
        try{
            return new ResponseEntity<Note>(noteService.updateNote(note), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }}
