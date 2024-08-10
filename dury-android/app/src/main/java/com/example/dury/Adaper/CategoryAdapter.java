package com.example.dury.Adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dury.Model.Category;
import com.example.dury.R;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<Category> {
    int resource;
    Context context;
    ArrayList<Category> categories;
    public CategoryAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Category> categories) {
        super(context, resource, 0, categories);
        this.context = context;
        this.resource = resource;
        this.categories = categories;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Category category = categories.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(this.resource, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.txtViewCategoryTitle);
        textView.setText(category.getName());
        return convertView;
    }
}
