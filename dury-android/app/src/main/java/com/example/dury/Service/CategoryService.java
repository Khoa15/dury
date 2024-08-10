package com.example.dury.Service;

import com.example.dury.Model.Category;
import com.example.dury.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CategoryService {

    @GET("/api/v1/categories")
    Call<List<Category>> loadAll();
    @GET("/api/v1/categories/u/{id}")
    Call<List<Category>> loadAllByUser(@Path("id")String id, @Header("Authorization") String token);// Token Access

    @GET("/api/v1/categories/{id}")
    Call<Category> getCategory(@Path("id")String id, @Header("Authorization") String token);

    @PUT("/api/v1/categories")
    Call<Category> updateCategory(@Body Category category, @Header("Authorization") String token);

    @DELETE("/api/v1/categories/{id}")
    Call<Void> deleteCategory(@Path("id") String id, @Header("Authorization") String token);

    @POST("/api/v1/categories")
    Call<Category> addCategory(@Body Category category, @Header("Authorization") String token);

}
