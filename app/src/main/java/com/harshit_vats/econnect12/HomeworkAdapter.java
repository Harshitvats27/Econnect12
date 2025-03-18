package com.harshit_vats.econnect12;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder> {

    private List<HomeworkModel> homeworkList;

    public HomeworkAdapter(List<HomeworkModel> homeworkList) {
        this.homeworkList = homeworkList;
    }

    @NonNull
    @Override
    public HomeworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homework, parent, false);
        return new HomeworkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeworkViewHolder holder, int position) {
        HomeworkModel homework = homeworkList.get(position);

        if (homework != null) {
            holder.textHomeworkSubject.setText(homework.getSubject() != null ? homework.getSubject() : "No Subject");
            holder.textHomeworkDescription.setText(homework.getDescription() != null ? homework.getDescription() : "No Description");
            holder.textHomeworkDate.setText(homework.getDate() != null ? homework.getDate() : "No Date");
        }
    }

    @Override
    public int getItemCount() {
        return homeworkList != null ? homeworkList.size() : 0;
    }

    public void updateHomeworkList(List<HomeworkModel> newList) {
        this.homeworkList = newList;
        notifyDataSetChanged(); // Refresh RecyclerView
    }

    static class HomeworkViewHolder extends RecyclerView.ViewHolder {
        TextView textHomeworkDescription, textHomeworkSubject, textHomeworkDate;

        public HomeworkViewHolder(@NonNull View itemView) {
            super(itemView);
            textHomeworkSubject = itemView.findViewById(R.id.text_homework_subject);
            textHomeworkDescription = itemView.findViewById(R.id.text_homework_description);
            textHomeworkDate = itemView.findViewById(R.id.text_homework_date);

            // Check if views are null
            if (textHomeworkSubject == null || textHomeworkDescription == null || textHomeworkDate == null) {
                throw new NullPointerException("Check TextView IDs in item_homework.xml");
            }
        }
    }
}
