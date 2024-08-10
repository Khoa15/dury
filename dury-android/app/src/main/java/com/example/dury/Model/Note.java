package com.example.dury.Model;

import android.os.Build;
import android.provider.MediaStore;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Note {
    private String id;
    private String title;
    private String note;
    private int status;
    private int priority;
    private int minute;
    private int hour;
    private int day;
    private int month;
    private int year;
    private Category category;
    private static String[] priorityList = {"High", "Medium", "Low"};

    public Note(
            String title,
            String strNote,
            int indexPriority,
            Category category,
            int minute,
            int hour,
            int day,
            int month,
            int year
    ) {// test đitest gi á cho cuong test à
        this.title = title;
        this.note = strNote;
        this.priority = priority;
        this.category = category;
        this.minute = minute;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public static String[] getPriorityList() {
        return priorityList;
    }

    // Constructor


    public int getStatus() {
        return status;//làm đi
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Note(String id, String title, String note, int priority, Category category) throws NoSuchAlgorithmException {
        this.id = id;
        this.title = title;
        this.note = note;
        this.priority = priority;
        this.category = category;
    }

    public Note(String note, int priority, Category category) throws NoSuchAlgorithmException {
        this.note = note;
        this.priority = priority;
        this.category = category;
    }
    public Note() throws NoSuchAlgorithmException {}

    // Getters and Setters
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        if(note == null) return "";
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    // toString method
    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", note='" + note + '\'' +
                ", priority=" + priority +
                //", category=" + category +
                //", notification=" + notification +
                '}';
    }


    public String getTitle() {
        if(title == null) return "";
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public static SecretKey secretKey(int n) throws NoSuchAlgorithmException {
//        KeyGenerator keyGenerator= KeyGenerator.getInstance("AES");
//        keyGenerator.init(n);
//        return (SecretKey) keyGenerator;
//    }
//
//    public static IvParameterSpec generateIv() {
//        byte[] iv = new byte[16];
//        new java.security.SecureRandom().nextBytes(iv);
//        return new IvParameterSpec(iv);
//    }
//    static SecretKey key;
//
//    static {
//        try {
//            key = secretKey(128);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    static IvParameterSpec iv=generateIv();
//
//     String encrypNote() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
//        Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
//        cipher.init(Cipher.ENCRYPT_MODE,key,iv);
//        byte[] cipherText=cipher.doFinal(getNote().getBytes());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            return Base64.getEncoder().encodeToString(cipherText);
//        }
//        return "";
//    }
//    public  String decrypt( ) throws NoSuchPaddingException, NoSuchAlgorithmException,
//            InvalidAlgorithmParameterException, InvalidKeyException,
//            BadPaddingException, IllegalBlockSizeException {
//
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        cipher.init(Cipher.DECRYPT_MODE, key,iv);
//        byte[] plainText = cipher.doFinal(Base64.getDecoder()
//                .decode(encrypNote()));
//        return new String(plainText);
//    }
}
