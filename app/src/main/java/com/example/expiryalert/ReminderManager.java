package com.example.expiryalert;
import android.content.Context;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReminderManager {
    private DatabaseReference remindersRef;

    public ReminderManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        remindersRef = database.getReference("reminders");
    }

    public void addReminder(String title, String date, String time) {
        String key = remindersRef.push().getKey(); // Generating unique key for each reminder
        Reminder reminder = new Reminder(key, title, date, time);
        remindersRef.child(key).setValue(reminder); // Adding reminder to Firebase
    }

    public DatabaseReference getRemindersRef() {
        return remindersRef;
    }
}
