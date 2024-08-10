package com.example.dury.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.dury.View.Category.CategoryFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {
    private String token;
    private String id;
    private String email;
    private String username;
    private String password;
    private boolean isBiometric;
    private List<Category> listCategories;
    private String accessToken;
    private boolean isFriend;

    public User(String id, String userName, String userEmail, String userPassword) {
        this.id = id;
        this.username = userName;
        this.email = userEmail;
        this.password = userPassword;
    }
    public User(String id, String userName, String userEmail, boolean isFriend) {
        this.id = id;
        this.username = userName;
        this.email = userEmail;
        this.isFriend = isFriend;
    }

    public User(User user) {
        this.id = user.id;
        this.username = user.username;
        this.email = user.email;
        this.password = user.password;
        this.isBiometric = user.isBiometric;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    // Constructor
    public User(String token, String username) {
        this.token = token;
        this.username = username;
    }



    public User(){}

    public User(String userId) {
        id = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Category> getListCategories() {
        return listCategories;
    }

    public void setListCategories(List<Category> listCategories) {
        this.listCategories = listCategories;
    }

    public void setBiometric(boolean biometric) {
        isBiometric = biometric;
    }
    public boolean isBiometric() {
        return isBiometric;
    }
    public void setUseBiometric(boolean isBiometric) {
         this.isBiometric = isBiometric;
    }
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", listCategories=" + listCategories +
                '}';
    }
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
