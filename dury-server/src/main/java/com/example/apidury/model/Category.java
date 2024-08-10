package com.example.apidury.model;

import lombok.Data;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Category {
    @Id
    private String id;
    @Unique
    private String name;
    private String color;
    @DBRef
    private List<Note> notes;
    @DBRef
    private User user;

    public Category(){}

    public Category(String name, String color, String userId) {
        this.name = name;
        this.color = color;
        this.user = new User();
        this.user.setId(userId);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



}
