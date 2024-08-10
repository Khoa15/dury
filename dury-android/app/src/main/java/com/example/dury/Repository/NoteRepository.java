package com.example.dury.Repository;

import com.example.dury.Model.Note;
import com.example.dury.Network.RetrofitClient;
import com.example.dury.Service.NoteService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteRepository {
    private final NoteService noteService;

    public NoteRepository() {
        noteService = RetrofitClient.getClient().create(NoteService.class);
    }
    public void getAllNotes(Callback<List<Note>> callback) {
        Call<List<Note>> call = noteService.getAllNotes();
        call.enqueue(callback);
    }
    public void getAllNotesByUser(String uid, String token, Callback<List<Note>> callback){
        Call<List<Note>> call = noteService.getAllNotesByUser(uid, token);
        call.enqueue(callback);
    }

    public void addNote(Note note, String token, Callback<Note> callback) {
        Call<Note> call = noteService.addNote(note, token);
        call.enqueue(callback);
    }

    public void getNote(String noteId, String token, Callback<Note> callback) {
        Call<Note> call = noteService.getNote(noteId, token);
        call.enqueue(callback);
    }

    public void deleteNote(Note note, String token, Callback<Note> callback) {
        Call<Note> call = noteService.deleteNote(note.getId(), token);
        call.enqueue(callback);
    }

    public void saveNote(Note note, String token, Callback<Note> callback) {
        Call<Note> call = noteService.saveNote(note, token);
        call.enqueue(callback);
    }
}

