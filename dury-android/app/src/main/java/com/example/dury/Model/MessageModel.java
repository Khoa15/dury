package com.example.dury.Model;

public class MessageModel
{
    public String messageID;
    public String senderID;
    public String message;
    private long timestamp;
    public MessageModel() {

    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public MessageModel(String messageID, String senderID, String message,long timestamp) {
        this.messageID = messageID;
        this.senderID = senderID;
        this.message = message;
        this.timestamp = timestamp;
    }
}