package com.example.dury.View.Note;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.RequestQueue;
import com.example.dury.Adaper.CategorySpinner;
import com.example.dury.Model.Category;
import com.example.dury.R;
import com.example.dury.Repository.CategoryRepository;
import com.example.dury.Repository.NoteRepository;

import java.util.ArrayList;

public class EditNoteActivity extends AppCompatActivity {
    TextView txtDateTime;
    EditText edtTxtNote, edtTxtTitle;
    Button btnAddNote, btnPickDateTime;
    RequestQueue queue;
    Spinner spinnerCategory, spinnerPriority;
    ArrayList<Category> listCategories;
    CategorySpinner categorySpinner;
    CategoryRepository categoryRepository = new CategoryRepository();
    NoteRepository noteRepository = new NoteRepository();
    int day, month, year, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}