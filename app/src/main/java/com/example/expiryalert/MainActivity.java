package com.example.expiryalert;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expiryalert.databinding.ActivityMainBinding;
import com.example.expiryalert.ui.dashboard.DashboardFragment;
import com.example.expiryalert.ui.home.HomeFragment;
import com.example.expiryalert.ui.notifications.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CODE_NOTIFICATION_PERMISSION = 2;
    private BottomNavigationView bottomNavigationView;
    private ActivityMainBinding binding;

    private ImageView imageView;

    RecyclerView mRecyclerview;
    myAdapter mAdapter;

    CollectionReference remindersRef;
    ArrayList<Model> mDataList = new ArrayList<>(); // ArrayList to hold reminders

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, new ProfileFragment())
                .commit();

        mRecyclerview = findViewById(R.id.recyclerView);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        bottomNavigationView = findViewById(R.id.nav_view);

        binding.navView.setSelectedItemId(R.id.navigation_home);
        navigateToFragment(new HomeFragment(), true);

        binding.navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                navigateToFragment(new HomeFragment(), false);
            } else if (itemId == R.id.navigation_dashboard) {
                navigateToFragment(new DashboardFragment(), false);
            } else if (itemId == R.id.navigation_notifications) {
                navigateToFragment(new ProfileFragment(), false);
            }
            return true;
        });

        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions() {
        // Check and request READ_EXTERNAL_STORAGE permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);
        }

        // Check and request POST_NOTIFICATIONS permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Check if the API level supports the permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                // Show rationale if needed
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                    new AlertDialog.Builder(this)
                            .setTitle("Notification Permission Needed")
                            .setMessage("This app needs the Notification permission to notify you about important updates.")
                            .setPositiveButton("OK", (dialog, which) ->
                                    ActivityCompat.requestPermissions(this,
                                            new String[]{Manifest.permission.POST_NOTIFICATIONS},
                                            REQUEST_CODE_NOTIFICATION_PERMISSION))
                            .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                            .create()
                            .show();
                } else {
                    // Request the permission directly
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.POST_NOTIFICATIONS},
                            REQUEST_CODE_NOTIFICATION_PERMISSION);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
//                Toast.makeText(this, "READ_EXTERNAL_STORAGE permission granted", Toast.LENGTH_SHORT).show();
                // You can now access external storage
            } else {
                // Permission denied
//                Toast.makeText(this, "READ_EXTERNAL_STORAGE permission denied", Toast.LENGTH_SHORT).show();
                // Handle the case when permission is denied
            }
        } else if (requestCode == REQUEST_CODE_NOTIFICATION_PERMISSION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                Toast.makeText(this, "POST_NOTIFICATIONS permission granted", Toast.LENGTH_SHORT).show();
                // You can now send notifications
            } else {
                // Permission denied
                Toast.makeText(this, "POST_NOTIFICATIONS permission denied", Toast.LENGTH_SHORT).show();
                // Handle the case when permission is denied
            }
        }
    }

    public void navigateToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Set custom animations
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
