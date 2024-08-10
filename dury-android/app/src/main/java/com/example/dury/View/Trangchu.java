package com.example.dury.View;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dury.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Trangchu extends AppCompatActivity {

    Button btnGhiChu,btnThongBao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trangchu);


        addcontroll();
//        event();

        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(R.id.action_home == menuItem.getItemId()){
                    Toast.makeText(Trangchu.this,"Home",Toast.LENGTH_SHORT).show();
                } else if (R.id.action_note == menuItem.getItemId()) {
                    Toast.makeText(Trangchu.this,"Ghi Chú",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

    }
    void addcontroll(){
        btnGhiChu = findViewById(R.id.buttonTaoGhiChu);
        btnThongBao = findViewById(R.id.buttonTaoThongBao);
    }
//    void event(){
//        btnGhiChu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Trangchu.this, NoteActivity.class);
//                startActivity(intent);
//            }
//        });
//        btnThongBao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Trangchu.this, NotificationActivity.class);
//                startActivity(intent);
//            }
//        });
//
//    }
}