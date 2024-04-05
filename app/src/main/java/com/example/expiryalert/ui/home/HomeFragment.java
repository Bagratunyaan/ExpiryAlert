package com.example.expiryalert.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expiryalert.MainActivity;
import com.example.expiryalert.Model;
import com.example.expiryalert.R;
import com.example.expiryalert.ReminderActivity;
import com.example.expiryalert.databinding.FragmentHomeBinding;
import com.example.expiryalert.dbManager;
import com.example.expiryalert.myAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    FloatingActionButton mCreateRem;
    RecyclerView mRecyclerview;
    ArrayList<Model> dataholder = new ArrayList<Model>();                                           //Array list to add reminders and display in recyclerview
    myAdapter adapter;
    ImageButton btnFilter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btnFilter = root.findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterMenu();
            }
        });


        mRecyclerview = (RecyclerView) root.findViewById(R.id.recyclerView);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mCreateRem = (FloatingActionButton) root.findViewById(R.id.create_reminder);                //Floating action button to change activity
        mCreateRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ReminderActivity.class);
                startActivity(intent);                                                              //Starts the new activity to add Reminders
            }
        });

        Cursor cursor = new dbManager(getContext()).readallreminders();
        while (cursor != null && cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndexOrThrow("id");
            int titleIndex = cursor.getColumnIndexOrThrow("title");
            int expDateIndex = cursor.getColumnIndexOrThrow("expDate");
            int timeIndex = cursor.getColumnIndexOrThrow("time");
            int addDataIndex = cursor.getColumnIndexOrThrow("addDate");


            Model model = new Model(
                    cursor.getString(titleIndex),
                    cursor.getString(expDateIndex),
                    cursor.getString(timeIndex),
                    cursor.getInt(idIndex),
                    cursor.getString(addDataIndex));
            dataholder.add(model);
        }

        adapter = new myAdapter(getActivity(), dataholder);
        mRecyclerview.setAdapter(adapter);                                                          //Binds the adapter with recyclerview
        return root;
    }

    private void showFilterMenu() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), btnFilter);
        popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item click
                if (item.getItemId() == R.id.sortAZ) {
                    // Sort by title A-Z
                    ((myAdapter) Objects.requireNonNull(mRecyclerview.getAdapter())).sortByTitle();
                } else if (item.getItemId() == R.id.sortZA) {
                    // Sort by title Z-A
                    ((myAdapter) Objects.requireNonNull(mRecyclerview.getAdapter())).sortByTitleDescending();
                } else if (item.getItemId() == R.id.sortExpDateAsc) {
                    // Sort by expiration date ascending
                    ((myAdapter) Objects.requireNonNull(mRecyclerview.getAdapter())).sortByExpDate();
                } else if (item.getItemId() == R.id.sortExpDateDesc) {
                    // Sort by expiration date descending
                    ((myAdapter) Objects.requireNonNull(mRecyclerview.getAdapter())).sortByExpDateDescending();
                } else if (item.getItemId() == R.id.sortAddDateAsc) {
                    // Sort by adding date ascending
                    ((myAdapter) Objects.requireNonNull(mRecyclerview.getAdapter())).sortByAddDate();
                } else if (item.getItemId() == R.id.sortAddDateDesc) {
                    // Sort by adding date descending
                    ((myAdapter) Objects.requireNonNull(mRecyclerview.getAdapter())).sortByAddDateDescending();
                }
                return true;
            }
        });
        popupMenu.show();
    }
//
//    @Override
//    public void onBackPressed() {
//        getActivity().finish();                                                                   //Makes the user to exit from the app
//        super.onBackPressed();
//
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}