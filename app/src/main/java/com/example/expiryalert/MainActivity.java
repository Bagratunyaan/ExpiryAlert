package com.example.expiryalert;

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.expiryalert.databinding.ActivityMainBinding;
import com.example.expiryalert.ui.dashboard.DashboardFragment;
import com.example.expiryalert.ui.home.HomeFragment;
import com.example.expiryalert.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ActivityMainBinding binding;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.navView.setSelectedItemId(R.id.navigation_home);
        navigateToFragment(new HomeFragment(), true);

        binding.navView.setOnItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                navigateToFragment(new HomeFragment(),false);
            } else if (itemId == R.id.navigation_dashboard) {
                navigateToFragment(new DashboardFragment(), false);
            } else if (itemId == R.id.navigation_notifications) {
                navigateToFragment(new NotificationsFragment(), false);
            }
            return true;
        });


        int nightModeFlags = getApplicationContext()
                .getResources()
                .getConfiguration()
                .uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                getWindow().getDecorView().setSystemUiVisibility(0);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public void navigateToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);

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