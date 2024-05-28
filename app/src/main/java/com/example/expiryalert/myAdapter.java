package com.example.expiryalert;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class myAdapter extends RecyclerView.Adapter<myAdapter.myviewholder> implements Filterable {
    private List<Model> dataholder;
    private List<Model> dataholderFull;
    private Context context;

    public myAdapter(Context context, List<Model> dataholder) {
        this.context = context;
        this.dataholder = dataholder;
        this.dataholderFull = new ArrayList<>(dataholder);
        sortByExpDate();
    }

    public void sortByTitle() {
        dataholder.sort(Comparator.comparing(Model::getTitle, String::compareToIgnoreCase));
        notifyDataSetChanged();
    }

    public void sortByTitleDescending() {
        dataholder.sort((o1, o2) -> o2.getTitle().compareToIgnoreCase(o1.getTitle()));
        notifyDataSetChanged();
    }

    public void sortByExpDate() {
        dataholder.sort(Comparator.comparing(Model::getExpDate));
        notifyDataSetChanged();
    }

    public void sortByExpDateDescending() {
        dataholder.sort((o1, o2) -> o2.getExpDate().compareTo(o1.getExpDate()));
        notifyDataSetChanged();
    }

    public void sortByAddDate() {
        dataholder.sort(Comparator.comparing(Model::getAddDate));
        notifyDataSetChanged();
    }

    public void sortByAddDateDescending() {
        dataholder.sort((o1, o2) -> o2.getAddDate().compareTo(o1.getAddDate()));
        notifyDataSetChanged();
    }

    public boolean isListEmpty() {
        return getItemCount() == 0;
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
        holder.itemView.getContext().getApplicationContext().sendBroadcast(new Intent("DATA_SET_CHANGED"));

        if (isReminderExpired(model.getExpDate())) {
            holder.mDaysLeft.setText("Expired");
        }

        if (model.getImageBitmap() != null) {
            holder.mImageView.setImageBitmap(model.getImageBitmap());
        }
//        else {
//            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
//        }
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

        return reminderDateObject != null && reminderDateObject.before(currentDate);
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    class myviewholder extends RecyclerView.ViewHolder {
        TextView mTitle, mExpDate, mTime, mDaysLeft;
        ImageView mImageView;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.txtTitle);
            mExpDate = itemView.findViewById(R.id.txtDate);
            mTime = itemView.findViewById(R.id.txtTime);
            mImageView = itemView.findViewById(R.id.imageView);
            mDaysLeft = itemView.findViewById(R.id.txtDaysLeft);
        }
    }

    public void deleteItem(int position) {
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

    @Override
    public Filter getFilter() {
        return dataFilter;
    }

    private final Filter dataFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Model> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(dataholderFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Model item : dataholderFull) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataholder.clear();
            dataholder.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}


