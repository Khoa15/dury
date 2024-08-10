package com.example.dury.ViewModel;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import com.example.dury.Model.SessionManager;
import com.example.dury.Model.User;
import com.example.dury.Repository.UserRepository;
import com.example.dury.Repository.IUserCallback;

public class UserViewModel {
    static User user;
    static UserRepository userRepository = new UserRepository();

    public static void login(User user, IUserCallback userCallback){
        userRepository.login(user, userCallback);
    }
    public static void signup(User user, IUserCallback callback){
        IUserCallback iUserCallback = new IUserCallback() {
            @Override
            public void onSuccess(User user) {
                callback.onSuccess(user);
            }

            @Override
            public void onFailure(Throwable message) {
                callback.onFailure(message);
            }
        };
        userRepository.addUser(user, iUserCallback);
    }
    public static boolean logout(){
        SessionManager.getInstance().logoutUser();
        return true;
    }

    public static void getUser(String userId, String token, IUserCallback userCallback) {
        userRepository.getUser(userId, token, userCallback);
    }

    public static void updateBiometric(User user, String token, boolean checked, IUserCallback userCallback) {
        userRepository.updateBiometric(user, token, checked, userCallback);
    }
}
