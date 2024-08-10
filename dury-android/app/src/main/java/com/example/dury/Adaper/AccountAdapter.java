package com.example.dury.Adaper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dury.Model.User;
import com.example.dury.R;
import com.example.dury.View.User.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<User> accountList;
    public AccountAdapter(Context context) {
        this.context = context;
        this.accountList = new ArrayList<>();
    }
    public void add(User account){
        accountList.add(account);
    }

    public void clear(){
        accountList.clear();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AccountAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountAdapter.MyViewHolder holder, int position) {
        User account = accountList.get(position);
        holder.name.setText(account.getUsername());
        holder.email.setText(account.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("id",account.getId());
                intent.putExtra("name",account.getUsername());
                context.startActivity(intent);
            }
        });

        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference().child("friends").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        friendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.child(account.getId()).exists()) {
                    holder.addButton.setVisibility(View.GONE); // Ẩn nút kết bạn nếu là bạn bè
                } else {
                    holder.addButton.setVisibility(View.VISIBLE); // Hiển thị nút kết bạn nếu không phải là bạn bè
                    holder.addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendFriendRequest(account.getId());
                            Toast.makeText(v.getContext(), "Đã gửi yêu cầu kết bạn cho " + account.getUsername(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }
    private void sendFriendRequest(String userId) {

        DatabaseReference recipientRef = FirebaseDatabase.getInstance().getReference().child("friend_requests").child(userId);
        recipientRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
    }
    @Override
    public int getItemCount() {
        return accountList.size();
    }
    public ArrayList<User> getAccountList(){
        return  accountList;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name,email;
        Button addButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            email = itemView.findViewById(R.id.useremail);
            addButton = itemView.findViewById(R.id.addButton);
        }
    }
}
