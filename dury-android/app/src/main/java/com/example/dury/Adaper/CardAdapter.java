package com.example.dury.Adaper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dury.Model.CardItem;
import com.example.dury.R;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<CardItem> cardItems;

    public CardAdapter(List<CardItem> cardItems) {
        this.cardItems = cardItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardItem item = cardItems.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.description.setChecked(item.isChecked()); // Set trạng thái của CheckBox
        holder.description.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setChecked(isChecked); // Lưu trạng thái vào đối tượng CardItem
        });
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    public void moveItem(int fromPosition, int toPosition) {
        CardItem item = cardItems.remove(fromPosition);
        cardItems.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    public CardItem removeItem(int position) {
        CardItem removedItem = cardItems.remove(position);
        notifyItemRemoved(position);
        return removedItem;
    }

    public void addItem(CardItem item, int position) {
        cardItems.add(position, item);
        notifyItemInserted(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public CheckBox description;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_title);
            description = itemView.findViewById(R.id.text_description);
        }
    }
}
