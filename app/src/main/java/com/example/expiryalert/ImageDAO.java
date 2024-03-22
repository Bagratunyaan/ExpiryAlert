//package com.example.expiryalert;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//
//import com.example.expiryalert.ui.notifications.NotificationsFragment;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//
//public class ImageDAO {
//    private SQLiteDatabase database;
//    private DatabaseHelper dbHelper;
//
//    public ImageDAO(Context context) {
//        dbHelper = new DatabaseHelper(context);
//    }
//
//    public void open() throws SQLException {
//        database = dbHelper.getWritableDatabase();
//    }
//
//    public void close() {
//        dbHelper.close();
//    }
//
//    public void insertImage(Bitmap image) {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//
//        ContentValues values = new ContentValues();
//        values.put(DatabaseHelper.COLUMN_IMAGE, byteArray);
//        database.insert(DatabaseHelper.TABLE_NAME, null, values);
//    }
//
//    public Bitmap retrieveImage() {
//        Bitmap bitmap = null;
//        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, new String[]{DatabaseHelper.COLUMN_IMAGE},
//                null, null, null, null, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE));
//            if (imageByteArray != null) {
//                ByteArrayInputStream imageStream = new ByteArrayInputStream(imageByteArray);
//                bitmap = BitmapFactory.decodeStream(imageStream);
//            }
//            cursor.close();
//        }
//        return bitmap;
//    }
//}
