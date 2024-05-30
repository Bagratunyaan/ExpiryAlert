package com.example.expiryalert.ui.notifications;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.expiryalert.R;
import com.example.expiryalert.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    private Button signInBtn;
    private Button languageBtn;
    private Button logOutBtn;
    private TextView userEmailTextView;

    private static final String ARG_USER_EMAIL = "user_email";

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        signInBtn = root.findViewById(R.id.sign_in_btn);
        languageBtn = root.findViewById(R.id.change_language_btn);
        userEmailTextView = root.findViewById(R.id.user_email_text_view);
        logOutBtn = root.findViewById(R.id.logout_btn);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            userEmailTextView.setText(user.getEmail());
            signInBtn.setVisibility(View.GONE);
            logOutBtn.setVisibility(View.VISIBLE);
        } else {
            userEmailTextView.setText("User Email");
        }

        // Set click listeners
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the login activity
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });

        return root;
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        performLogout();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void performLogout() {
        FirebaseAuth.getInstance().signOut();
        userEmailTextView.setText("User Email");
        signInBtn.setVisibility(View.VISIBLE);
        logOutBtn.setVisibility(View.GONE);
    }

    public static ProfileFragment newInstance(String userEmail) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_EMAIL, userEmail);
        fragment.setArguments(args);
        return fragment;
    }
}




