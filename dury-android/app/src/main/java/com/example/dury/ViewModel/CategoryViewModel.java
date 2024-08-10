package com.example.dury.ViewModel;

import com.example.dury.Model.Category;
import com.example.dury.Repository.ICategoryCallback;
import com.example.dury.Repository.CategoryRepository;

import java.util.ArrayList;

public class CategoryViewModel {
    static CategoryRepository categoryRepository = new CategoryRepository();
    public static ArrayList<Category> listCategories = new ArrayList<>();
    public static void load(ICategoryCallback iCategoryCallback, String userId, String token){
        categoryRepository.loadAllCategories(iCategoryCallback, userId, token);
    }

    public static void addCategory(Category category, String token, IApiCallback IApiCallback) {
        categoryRepository.addCategory(category, token, IApiCallback);
    }

    public static void updateCategory(Category category, String token, IApiCallback apiCallback) {
        categoryRepository.updateCategory(category, token, apiCallback);

    }

    public static void loadAllCategoriesByUid(String uid, String token, IApiCallback iApiCallback) {
        categoryRepository.loadAllCategoriesByUid(uid, token, iApiCallback);
    }

    public static void delete(Category category, String token, IApiCallback iApiCallback) {
        categoryRepository.delete(category, token, iApiCallback);
    }
}
