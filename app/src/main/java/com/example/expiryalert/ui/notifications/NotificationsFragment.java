package com.example.expiryalert.ui.notifications;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.expiryalert.R;
import com.example.expiryalert.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {
    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    private FragmentNotificationsBinding binding;
    private ImageView imageView;
    private Button importImageButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imageView = binding.imageView;
        importImageButton = binding.importImageButton;

//        ImageDAO imageDAO = new ImageDAO(requireContext()); // Use requireContext() for the fragment's context

        importImageButton.setOnClickListener(v -> showImagePickerDialog());

//        imageDAO.open();

        // Insert an example image into the database
        Bitmap exampleImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        if (exampleImage != null) {
//            imageDAO.insertImage(exampleImage);
        }

        // Retrieve and display the image from the database
//        Bitmap retrievedImage = imageDAO.retrieveImage();
//        if (retrievedImage != null) {
//            imageView.setImageBitmap(retrievedImage);
//        }
//        imageDAO.close();

        return root;
    }

    private void showImagePickerDialog() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImageIntent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
