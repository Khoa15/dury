package com.example.dury.ViewModel;

import com.example.dury.Model.Note;
import com.example.dury.Repository.INoteCallback;
import com.example.dury.Repository.NoteRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteViewModel {
    static NoteRepository noteRepository = new NoteRepository();
    static ArrayList<Note> listNotes = new ArrayList<>();

    public static void getAllNotesByUser(String userId, String token, INoteCallback iNoteCallback) {
        noteRepository.getAllNotesByUser(userId, token, new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                if(response.isSuccessful()){
                    iNoteCallback.onSuccess((ArrayList<Note>)response.body());
                }else{
                    iNoteCallback.onFailure(new Throwable("Failed"));
                }
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable throwable) {
                iNoteCallback.onFailure(throwable);
            }
        });
    }
    public static void load(INoteCallback iNoteCallback){
        noteRepository.getAllNotes(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                iNoteCallback.onSuccess((ArrayList<Note>)response.body());
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable throwable) {
                iNoteCallback.onFailure(throwable);
            }
        });
    }


    public static void add(Note note, String token, INoteCallback iNoteCallback){
        noteRepository.addNote(note, token, new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if(response.isSuccessful()){
                    iNoteCallback.onSuccess(response.body());
                }else{
                    iNoteCallback.onFailure(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable throwable) {
                iNoteCallback.onFailure(throwable);
            }
        });
    }

    public static void load(String noteId, String token, INoteCallback iNoteCallback) {
        noteRepository.getNote(noteId, token, new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if(response.isSuccessful()){
                    iNoteCallback.onSuccess(response.body());
                }else{
                    iNoteCallback.onFailure(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable throwable) {
                iNoteCallback.onFailure(throwable);
            }
        });
    }

    public static void delete(Note note, String token, IApiCallback iApiCallback) {
        noteRepository.deleteNote(note, token, new Callback<Note>() {

            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if(response.isSuccessful()){
                    iApiCallback.onSuccess(response.body());
                }else{
                    iApiCallback.onFailure(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable throwable) {
                iApiCallback.onFailure(throwable);
            }
        });
    }

    public static void save(Note note, String token, IApiCallback iApiCallback) {
        noteRepository.saveNote(note, token, new Callback<Note>() {

            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if(response.isSuccessful()){
                    iApiCallback.onSuccess(response.body());
                }else{
                    iApiCallback.onFailure(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable throwable) {
                iApiCallback.onFailure(throwable);
            }
        });
    }
}
