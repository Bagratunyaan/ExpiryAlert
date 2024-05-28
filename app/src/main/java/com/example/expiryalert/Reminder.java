package com.example.expiryalert;

public class Reminder {
    private String id;
    private String title;
    private String date;
    private String time;
    private String imagePath;
//
//    public Reminder() {
//        // Default constructor required for calls to DataSnapshot.getValue(Reminder.class)
//    }

    public Reminder(String id, String title, String date, String time, String imagePath) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.imagePath = imagePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}

