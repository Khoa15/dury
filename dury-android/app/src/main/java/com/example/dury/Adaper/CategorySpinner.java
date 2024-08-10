package com.example.dury.Adaper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dury.Model.Category;
import com.example.dury.R;

import java.util.ArrayList;

public class CategorySpinner extends BaseAdapter {
    ArrayList<Category> listCategories;
    LayoutInflater activity;
    int layoutItem;
    public CategorySpinner(Activity activity, int layoutItem, ArrayList<Category> listCategories) {
        this.activity = activity.getLayoutInflater();
        this.layoutItem = layoutItem;
        this.listCategories = listCategories;
    }

    @Override
    public int getCount() {
        return listCategories.size();
    }

    @Override
    public Object getItem(int position) {
        return listCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category category = listCategories.get(position);
        View view = activity.inflate(layoutItem, null, true);

        ((TextView)view.findViewById(R.id.txtViewCategorySpinnerTitle)).setText(category.getName());

        return view;
    }
}
