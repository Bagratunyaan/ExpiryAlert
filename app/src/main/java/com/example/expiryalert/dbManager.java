package com.example.expiryalert;

import android.app.NotificationManager;
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
    private static final int DB_VERSION = 2;

    public dbManager(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE tbl_reminder (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "expDate TEXT, " +  // Change the column name to expDate
                "time TEXT, " +  // Add the new column addDate
                "addDate TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Perform the necessary migration if upgrading from version 1 to version 2
            db.execSQL("ALTER TABLE tbl_reminder RENAME TO temp_tbl_reminder");
            onCreate(db);
            db.execSQL("INSERT INTO tbl_reminder (id, title, expDate, time) SELECT id, title, date, time FROM temp_tbl_reminder");
            db.execSQL("DROP TABLE temp_tbl_reminder");
        }
    }

    public void deleteReminder(int id) {
//        cancelNotification(context, id);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tbl_reminder", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    private void cancelNotification(Context context, int id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }

    public String addreminder(String title, String expdate, String time) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("expDate", expdate);  // Use the new column name expDate
        contentValues.put("addDate", getCurrentDateTime()); // Set the current date and time
        contentValues.put("time", time);

        long result = database.insert("tbl_reminder", null, contentValues);

        if (result == -1) {
            return "Failed";
        } else {
            return "Successfully inserted";
        }
    }

    public Cursor readallreminders() {
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
