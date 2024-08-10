package com.example.dury.Repository;

import com.example.dury.Model.Category;

import java.util.List;

public interface ICategoryCallback {
    void onSuccess(List<Category> listCategories);
    void onFailure(Throwable message);
}
