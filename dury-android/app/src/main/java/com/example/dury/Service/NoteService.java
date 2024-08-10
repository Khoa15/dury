package com.example.dury.Service;

import com.example.dury.Model.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NoteService {
    @GET("/api/v1/notes")
    Call<List<Note>> getAllNotes();
    @POST("/api/v1/notes")
    Call<Note> addNote(@Body Note note, @Header("Authorization") String token);
    @GET("/api/v1/notes/{id}")
    Call<Note> getNote(@Path("id") String id, @Header("Authorization") String token);

    @DELETE("/api/v1/notes/{id}")
    Call<Note> deleteNote(@Path("id") String id, @Header("Authorization") String token);

    @GET("/api/v1/notes/u/{uid}")
    Call<List<Note>> getAllNotesByUser(@Path("uid") String uid,@Header("Authorization") String token);

    @PUT("/api/v1/notes")
    Call<Note> saveNote(@Body Note note, @Header("Authorization") String token);
}
