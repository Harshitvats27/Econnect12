package com.harshit_vats.econnect12;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.util.Log;
public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {

    private List<Notice> noticeList = new ArrayList<>();
    private final SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    private final boolean isUpcoming; // true = Upcoming, false = Completed

    public NoticeAdapter(List<Notice> noticeList, boolean isUpcoming) {
        this.noticeList = noticeList != null ? noticeList : new ArrayList<>();
        this.isUpcoming = isUpcoming;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        Notice notice = noticeList.get(position);

        // Log data binding
        Log.d("NoticeAdapter", "Binding notice at position: " + position + ", Title: " + notice.getNotice_title());

        holder.title.setText(notice.getNotice_title());
        holder.message.setText(notice.getNotice_description());
        holder.date.setText(formatDate(notice.getNotice_date()));  // Corrected field name

        // Set status based on whether the notice is upcoming or completed
        if (isUpcoming) {
            holder.status.setText("Upcoming");
            holder.status.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_green_dark));
            holder.statusIndicator.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_green_dark));
        } else {
            holder.status.setText("Completed");
            holder.status.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_red_dark));
            holder.statusIndicator.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_red_dark));
        }
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    public void updateNotices(List<Notice> newNotices) {
        // Log the incoming new notices list
        Log.d("NoticeAdapter", "Received new notices for update: " + newNotices);

        // If the new notices are null or empty, don't update
        if (newNotices == null || newNotices.isEmpty()) {
            Log.d("NoticeAdapter", "No new notices to update.");
            return;
        }

        // Use DiffUtil only if the new list is different from the old list
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new NoticeDiffCallback(this.noticeList, newNotices));
        this.noticeList.clear();
        this.noticeList.addAll(newNotices);

        // Log after adding new notices
        Log.d("NoticeAdapter", "Adapter list size after update: " + this.noticeList.size());

        // Dispatch changes to RecyclerView
        diffResult.dispatchUpdatesTo(this);
    }

    private String formatDate(Date date) {
        if (date == null) {
            return "Unknown Date";
        }
        return outputFormat.format(date);  // Format date as per your requirement
    }

    public static class NoticeViewHolder extends RecyclerView.ViewHolder {
        TextView title, message, date, status;
        View statusIndicator;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.noticeTitle);
            message = itemView.findViewById(R.id.noticeMessage);
            date = itemView.findViewById(R.id.noticeDate);
            status = itemView.findViewById(R.id.noticeStatus);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);
        }
    }
}
