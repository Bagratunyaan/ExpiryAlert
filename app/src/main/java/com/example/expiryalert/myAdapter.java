package com.example.expiryalert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class myAdapter extends RecyclerView.Adapter<myAdapter.myviewholder> {
    private ArrayList<Model> dataholder;
    private Context context;

    public myAdapter(Context context, ArrayList<Model> dataholder) {
        this.dataholder = dataholder;
        this.context = context;

        // Sort the reminders by days left
        sortByExpDate();
    }

//    private void sortRemindersByDaysLeft() {
//        dataholder.sort(new Comparator<Model>() {
//            @Override
//            public int compare(Model o1, Model o2) {
//                return o1.getExpDate().compareTo(o2.getExpDate());
//            }
//        });
//        notifyDataSetChanged();
//    }

    // Sort the reminders by title alphabetically
    public void sortByTitle() {
        dataholder.sort(new Comparator<Model>() {
            @Override
            public int compare(Model o1, Model o2) {
                return o1.getTitle().compareToIgnoreCase(o2.getTitle());
            }
        });
        notifyDataSetChanged();
    }

    public void sortByTitleDescending() {
        dataholder.sort(new Comparator<Model>() {
            @Override
            public int compare(Model o1, Model o2) {
                return o2.getTitle().compareToIgnoreCase(o1.getTitle());
            }
        });
        notifyDataSetChanged();
    }

    // Sort the reminders by expiration date
    public void sortByExpDate() {
        dataholder.sort(new Comparator<Model>() {
            @Override
            public int compare(Model o1, Model o2) {
                return o1.getExpDate().compareTo(o2.getExpDate());
            }
        });
        notifyDataSetChanged();
    }

    public void sortByExpDateDescending() {
        dataholder.sort(new Comparator<Model>() {
            @Override
            public int compare(Model o1, Model o2) {
                return o2.getExpDate().compareTo(o1.getExpDate());
            }
        });
        notifyDataSetChanged();
    }

    // Sort the reminders by adding date
    public void sortByAddDate() {
        dataholder.sort(new Comparator<Model>() {
            @Override
            public int compare(Model o1, Model o2) {
                return o1.getAddDate().compareTo(o2.getAddDate());
            }
        });
        notifyDataSetChanged();
    }

    public void sortByAddDateDescending() {
        dataholder.sort(new Comparator<Model>() {
            @Override
            public int compare(Model o1, Model o2) {
                return o2.getAddDate().compareTo(o1.getAddDate());
            }
        });
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_reminder_file, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        Model model = dataholder.get(position);
        holder.mTitle.setText(model.getTitle());
        holder.mExpDate.setText(model.getExpDate());
        holder.mTime.setText(model.getTime());
        holder.mDaysLeft.setText("Days Left: " + getDaysUntilReminder(model.getExpDate()));

        // Check if reminder is expired
        if (isReminderExpired(model.getExpDate())) {
            holder.mDaysLeft.setText("Expired");
        }
    }

    private boolean isReminderExpired(String reminderDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date currentDate = new Date();
        Date reminderDateObject = null;
        try {
            reminderDateObject = dateFormat.parse(reminderDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // If reminder date is before current date, it's expired
        return reminderDateObject != null && reminderDateObject.before(currentDate);
    }


    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    class myviewholder extends RecyclerView.ViewHolder {
        TextView mTitle, mExpDate, mTime, mDaysLeft, mAddDate;
        ImageButton btnDelete;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.txtTitle);
            mExpDate = itemView.findViewById(R.id.txtDate);
            mTime = itemView.findViewById(R.id.txtTime);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            mDaysLeft = itemView.findViewById(R.id.txtDaysLeft);


            btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    deleteItem(position);
                }
            });
        }
    }

    private void deleteItem(int position) {
        int id = dataholder.get(position).getId();
        new dbManager(context).deleteReminder(id);
        dataholder.remove(position);
        notifyItemRemoved(position);
    }

    private long getDaysUntilReminder(String reminderDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date currentDate = new Date();
        Date reminderDateObject = null;
        try {
            reminderDateObject = dateFormat.parse(reminderDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (reminderDateObject != null) {
            long diffInMillies = reminderDateObject.getTime() - currentDate.getTime();
            return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        }

        return -1; // Error occurred
    }
}
