package com.example.dury.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dury.MainActivity;
import com.example.dury.R;

import java.util.ArrayList;
import java.util.List;

public class LichTrinh extends AppCompatActivity {

    Button backGhiChu , backthongbao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lich_trinh);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

addController();
addEvent();

    }
//    private List<FolderModel_ver2> getListCategory(){
//        List<FolderModel_ver2> categoryList = new ArrayList<>();
//        categoryList.add(new FolderModel_ver2("1", "Phân bón","2023"));
//        categoryList.add(new FolderModel_ver2("2", "Phun thuốc","2023"));
//        categoryList.add(new FolderModel_ver2("3", "Phân bón","2023"));
//        categoryList.add(new FolderModel_ver2("4", "Phun thuốc","2023"));
//        categoryList.add(new FolderModel_ver2("1", "Phân bón","2023"));
//        categoryList.add(new FolderModel_ver2("2", "Phun thuốc","2023"));
//        categoryList.add(new FolderModel_ver2("3", "Phân bón","2023"));
//        categoryList.add(new FolderModel_ver2("4", "Phun thuốc","2023"));
//        return categoryList;
//    }
    void addController(){
        backGhiChu = findViewById(R.id.btnBackLichTrinh);
        backthongbao = findViewById(R.id.btnBackThongBao);
    }
    void addEvent(){

        backGhiChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        backthongbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }
}