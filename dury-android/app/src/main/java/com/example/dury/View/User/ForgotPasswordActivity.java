package com.example.dury.View.User;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.dury.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Button btnForget, btnToLogin;
    EditText edtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        addControls();
        addEvents();
    }

    private void addControls() {
        btnForget = findViewById(R.id.btnForget);
        btnToLogin = findViewById(R.id.btnToLogin);
        edtEmail = findViewById(R.id.edtEmail);
    }


    void addEvents()
    {
        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edtEmail.getText().toString();
                if (email.isEmpty())
                    edtEmail.setError("Please enter your email");

                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(ForgotPasswordActivity.this,"Please check your email",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
                        }
                    }
                });
            }
        });


        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
            }
        });
    }
}