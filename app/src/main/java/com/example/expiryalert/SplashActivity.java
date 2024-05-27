package com.example.expiryalert;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                // Use fade in and zoom out animations
//                overridePendingTransition(R.anim.fade_in, R.anim.zoom_out);
                finish();
            }
        }, secondsDelayed * 1000); // Delay in milliseconds
    }
}
