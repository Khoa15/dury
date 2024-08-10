package com.example.dury;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dury.View.User.LoginActivity;

public class LoadingAppActivity extends AppCompatActivity {
    ImageView img;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_translate_open_app);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        img = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView2);
        View();
    }
    public void View(){

        Animation imgage = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_top_down);
        img.startAnimation(imgage);
        Animation text = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_bottom_up);
        textView.startAnimation(text);


        // Đặt một delay 3 giây trước khi chuyển sang MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingAppActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4500);
    }
}