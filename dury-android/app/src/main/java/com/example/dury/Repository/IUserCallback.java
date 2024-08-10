package com.example.dury.Repository;

import com.example.dury.Model.User;

public interface IUserCallback {
    void onSuccess(User user);
    void onFailure(Throwable message);
}
