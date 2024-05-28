package com.example.expiryalert.ui.home;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expiryalert.Model;
import com.example.expiryalert.R;
import com.example.expiryalert.ReminderActivity;
import com.example.expiryalert.databinding.FragmentHomeBinding;
import com.example.expiryalert.dbManager;
import com.example.expiryalert.myAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    FloatingActionButton mCreateRem;
    RecyclerView mRecyclerview;
    ArrayList<Model> dataholder = new ArrayList<>();
    myAdapter adapter;
    ImageButton btnFilter;
    TextView noResultsTextView;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PHOTO_REQUEST = 2;
    private Uri imageUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnFilter = root.findViewById(R.id.btnFilter);
        noResultsTextView = root.findViewById(R.id.noResultsTextView);
        mRecyclerview = root.findViewById(R.id.recyclerView);
        mCreateRem = root.findViewById(R.id.create_reminder);

        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new myAdapter(getActivity(), dataholder);
        mRecyclerview.setAdapter(adapter);

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterMenu();
            }
        });

        mCreateRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ReminderActivity.class);
                startActivity(intent);
            }
        });

        loadReminders();

        SearchView searchView = root.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                if (newText.isEmpty()) {
                    btnFilter.setVisibility(View.VISIBLE);
                    checkIfNoResults();
                } else {
                    btnFilter.setVisibility(View.GONE);
                    noResultsTextView.setVisibility(View.GONE);
                }
                return true;
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            private final ColorDrawable background = new ColorDrawable(Color.rgb(255, 32, 78));
            private final Drawable deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_delete_outline_24);
            private final Drawable editIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_edit_24);
            private final ColorDrawable editBackground = new ColorDrawable(Color.rgb(200, 130, 239));

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    showDeleteConfirmationDialog(position);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    showEditDialog(position);
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20;

                int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                int iconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();

                if (dX < 0) { // Swiping to the left
                    int iconLeft = itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth();
                    int iconRight = itemView.getRight() - iconMargin;
                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                    background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                            itemView.getTop(), itemView.getRight(), itemView.getBottom());
                    background.draw(c);
                    deleteIcon.draw(c);
                } else if (dX > 0) { // Swiping to the right
                    int iconLeft = itemView.getLeft() + iconMargin;
                    int iconRight = itemView.getLeft() + iconMargin + editIcon.getIntrinsicWidth();
                    editIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                    editBackground.setBounds(itemView.getLeft(), itemView.getTop(),
                            itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
                    editBackground.draw(c);
                    editIcon.draw(c);
                } else {
                    background.setBounds(0, 0, 0, 0);
                    editBackground.setBounds(0, 0, 0, 0);
                }
            }
        }).attachToRecyclerView(mRecyclerview);

        return root;
    }

    private void loadReminders() {
        Cursor cursor = new dbManager(getContext()).readAllReminders();
        while (cursor != null && cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndexOrThrow("id");
            int titleIndex = cursor.getColumnIndexOrThrow("title");
            int expDateIndex = cursor.getColumnIndexOrThrow("expDate");
            int timeIndex = cursor.getColumnIndexOrThrow("time");
            int addDataIndex = cursor.getColumnIndexOrThrow("addDate");
            int imagePathIndex = cursor.getColumnIndexOrThrow("imagePath");

            Model model = new Model(
                    cursor.getString(titleIndex),
                    cursor.getString(expDateIndex),
                    cursor.getString(timeIndex),
                    cursor.getInt(idIndex),
                    cursor.getString(addDataIndex),
                    cursor.getString(imagePathIndex));
            dataholder.add(model);
        }
    }

    private void showFilterMenu() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), btnFilter);
        popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
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

    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Reminder");
        builder.setMessage("Are you sure you want to delete this reminder?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                adapter.deleteItem(position);
                checkIfNoResults();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                adapter.notifyItemChanged(position);
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showEditDialog(int position) {
        Model reminder = dataholder.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_reminder, null);
        builder.setView(dialogView);

        EditText editTitle = dialogView.findViewById(R.id.editTitle);
        Button editDateBtn = dialogView.findViewById(R.id.editExpDate);
        Button editTimeBtn = dialogView.findViewById(R.id.editTime);
        Button editImagePathBtn = dialogView.findViewById(R.id.editImagePath);
        ImageView editImageView = dialogView.findViewById(R.id.editImageView);

        // Set current reminder details to the input fields
        editTitle.setText(reminder.getTitle());
        editDateBtn.setText("Edit expiration date" + " (" + reminder.getExpDate() + ")");
        editTimeBtn.setText("Edit time to notify" + " (" + reminder.getTime() + ")");
//        editImagePathBtn.setText(reminder.getImagePath());

        // Load current image
        Bitmap bitmap = BitmapFactory.decodeFile(reminder.getImagePath());
        if (bitmap != null) {
            editImageView.setImageBitmap(bitmap);
        }

        editDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(editDateBtn);
            }
        });

        editTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime(editTimeBtn);
            }
        });

        editImagePathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        builder.setTitle("Edit Reminder");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Get updated details from input fields
                String newTitle = editTitle.getText().toString();
                String newExpDate = editDateBtn.getText().toString();
                String newTime = editTimeBtn.getText().toString();
                String newImagePath = editImagePathBtn.getText().toString();

                // If a new image was selected, save it to storage and update the path
                if (imageUri != null) {
                    newImagePath = saveImageToStorage(imageUri);
                }

                // Update the reminder in the database
                dbManager db = new dbManager(getContext());
                db.updateReminder(reminder.getId(), newTitle, newExpDate, newTime, newImagePath);

                // Update the reminder in the adapter and notify the change
                reminder.setTitle(newTitle);
                reminder.setExpDate(newExpDate);
                reminder.setTime(newTime);
                reminder.setImagePath(newImagePath);
                adapter.notifyItemChanged(position);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                adapter.notifyItemChanged(position);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openFileChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Action");
        builder.setItems(new CharSequence[]{"Take Photo", "Choose from Gallery"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePicture.resolveActivity(requireActivity().getPackageManager()) != null) {
                                    File photoFile = null;
                                    try {
                                        photoFile = createImageFile();
                                    } catch (IOException ex) {
                                        // Error occurred while creating the File
                                        ex.printStackTrace();
                                    }
                                    // Continue only if the File was successfully created
                                    if (photoFile != null) {
                                        imageUri = FileProvider.getUriForFile(requireContext(),
                                                "com.example.expiryalert.fileprovider",
                                                photoFile);
                                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                        startActivityForResult(takePicture, TAKE_PHOTO_REQUEST);
                                    }
                                }
                                break;
                            case 1:
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, PICK_IMAGE_REQUEST);
                                break;
                        }
                    }
                });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PHOTO_REQUEST) {
                // The photo was taken and saved to imageUri
                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                    saveImageToStorage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                    saveImageToStorage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String saveImageToStorage(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return saveImageToStorage(bitmap);
    }

    private String saveImageToStorage(Bitmap bitmap) {
        FileOutputStream outputStream = null;
        try {
            String uuid = UUID.randomUUID().toString();
            File file = new File(requireContext().getExternalFilesDir(null), uuid + ".jpg");
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return file.getAbsolutePath(); // Save the file path for later use
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void selectDate(Button editDateBtn) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (datePicker, year1, month1, day1) -> {
            String selectedDate = day1 + "-" + (month1 + 1) + "-" + year1;
            editDateBtn.setText(selectedDate);
        }, year, month, day);

        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

    private void selectTime(Button editTimeBtn) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (timePicker, hourOfDay, minuteOfDay) -> {
            String selectedTime = String.format("%02d:%02d", hourOfDay, minuteOfDay);
            editTimeBtn.setText(selectedTime);
        }, hour, minute, false);

        timePickerDialog.show();
    }

    private void checkIfNoResults() {
        if (adapter.getItemCount() == 0) {
            noResultsTextView.setVisibility(View.VISIBLE);
        } else {
            noResultsTextView.setVisibility(View.GONE);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imageUri = Uri.fromFile(image);
        return image;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}