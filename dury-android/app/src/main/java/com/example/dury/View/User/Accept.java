package com.example.dury.View.User;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dury.Adaper.FriendAdapter;
import com.example.dury.Model.User;
import com.example.dury.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Accept extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FriendAdapter friendAdapter;
    private ArrayList<User> friendList;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accept);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolbarAccep);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Lời mời kết bạn");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Quay về màn hình trước đó khi nhấn nút back trên toolbar
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        friendList = new ArrayList<>();
        friendAdapter = new FriendAdapter(this, friendList);
        recyclerView.setAdapter(friendAdapter);

        // Lấy tham chiếu đến node "friend_requests" trong Firebase Database
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("friend_requests").child(currentUserId);


        // Lắng nghe sự thay đổi trong dữ liệu
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friendList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String requesterId = snapshot.getKey();
                    boolean isRequest = snapshot.getValue(Boolean.class);

                    if (isRequest) {
                        // Lấy thông tin của mỗi user từ node "users"
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(requesterId);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                String id = userSnapshot.child("id").getValue(String.class);
                                String username = userSnapshot.child("username").getValue(String.class);
                                String email = userSnapshot.child("email").getValue(String.class);

                                // Thêm user vào danh sách bạn bè
                                friendList.add(new User(id, username, email, false)); // Gán isFriend mặc định là false

                                // Cập nhật RecyclerView
                                friendAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Xử lý khi có lỗi xảy ra
                            }
                        });
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }
}