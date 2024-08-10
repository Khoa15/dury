package com.example.dury.Model;

import java.time.LocalDateTime;

public class Notification {
    private String id;
    private LocalDateTime notifyAt;
    private int loop;

    // Constructor
    public Notification(LocalDateTime notifyAt, int loop) {
        this.notifyAt = notifyAt;
        this.loop = loop;
    }

    public Notification() {}

    // Getter và Setter cho id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter và Setter cho notifyAt
    public LocalDateTime getNotifyAt() {
        return notifyAt;
    }

    public void setNotifyAt(LocalDateTime notifyAt) {
        this.notifyAt = notifyAt;
    }

    // Getter và Setter cho loop
    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }

    // Phương thức toString để trả về thông tin chi tiết của Notification
    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", notifyAt=" + notifyAt +
                ", loop=" + loop +
                '}';
    }


}
