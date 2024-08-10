package com.example.dury.Service;


import com.example.dury.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @POST("/api/auth/signup")
    Call<User> addUser(@Body User user);

    @POST("/api/auth/signin")
    Call<User> login(@Body User user);

    @GET("/api/v1/users/{id}")
    Call<User> getUser(@Path("id") String id, @Header("Authorization") String token);

    @PUT("/api/v1/users")
    Call<User> updateBiometric(@Body User user, @Header("Authorization") String token);
}