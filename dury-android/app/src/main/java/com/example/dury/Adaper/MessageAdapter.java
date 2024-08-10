package com.example.dury.Adaper;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;



import com.example.dury.Model.MessageModel;
import com.example.dury.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private  static  final int VIEW_TYPE_SENT = 1;
    private  static  final int VIEW_TYPE_RECEIVED = 2;
    private Context context;
    private List<MessageModel> messageModelList;
    private String senderRoom;
    private String receivedRoom;

    public MessageAdapter(Context context, String senderRoom, String receivedRoom) {
        this.context = context;
        this.messageModelList = new ArrayList<>();
        this.senderRoom = senderRoom;
        this.receivedRoom = receivedRoom;
    }

    public void add(MessageModel messageModel){
        messageModelList.add(messageModel);
    }

    public void clear(){
        messageModelList.clear();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == VIEW_TYPE_SENT) {
            view = inflater.inflate(R.layout.message_row_sent, parent, false);
        } else {
            view = inflater.inflate(R.layout.message_row_received, parent, false);
        }
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {
        MessageModel messageModel = messageModelList.get(position);

        if(messageModel.getSenderID().equals(FirebaseAuth.getInstance().getUid())) {
            // Tin nhắn của người gửi: Hiển thị bên phải
            // holder.textViewSendMess.setVisibility(View.VISIBLE);
            //  holder.textViewReceivedMess.setVisibility(View.GONE);
            holder.textViewSendMess.setText(messageModel.getMessage());
        } else {
            // Tin nhắn của người nhận: Hiển thị bên trái
            // holder.textViewReceivedMess.setVisibility(View.VISIBLE);
            //holder.textViewSendMess.setVisibility(View.GONE);
            holder.textViewReceivedMess.setText(messageModel.getMessage());
        }

//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                // Hiển thị hộp thoại hoặc menu ngữ cảnh
//                showContextMenu(messageModel);
//                return true;
//            }
//        });

    }
//    private void showContextMenu(MessageModel message) {
//        // Tạo hộp thoại hoặc menu ngữ cảnh để cho người dùng lựa chọn hành động
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Chia sẻ tin nhắn");
//        builder.setMessage("Bạn có muốn chia sẻ tin nhắn này với bạn bè không?");
//        builder.setPositiveButton("Chia sẻ", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                shareMessageWithFriend(message);
//            }
//        });
//        builder.setNegativeButton("Hủy", null);
//        builder.show();
//    }
//    private void shareMessageWithFriend(MessageModel message) {
//
//        String friendId = "";
//        String messageContent = message.getMessage();
//        shareMessageWithFriend(friendId, messageContent);
//    }
//
//    private void shareMessageWithFriend(String friendId, String message) {
//
//        String messageID = UUID.randomUUID().toString(); // Tạo ID tin nhắn
//        long timestamp = System.currentTimeMillis(); // Lấy thời gian hiện tại
//        MessageModel messageModel = new MessageModel(messageID, FirebaseAuth.getInstance().getUid(), message, timestamp);
//
//        // Thêm tin nhắn vào node của người gửi
//        DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom).child(messageID);
//        senderRef.setValue(messageModel);
//
//        // Thêm tin nhắn vào node của người nhận
//        DatabaseReference receiverRef = FirebaseDatabase.getInstance().getReference("chats").child(receivedRoom).child(messageID);
//        receiverRef.setValue(messageModel);
//
//        // Hiển thị thông báo cho người dùng
//        Toast.makeText(context, "Tin nhắn đã được chia sẻ với bạn bè.", Toast.LENGTH_SHORT).show();
//    }

    @Override
    public int getItemViewType(int position) {
        if(messageModelList.get(position).getSenderID().equals(FirebaseAuth.getInstance().getUid()))
        {
            return VIEW_TYPE_SENT;
        }else{
            return VIEW_TYPE_RECEIVED;
        }

    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }
    public List<MessageModel> getMessageModelList(){
        return  messageModelList;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewSendMess,textViewReceivedMess;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSendMess = itemView.findViewById(R.id.textViewSentMessage);
            textViewReceivedMess = itemView.findViewById(R.id.textViewReceivedMessage);
        }
    }
}