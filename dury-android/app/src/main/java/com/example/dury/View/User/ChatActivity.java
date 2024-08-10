package com.example.dury.View.User;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dury.Adaper.AccountAdapter;
import com.example.dury.Adaper.MessageAdapter;
import com.example.dury.Model.MessageModel;
import com.example.dury.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    String receriverID ,receriverName, senderRoom , receivedRoom;
    String senderID , senderName;

    DatabaseReference dbReferencesSender , dbReferencesReceiver, UserReferences;

    ImageView sendBtn;
    EditText messageText;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        UserReferences = FirebaseDatabase.getInstance().getReference("users");
        receriverID = getIntent().getStringExtra("id");
        receriverName = getIntent().getStringExtra("name");

        getSupportActionBar().setTitle(receriverName);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Quay về màn hình trước đó khi nhấn nút back trên toolbar
            }
        });
        if(receriverID != null){
            senderRoom = FirebaseAuth.getInstance().getUid()+receriverID;
            receivedRoom = receriverID + FirebaseAuth.getInstance().getUid();
        }
        sendBtn = findViewById(R.id.sendMessageIcon);

        messageAdapter = new MessageAdapter(this, senderRoom, receivedRoom);

        recyclerView = findViewById(R.id.chatrecycler);
        messageText = findViewById(R.id.messageEdit);

        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbReferencesSender = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);

        dbReferencesReceiver = FirebaseDatabase.getInstance().getReference("chats").child(receivedRoom);
        dbReferencesSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MessageModel> messages = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    messages.add(messageModel);
                }

                // Sắp xếp danh sách tin nhắn theo thời gian
                Collections.sort(messages, new Comparator<MessageModel>() {
                    @Override
                    public int compare(MessageModel message1, MessageModel message2) {
                        return Long.compare(message1.getTimestamp(), message2.getTimestamp());
                    }
                });

                messageAdapter.clear();
                for(MessageModel message : messages){
                    messageAdapter.add(message);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageText.getText().toString();
                if(message.trim().length()> 0){
                    SendMessage(message);
                }else {
                    Toast.makeText(ChatActivity.this,"Tin nhan khong duoc trong",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private  void SendMessage(String mess){
        String messageID = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();
        MessageModel messageModel = new MessageModel(messageID,FirebaseAuth.getInstance().getUid(),mess,timestamp);
        messageAdapter.add(messageModel);

        dbReferencesSender.child(messageID).setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActivity.this,"Loi gui tin nhan",Toast.LENGTH_SHORT).show();
            }
        });
        dbReferencesReceiver.child(messageID).setValue(messageModel);
        recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
        messageText.setText("");
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_top,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == R.id.logout)
//        {
//            FirebaseAuth.getInstance().signOut();
//            startActivity(new Intent(ChatActivity.this,Son_DangNhap.class));
//            finish();
//            return true;
//        }
//        return false;
//    }
}