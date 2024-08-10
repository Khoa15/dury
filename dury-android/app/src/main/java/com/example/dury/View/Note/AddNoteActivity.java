package com.example.dury.View.Note;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.dury.Adaper.CategorySpinner;
import com.example.dury.Model.Category;
import com.example.dury.Model.Note;
import com.example.dury.R;
import com.example.dury.Repository.CategoryRepository;
import com.example.dury.Repository.ICategoryCallback;
import com.example.dury.Repository.INoteCallback;
import com.example.dury.Repository.NoteRepository;
import com.example.dury.ViewModel.CategoryViewModel;
import com.example.dury.ViewModel.IApiCallback;
import com.example.dury.ViewModel.NoteViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class AddNoteActivity extends AppCompatActivity {
    TextView txtDateTime;
    EditText edtTxtNote, edtTxtTitle;
    Button btnAddNote, btnPickDateTime, btnPickImage;
    RequestQueue queue;
    Spinner spinnerCategory, spinnerPriority;
    ArrayList<Category> listCategories;
    CategorySpinner categorySpinner;
    CategoryRepository categoryRepository = new CategoryRepository();
    NoteRepository noteRepository = new NoteRepository();
    int     day = 0,
            month = 0,
            year = 0,
            hour = 0,
            minute = 0;
    boolean isEdit = false;
    Note oldNote;
    SharedPreferences sp;
    String userId;
    String token;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    @Override
    protected synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        String noteId = intent.getStringExtra("noteId");
        sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        userId = sp.getString("uid", null);
        token = sp.getString("accessToken", null);
        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Uri imageUri = uri;
                        sendRequest(imageUri);
                    } else {
                        Toast.makeText(this, "Không có ảnh được chọn", Toast.LENGTH_SHORT).show();
                    }
                });
        addControls();
        addEvents();
        loadData(noteId);
        initData();
    }

    private void loadData(String noteId) {
        if (noteId == null) return;
        btnAddNote.setText("Lưu");
        NoteViewModel.load(noteId, token, new INoteCallback() {
            @Override
            public void onSuccess(ArrayList<Note> listNotes) {
            }

            @Override
            public void onSuccess(Note note) {
                oldNote = note;
                if(note.getTitle() != null){
                    edtTxtTitle.setText(note.getTitle());
                }
                edtTxtNote.setText(note.getNote());
                isEdit = true;
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(AddNoteActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData(){
        ICategoryCallback iCategoryCallback = new ICategoryCallback() {

            @Override
            public void onSuccess(List<Category> categories) {
                listCategories = (ArrayList<Category>) categories;
                if(listCategories.size() == 0){
                    Toast.makeText(AddNoteActivity.this, "Chưa có danh mục", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                categorySpinner = new CategorySpinner(AddNoteActivity.this,
                        R.layout.activity_category_spinner_layout,
                        listCategories);
                spinnerCategory.setAdapter(categorySpinner);
            }

            @Override
            public void onFailure(Throwable message) {

            }
        };
        CategoryViewModel.load(iCategoryCallback, userId, token);
        //listCategories = CategoryViewModel.load();
    }

    private void addControls(){
        btnPickImage = findViewById(R.id.btnPickImage);
        txtDateTime = findViewById(R.id.txtDateTime);
        btnPickDateTime = findViewById(R.id.btnPickDateTime);
        edtTxtTitle = findViewById(R.id.edtTxtTitle);
        edtTxtNote = findViewById(R.id.edtTxtNote);
        btnAddNote = findViewById(R.id.btnAddNote);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                Note.getPriorityList()
        );
        spinnerPriority.setAdapter(adapter);
    }

    private void addEvents(){
        btnPickImage.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
        btnPickDateTime.setOnClickListener(v -> {
            v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(150).start();
            openDatePicker();
            openTimePicker();
        });
        btnAddNote.setOnClickListener(v -> {
            String strNote = edtTxtNote.getText().toString();
            String title = edtTxtTitle.getText().toString();
            v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(150).start();
            int indexCategory = spinnerCategory.getSelectedItemPosition();
            int indexPriority = spinnerPriority.getSelectedItemPosition();
            if (strNote.isEmpty()){
                Toast.makeText(this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                Note note = new Note(
                        title,
                        strNote,
                        indexPriority,
                        listCategories.get(indexCategory),
                        minute,
                        hour,
                        day,
                        month,
                        year
                );
                if(isEdit){
                    note.setId(oldNote.getId());
                    NoteViewModel.save(note, token, new IApiCallback(){
                        @Override
                        public void onSuccess(Object object) {
                            Toast.makeText(AddNoteActivity.this, "Successfully!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(AddNoteActivity.this, "Failed!", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    });
                }else{
                    INoteCallback callback = new INoteCallback() {
                        @Override
                        public void onSuccess(ArrayList<Note> listNotes) {
                        }

                        @Override
                        public void onSuccess(Note note) {
                            Toast.makeText(AddNoteActivity.this, "Successfully!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(AddNoteActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        }
                    };
                    NoteViewModel.add(note, token, callback);
                }
            }
        });
    }
    public interface ImageService {
        @Multipart
        @POST("/api/extract_text") // Thay đổi URL theo thực tế
        Call<CustomImageRequest> uploadImage(
                @Part MultipartBody.Part image
        );
        //@Part("image") RequestBody requestBody
    }
    public class CustomImageRequest{
        private byte[] image;
        private String text;
        private String error;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public byte[] getImage() {
            return image;
        }

        public void setImage(byte[] image) {
            this.image = image;
        }
    }
    private File createTempFileFromUri(Uri uri) throws IOException {
        File tempFile = File.createTempFile("image", ".jpg", getCacheDir());
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }
    private void sendRequest(Uri imageUri) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.110:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ImageService imageService = retrofit.create(ImageService.class);
        try{
            File imageFile = createTempFileFromUri(imageUri); // Chuyển đổi Uri thành File
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);

            Call<CustomImageRequest> call = imageService.uploadImage(imagePart);
            call.enqueue(new Callback<CustomImageRequest>() {
                @Override
                public void onResponse(Call<CustomImageRequest> call, Response<CustomImageRequest> response) {
                    if (response.isSuccessful()) {
                        CustomImageRequest uploadResponse = response.body();
                        // Handle the response
                        String text = uploadResponse.getText();
                        String error = uploadResponse.getError();
                        if (error != null) {
                            Toast.makeText(AddNoteActivity.this, error, Toast.LENGTH_SHORT).show();
                        } else {
                            edtTxtNote.setText(text);
                        }
                    } else {
                        Toast.makeText(AddNoteActivity.this, "Scan Failed!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CustomImageRequest> call, Throwable t) {
                    Toast.makeText(AddNoteActivity.this, "Scan Failed!", Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception exception){
            Toast.makeText(this,  exception.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void openDatePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                day = d;
                month = m;
                year = y;
                txtDateTime.setText(String.valueOf(year)+ "."+String.valueOf(month)+ "."+String.valueOf(day));

            }
        }, 2024, 06, 16);

        datePickerDialog.show();
    }


    private void openTimePicker(){

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                hour = h;
                minute = m;
                txtDateTime.setText(String.valueOf(hour)+ ":"+String.valueOf(minute));
            }
        }, 15, 30, true);

        timePickerDialog.show();
    }


}