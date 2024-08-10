

package com.example.dury.View.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dury.Adaper.AccountAdapter;

import com.example.dury.Model.User;
import com.example.dury.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendFrag extends Fragment {
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private Button searchButton, ketban;

    private DatabaseReference usersRef;

    private DatabaseReference friendsRef;

    private AccountAdapter accountAdapter;

    private ArrayList<User> accountList;

    public FriendFrag() {
        // Required empty public constructor
    }

    public static FriendFrag newInstance() {
        return new FriendFrag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);

        addController(view);
        addEvent();


        return view;
    }

    private void addController(View view) {
        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        recyclerView = view.findViewById(R.id.messages_list);
        accountAdapter = new AccountAdapter(getContext());
        recyclerView.setAdapter(accountAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void addEvent() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        friendsRef = FirebaseDatabase.getInstance().getReference("friends").child(currentUserId);

        friendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                accountAdapter.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String friendId = dataSnapshot.getKey();
                    loadFriendDetails(friendId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi khi tải bạn bè: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchEditText.getText().toString().trim();
                if (!searchQuery.isEmpty()) {
                    searchAccounts(searchQuery);
                } else {
                    friendsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            accountAdapter.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String friendId = dataSnapshot.getKey();
                                loadFriendDetails(friendId);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Xử lý lỗi nếu cần
                        }
                    });
                }
            }
        });

    }

    private void searchAccounts(String query) {
        accountAdapter.clear();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        Query searchQuery = usersRef.orderByChild("username").startAt(query).endAt(query + "\uf8ff");

        searchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String uid = dataSnapshot.getKey();
                    if (!uid.equals(currentUserId)) { // Loại trừ tài khoản của chính mình
                        User account = dataSnapshot.getValue(User.class);
                        if (account != null) {
                            accountAdapter.add(account);
                        }
                    }
                }
                accountAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi khi tìm kiếm: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void loadFriendDetails(String friendId) {
        usersRef = FirebaseDatabase.getInstance().getReference("users").child(friendId);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User friend = snapshot.getValue(User.class);
                if (friend != null) {
                    accountAdapter.add(friend);
                    accountAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi khi tải thông tin bạn bè: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
