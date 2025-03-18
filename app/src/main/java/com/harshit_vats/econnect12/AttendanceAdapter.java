//package com.harshit_vats.econnect12;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {
//
//    private List<Student> studentList;
//
//    public AttendanceAdapter(List<Student> studentList) {
//        this.studentList = studentList;
//    }
//
//    @NonNull
//    @Override
//    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_student_attendance, parent, false);
//        return new AttendanceViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
//        Student student = studentList.get(position);
//        holder.nameTextView.setText(student.getName());
//        holder.emailTextView.setText(student.getEmail());
//        holder.attendanceCheckBox.setChecked(student.isPresent());
//
//        // Toggle attendance status when checkbox is clicked
//        holder.attendanceCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            student.setPresent(isChecked);
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return studentList.size();
//    }
//
//    public List<Student> getUpdatedStudentList() {
//        return studentList;
//    }
//
//    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {
//        TextView nameTextView, emailTextView;
//        CheckBox attendanceCheckBox;
//
//        public AttendanceViewHolder(@NonNull View itemView) {
//            super(itemView);
//            nameTextView = itemView.findViewById(R.id.student_name_tv);
//            emailTextView = itemView.findViewById(R.id.student_email_tv);
//            attendanceCheckBox = itemView.findViewById(R.id.attendance_checkbox);
//        }
//    }
//}
