package com.example.expiryalert;

//model class is used to set and get the data from the database

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class Model {
    private String title;
    private String expDate;
    private String time;
    private int id;
    private String addDate;
    private String imagePath;
    private Bitmap imageBitmap;

    public Model(String title, String expDate, String time, int id, String addDate, String imagePath) {
        this.title = title;
        this.expDate = expDate;
        this.time = time;
        this.id = id;
        this.addDate = addDate;
        this.imagePath = imagePath;
        this.imageBitmap = loadImageFromStorage(imagePath);
    }

    private Bitmap loadImageFromStorage(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                return BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }
}