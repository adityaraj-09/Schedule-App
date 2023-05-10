package com.example.timble;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class timble extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
