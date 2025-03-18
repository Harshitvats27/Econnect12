package com.harshit_vats.econnect12;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private List<String> classList;
    private OnClassClickListener listener;

    public interface OnClassClickListener {
        void onClassClick(String className);
    }

    public ClassAdapter(List<String> classList, OnClassClickListener listener) {
        this.classList = classList;
        this.listener = listener;
    }

    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position) {
        String className = classList.get(position);
        holder.classNameTextView.setText(className);
        holder.itemView.setOnClickListener(v -> listener.onClassClick(className));
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView classNameTextView;

        public ClassViewHolder(View itemView) {
            super(itemView);
            classNameTextView = itemView.findViewById(R.id.classNameTextView);
        }
    }
}
