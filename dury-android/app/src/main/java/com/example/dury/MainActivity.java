package com.example.dury;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dury.Model.Category;
import com.example.dury.View.Category.CategoryFragment;
import com.example.dury.View.Note.NoteFragment;

import com.example.dury.View.User.FriendFrag;
import com.example.dury.View.User.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentChangeListener {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    RecyclerView recyclerView;
    ArrayList<Category> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String userName = getIntent().getStringExtra("name");
        Toast.makeText(this, "Welcome, " + userName + "!", Toast.LENGTH_SHORT).show(); // Display toast notification

        bottomNavigationView = findViewById(R.id.bottom_nav);

        frameLayout = findViewById(R.id.frame_layout);
        openFragment(new HomeFrag());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.action_home) {
                    openFragment(new HomeFrag());
                    return true;
                } else if (item.getItemId() == R.id.action_note) {
                    openFragment(new NoteFragment());
                    return true;
                } else if (item.getItemId() == R.id.action_category) {
                    openFragment(new CategoryFragment());
                    return true;
                } else if (item.getItemId() == R.id.action_friend) {
                    openFragment(new FriendFrag());
                    return true;
                }  else if (item.getItemId() == R.id.action_account) {
                    openProfileFragment(userName);
                    return true;
                } else return false;
            }
        });
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openProfileFragment(String userName) {
        Profile profileFragment = Profile.newInstance(userName);
        openFragment(profileFragment);
    }

    @Override
    public void onFragmentChange(int itemId) {
        bottomNavigationView.setSelectedItemId(itemId);
    }

}