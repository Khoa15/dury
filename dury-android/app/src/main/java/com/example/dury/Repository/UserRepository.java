package com.example.dury.Repository;

import com.example.dury.Model.User;
import com.example.dury.Network.RetrofitClient;
import com.example.dury.Service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final UserService userService;

    public UserRepository() {
        userService = RetrofitClient.getClient().create(UserService.class);
    }

    // Hoa
    public void addUser(User user, final IUserCallback callback) {// Register
        Call<User> call = userService.addUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Throwable("Error"));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    // Khoa
    public void login(User user, IUserCallback callback){
        Call<User> call = userService.login(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Throwable("Failed!"));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public void getUser(String userId, String token, IUserCallback userCallback) {
        Call<User> call = userService.getUser(userId, token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    userCallback.onSuccess(response.body());
                } else {
                    userCallback.onFailure(new Throwable("Failed!"));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                userCallback.onFailure(t);
            }
        });
    }

    public void updateBiometric(User user, String token, boolean checked, IUserCallback userCallback) {
        Call<User> call = userService.updateBiometric(user, token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    userCallback.onSuccess(response.body());
                } else {
                    userCallback.onFailure(new Throwable("Failed!"));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                userCallback.onFailure(t);
            }
        });
    }
}
