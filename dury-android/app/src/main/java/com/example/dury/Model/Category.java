package com.example.dury.Model;

import java.util.List;

public class Category {
    private String id;
    private String name;
    private List<Note> categoryNotes;
    private User user;

    // Constructor
    public Category(String name) {
        this.name = name;
    }
    public Category(){}

    // Getter và Setter cho id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter và Setter cho name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    // Getter và Setter cho categoryNotes
    public List<Note> getCategoryNotes() {
        return categoryNotes;
    }

    public void setCategoryNotes(List<Note> categoryNotes) {
        this.categoryNotes = categoryNotes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", categoryNotes=" + categoryNotes +
                ", user=" + user +
                '}';
    }


}
