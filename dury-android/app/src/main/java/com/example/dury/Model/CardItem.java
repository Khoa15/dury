package com.example.dury.Model;

public class CardItem {
    private String title;
    private String description;
    private boolean isChecked; // Thêm thuộc tính này

    public CardItem(String title, String description) {
        this.title = title;
        this.description = description;
        this.isChecked = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
