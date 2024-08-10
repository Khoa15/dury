package com.example.dury.Adaper;

import android.app.Activity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private ArrayList<User> friendList;
    private Context context;

    public FriendAdapter(Context context, ArrayList<User> friendList) {
        this.context = context;
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row_accept, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        User friend = friendList.get(position);
        holder.textViewUsername.setText(friend.getUsername());
        holder.textViewEmail.setText(friend.getEmail());

        // Ẩn hoặc hiển thị nút "Kết bạn" dựa trên giá trị của trường isFriend
        if (friend.isFriend()) {
            holder.buttonAccept.setVisibility(View.GONE);
        } else {
            holder.buttonAccept.setVisibility(View.VISIBLE);
            holder.buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptFriendRequest(friend.getId());
                    Toast.makeText(v.getContext(), "Yêu cầu kết bạn đã được chấp nhận", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent();
//                    ((Activity) context).setResult(MainActivity2.RESULT_OK, intent);
//                    ((Activity) context).finish();
                }
            });
        }
    }
    private void acceptFriendRequest(String userId) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference currentUserFriendsRef = FirebaseDatabase.getInstance().getReference().child("friends").child(currentUserId);
        DatabaseReference friendUserFriendsRef = FirebaseDatabase.getInstance().getReference().child("friends").child(userId);

        // Thêm bạn cho người dùng hiện tại
        currentUserFriendsRef.child(userId).setValue(true);
        // Thêm người dùng hiện tại vào danh sách bạn bè của bạn
        friendUserFriendsRef.child(currentUserId).setValue(true);

        // Xóa yêu cầu kết bạn khỏi cơ sở dữ liệu
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference().child("friend_requests").child(currentUserId);
        currentUserRef.child(userId).removeValue();

        // Xóa yêu cầu kết bạn của bạn khỏi cơ sở dữ liệu (nếu có)
        DatabaseReference friendUserRef = FirebaseDatabase.getInstance().getReference().child("friend_requests").child(userId);
        friendUserRef.child(currentUserId).removeValue();

        // Cập nhật trường isFriend của cả hai tài khoản
        DatabaseReference currentUserAccountRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
        currentUserAccountRef.child("isFriend").setValue(true);

        DatabaseReference friendAccountRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        friendAccountRef.child("isFriend").setValue(true);
    }


    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewUsername, textViewEmail;
        public Button buttonAccept;

        public FriendViewHolder(View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.username);
            textViewEmail = itemView.findViewById(R.id.useremail);
            buttonAccept = itemView.findViewById(R.id.btnchapnhan);
        }
    }
}
