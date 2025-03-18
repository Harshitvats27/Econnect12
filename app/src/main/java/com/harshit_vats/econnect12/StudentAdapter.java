package com.harshit_vats.econnect12;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private List<StudentModel> studentList;

    public StudentAdapter(List<StudentModel> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentModel student = studentList.get(position);
        holder.nameText.setText(student.getName());
        holder.rollText.setText("Roll No: " + student.getRollNumber());

        // ✅ Set checkbox state correctly
        holder.attendanceCheckBox.setOnCheckedChangeListener(null); // Prevent unwanted triggers
        holder.attendanceCheckBox.setChecked(student.isPresent());

        holder.attendanceCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            student.setPresent(isChecked); // Store attendance state
        });
    }


    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public List<StudentModel> getStudentList() {
        return studentList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, rollText;
        CheckBox attendanceCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.studentName);
            rollText = itemView.findViewById(R.id.rollNumber);
            attendanceCheckBox = itemView.findViewById(R.id.attendanceCheckbox);
        }
    }
}
