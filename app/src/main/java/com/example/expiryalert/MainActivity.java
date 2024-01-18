 package com.example.expiryalert;

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

 public class MainActivity extends AppCompatActivity {

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         View decor = getWindow().getDecorView();
         if (decor.getSystemUiVisibility() != SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){

         } else {

         }

        int nightModeFlags = getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                 getWindow().getDecorView().setSystemUiVisibility(0);
                 break;
            case Configuration.UI_MODE_NIGHT_NO:
                 getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}