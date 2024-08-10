package com.example.dury.Adaper;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dury.Model.Category;
import com.example.dury.R;

import java.util.ArrayList;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder>
{
    private final RecycleViewInterface recycleViewInterface;
    ArrayList<Category> categoryArrayList;
    OnItemClickListener itemClickListener;
    public CategoryRecyclerViewAdapter(RecycleViewInterface recycleViewInterface, ArrayList<Category> categoryArrayList) {
        this.recycleViewInterface = recycleViewInterface;
        this.categoryArrayList = categoryArrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recyclerview_category_item, null);
        ViewHolder viewHolder = new ViewHolder(view, recycleViewInterface);
        viewHolder.txtViewCategoryTitle = view.findViewById(R.id.txtViewCategoryTitle);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryArrayList.get(position);
        holder.txtViewCategoryTitle.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtViewCategoryTitle;
        public ViewHolder(@NonNull View itemView, RecycleViewInterface recycleViewInterface) {
            super(itemView);
            txtViewCategoryTitle = itemView.findViewById(R.id.txtViewCategoryTitle);
            itemView.setOnClickListener(v ->{
                if(recycleViewInterface != null){
                    RecyclerView recyclerView = (RecyclerView) itemView.getParent();
                    int position = recyclerView.getChildAdapterPosition(itemView);
                    if (position != RecyclerView.NO_POSITION) {
                        recycleViewInterface.onItemClick(position);
                    }
                }
            });
        }
    }
}
