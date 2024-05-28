package com.example.expiryalert;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReminderManager {
    private CollectionReference remindersRef;

    public ReminderManager() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        remindersRef = firestore.collection("reminders");
    }

    public void addReminder(String title, String date, String time, String imagePath) {
        String key = remindersRef.document().getId(); // Generating unique key for each reminder
        Reminder reminder = new Reminder(key, title, date, time, imagePath);
        remindersRef.document(key).set(reminder); // Adding reminder to Firestore
    }

    public CollectionReference getRemindersRef() {
        return remindersRef;
    }
}
