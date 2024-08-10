package com.example.dury.ViewModel;

public interface IApiCallback {
    void onSuccess(Object object);
    void onFailure(Throwable t);
    void onFailure(Exception e);
}
