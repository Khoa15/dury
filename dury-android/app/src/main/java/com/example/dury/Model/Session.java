package com.example.dury.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class Session implements Parcelable {

    private String sessionId;
    private User user;
    private Date expirationDate;

    // Constructor, getters, setters, etc.
    public Session(User user) {
        this.user = user;
    }

    public Session(User user, String sessionId, Date expirationDate) {
        this.user = user;
        this.sessionId = sessionId;
        this.expirationDate = expirationDate;
    }

    public User getUser() {
        return user;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(sessionId);
        dest.writeParcelable((Parcelable) user, flags);
        dest.writeLong(expirationDate.getTime());
    }
    public static final Parcelable.Creator<Session> CREATOR = new Parcelable.Creator<Session>() {
        @Override
        public Session createFromParcel(Parcel source) {
            String sessionId = source.readString();
            User user = source.readParcelable(User.class.getClassLoader());
            Date expirationDate = new Date(source.readLong());
            return new Session(user, sessionId, expirationDate);
        }

        @Override
        public Session[] newArray(int size) {
            return new Session[size];
        }
    };
}