package com.example.dury.Adaper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dury.Model.Note;
import com.example.dury.R;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private List<Note> data;

    public GridAdapter(List<Note> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtViewData.setText(data.get(position).getNote());
        holder.txtViewTitle.setText(data.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtViewData, txtViewTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtViewData = itemView.findViewById(R.id.txtData);
            txtViewTitle = itemView.findViewById(R.id.txtViewTitle);
        }
    }
    public void updateData(List<Note> newData) {
        this.data.clear();
        this.data.addAll(newData);
        notifyDataSetChanged();
    }
}