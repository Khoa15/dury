package com.example.dury.View.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dury.MainActivity;
import com.example.dury.Model.SessionManager;
import com.example.dury.Model.User;
import com.example.dury.R;
import com.example.dury.Repository.IUserCallback;
import com.example.dury.ViewModel.UserViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    Executor executor;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    //Login
    EditText edtTxtEmail, edtTxtPassword;
    Button btnLogin, btnToSignUp;
    String emailS, passS;
    TextView txtViewForgetPassword;
    ImageView imgShowPassword, imgCheckUser;
    private boolean isPasswordVisible = false;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
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
        txtViewForgetPassword = findViewById(R.id.txtViewForgetPassword);
        edtTxtEmail = findViewById(R.id.edtTxtEmail);
        edtTxtPassword = findViewById(R.id.edtTxtPass);

        btnLogin = findViewById(R.id.btnLogin);
        btnToSignUp = findViewById(R.id.btnToSignUp);

        imgShowPassword = findViewById(R.id.imgShowPassword);
        imgCheckUser = findViewById(R.id.imgCheck);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//        }
//    }

    private void addEvents() {
        txtViewForgetPassword.setOnClickListener(t ->{
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailS = edtTxtEmail.getText().toString().trim();
                passS = edtTxtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(emailS)) {
                    edtTxtEmail.setError("Nhập email...");
                    edtTxtEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(passS)) {
                    edtTxtPassword.setError("Nhập pass...");
                    edtTxtPassword.requestFocus();
                    return;
                }
                Signin();
            }

        });
        btnToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(it);
            }
        });


        imgShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPasswordVisible = !isPasswordVisible;
                if (isPasswordVisible) {
                    edtTxtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    imgShowPassword.setImageResource(R.drawable.icon_show_password);
                } else {
                    edtTxtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imgShowPassword.setImageResource(R.drawable.icon_show_password);
                }
                edtTxtPassword.setSelection(edtTxtPassword.getText().length());
            }
        });
        imgCheckUser.setVisibility(View.GONE);
        edtTxtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    imgCheckUser.setVisibility(View.GONE);
                } else {
                    imgCheckUser.setVisibility(View.VISIBLE);
                }
            }
        });


        btnLogin.setOnClickListener(v -> {
            String strEmail = edtTxtEmail.getText().toString();
            String strPassword = edtTxtPassword.getText().toString();
            FirebaseAuth.getInstance().
                    signInWithEmailAndPassword(
                            strEmail,
                            strPassword).addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    user.getIdToken(true).addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            String token = task.getResult().getToken();
                                            User user1 = new User();
                                            user1.setToken(token);
                                            sendRequestLoginToServer(user1);
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(LoginActivity.this, "Khong ton tai user", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Dang nhap LOI", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            btnLogin.setBackgroundColor(Color.parseColor("#CCCCCC"));

        });
        btnLogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Thay đổi màu nền của Button khi bắt đầu nhấn giữ
                        btnLogin.setBackgroundColor(Color.parseColor("#CCCCCC"));
                        v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(150).start();
                        break;
                    case MotionEvent.ACTION_UP:
                        // Khôi phục màu nền ban đầu của Button khi thả ra
                        btnLogin.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start();
                        break;
                }
                return false;
            }
        });
        btnToSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }


    void authWithBiometric(BiometricPrompt.AuthenticationCallback callback) {
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginActivity.this, callback);
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authenticaiton")
                .setSubtitle("Touch your fingerprint")
                .setNegativeButtonText("Cancel")
                .build();
    }

    private void Signin() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailS.trim(), passS).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();


                Intent it = new Intent(LoginActivity.this, MainActivity.class);
                it.putExtra("name", username);
                startActivity(it);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidUserException) {
                    Toast.makeText(LoginActivity.this, "Khong ton tai user", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Dang nhap LOI", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendRequestLoginToServer(User user) {
        IUserCallback userCallback = new IUserCallback() {
            @Override
            public void onSuccess(User user) {
                if (user != null) {
                    if (user.isBiometric()) {
                        authWithBiometric(new BiometricPrompt.AuthenticationCallback() {
                            @Override
                            public void onAuthenticationError(int errorCode, CharSequence errString) {
                                super.onAuthenticationError(errorCode, errString);
                            }

                            @Override
                            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                                super.onAuthenticationSucceeded(result);
                                loginSucceed(user);
                            }

                            @Override
                            public void onAuthenticationFailed() {
                                super.onAuthenticationFailed();
                                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                        biometricPrompt.authenticate(promptInfo);
                    } else {
                        loginSucceed(user);
                    }
                }
            }

            @Override
            public void onFailure(Throwable message) {
                Toast.makeText(getApplicationContext(), message.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        UserViewModel.login(user, userCallback);
    }

    private void loginSucceed(User user) {
        Toast.makeText(this, "Login successfully!", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", user.getId());
        editor.putString("accessToken", "Bearer " + user.getAccessToken());
        editor.putBoolean("isUseBiometric", user.isBiometric());
        editor.apply();
        SessionManager.getInstance().saveUser(user);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}