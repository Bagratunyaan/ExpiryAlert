package com.example.expiryalert;

//model class is used to set and get the data from the database

public class Model {
    String title, expDate, time, addDate;
    private int id;


    public Model(String title, String expDate, String time, int id, String addDate) {
        this.title = title;
        this.expDate = expDate;
        this.time = time;
        this.id = id;
        this.addDate = addDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }
}