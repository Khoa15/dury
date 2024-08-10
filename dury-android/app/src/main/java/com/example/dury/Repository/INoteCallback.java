package com.example.dury.Repository;

import com.example.dury.Model.Note;

import java.util.ArrayList;

public interface INoteCallback {
    void onSuccess(ArrayList<Note> listNotes);
    void onSuccess(Note note);
    void onFailure(Throwable t);
}
