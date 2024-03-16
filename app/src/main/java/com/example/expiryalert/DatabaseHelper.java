package com.example.expiryalert;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.expiryalert.ui.notifications.NotificationsFragment;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "image_database.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    static final String TABLE_NAME = "images";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_IMAGE = "image_data";

    // SQL statement to create the table
    static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_IMAGE + " TEXT" +
            ")";

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

