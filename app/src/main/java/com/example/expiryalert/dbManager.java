package com.example.expiryalert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class dbManager extends SQLiteOpenHelper {
    private static final String DB_NAME = "reminder";
    private static final int DB_VERSION = 3;  // Incremented database version to 3

    public dbManager(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE tbl_reminder (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "expDate TEXT, " +
                "time TEXT, " +
                "addDate TEXT, " +
                "imagePath TEXT)";  // Added imagePath column
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE tbl_reminder RENAME TO temp_tbl_reminder");
            onCreate(db);
            db.execSQL("INSERT INTO tbl_reminder (id, title, expDate, time, addDate) SELECT id, title, date, time, addDate FROM temp_tbl_reminder");
            db.execSQL("DROP TABLE temp_tbl_reminder");
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE tbl_reminder ADD COLUMN imagePath TEXT");  // Add the new column
        }
    }

    public void deleteReminder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tbl_reminder", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateReminder(int id, String title, String expDate, String time, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("expDate", expDate);
        contentValues.put("time", time);
        contentValues.put("imagePath", imagePath);  // Update the imagePath
        db.update("tbl_reminder", contentValues, "id = ?", new String[]{String.valueOf(id)});
    }

    public String addReminder(String title, String expDate, String time, String imagePath) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("expDate", expDate);
        contentValues.put("addDate", getCurrentDateTime());
        contentValues.put("time", time);
        contentValues.put("imagePath", imagePath);  // Add the imagePath

        long result = database.insert("tbl_reminder", null, contentValues);

        if (result == -1) {
            return "Failed";
        } else {
            return "Successfully inserted";
        }
    }

    public Cursor readAllReminders() {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM tbl_reminder ORDER BY id DESC";
        return database.rawQuery(query, null);
    }

    // Helper method to get the current date and time in string format
    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
}

