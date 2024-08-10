package com.example.dury.View.User;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dury.LoadingAppActivity;
import com.example.dury.MainActivity;
import com.example.dury.Model.Session;
import com.example.dury.Model.SessionManager;
import com.example.dury.Model.User;
import com.example.dury.R;
import com.example.dury.Repository.IUserCallback;
import com.example.dury.ViewModel.UserViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    EditText editEmail, edtTxtUser, edtTxtPass;
    Button btnSignUp, btnToLogin;
    DatabaseReference databaseReference;
    String emailCheck, passCheck, nameCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        addControls();
        addEvents();
    }

    private void addControls() {
        editEmail = findViewById(R.id.editEmail);
        edtTxtUser = findViewById(R.id.editUser);
        edtTxtPass = findViewById(R.id.editPass);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnToLogin = findViewById(R.id.btnToLogin);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();
        }
    }

    private void addEvents() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailCheck = editEmail.getText().toString().trim();
                nameCheck = edtTxtUser.getText().toString().trim();
                passCheck = edtTxtPass.getText().toString().trim();
                if (TextUtils.isEmpty(nameCheck)) {
                    edtTxtUser.setError("Nhập user...");
                    edtTxtUser.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(emailCheck)) {
                    editEmail.setError("Nhập email...");
                    editEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(passCheck)) {
                    edtTxtPass.setError("Nhập pass...");
                    edtTxtPass.requestFocus();
                    return;
                }
                Signup();
            }

        });
        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(it);
            }
        });
    }

    private void Signup() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailCheck.trim(), passCheck)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(nameCheck).build();
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        firebaseUser.updateProfile(userProfileChangeRequest);

                        User account = new User(FirebaseAuth.getInstance().getUid(), nameCheck, emailCheck, passCheck);
                        databaseReference.child(FirebaseAuth.getInstance().getUid()).setValue(account);
                        firebaseUser.getIdToken(true).addOnCompleteListener(task ->{
                            if(task.isSuccessful()){
                                String idToken = task.getResult().getToken();
                                User user1 = new User(idToken, nameCheck);
                                sendSignUpRequestToServer(user1);
                            }
                        });

//                        Intent it = new Intent(SignUpActivity.this, MainActivity.class);
//                        it.putExtra("name", nameCheck);
//                        startActivity(it);
//                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "Dang ky loi", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendSignUpRequestToServer(User user) {
        UserViewModel.signup(user, new IUserCallback() {
            @Override
            public void onSuccess(User user) {
                if (user != null) {
                    Toast.makeText(SignUpActivity.this, "Successfully!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable message) {
                Toast.makeText(SignUpActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}