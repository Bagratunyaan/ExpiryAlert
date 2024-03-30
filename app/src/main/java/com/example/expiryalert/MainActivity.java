package com.example.expiryalert;

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expiryalert.databinding.ActivityMainBinding;
import com.example.expiryalert.ui.dashboard.DashboardFragment;
import com.example.expiryalert.ui.home.HomeFragment;
import com.example.expiryalert.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ActivityMainBinding binding;

    private ImageView imageView;

    FloatingActionButton mCreateRem;
    RecyclerView mRecyclerview;
    MyAdapter mAdapter;

    CollectionReference remindersRef;
    ArrayList<Model> mDataList = new ArrayList<>(); // ArrayList to hold reminders

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

                                                                                                    mRecyclerview = findViewById(R.id.recyclerView);
                                                                                                    mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
                                                                                                    mAdapter = new MyAdapter(mDataList);
                                                                                                    mRecyclerview.setAdapter(mAdapter);
                                                                                                    mCreateRem = (FloatingActionButton) findViewById(R.id.create_reminder);
                                                                                                    mCreateRem.setOnClickListener(new View.OnClickListener() {
                                                                                                        @Override
                                                                                                        public void onClick(View view) {
                                                                                                            Intent intent = new Intent(getApplicationContext(), ReminderActivity.class);
                                                                                                            startActivity(intent);                                                              //Starts the new activity to add Reminders
                                                                                                        }
                                                                                                    });


        binding.navView.setSelectedItemId(R.id.navigation_home);
        navigateToFragment(new HomeFragment(), true);

        binding.navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                navigateToFragment(new HomeFragment(), false);
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



        loadRemindersFromFirebase();
    }

    private void loadRemindersFromFirebase() {
        Query query = FirebaseDatabase.getInstance().getReference("reminders");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Model model = snapshot.getValue(Model.class);
                    if (model != null) {
                        mDataList.add(model);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to load reminders", Toast.LENGTH_SHORT).show();
            }
        });
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
