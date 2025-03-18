package com.harshit_vats.econnect12;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<Event> eventsList;

    public EventsAdapter(List<Event> eventsList) {
        this.eventsList = eventsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = eventsList.get(position);
        holder.eventName.setText(event.getEvent_name());
        holder.eventTime.setText(event.getEvent_time());
        holder.eventDate.setText(event.getEvent_date());
        holder.eventDescription.setText(event.getDescription());
        holder.eventLocation.setText(event.getLocation());
        holder.eventCategory.setText(event.getCategory());
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventDescription, eventDate, eventLocation, eventTime, eventCategory;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views
            eventName = itemView.findViewById(R.id.eventName);
            eventDescription = itemView.findViewById(R.id.eventDescription);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            eventTime = itemView.findViewById(R.id.eventTime);
            eventCategory = itemView.findViewById(R.id.eventCategory); // Add category field
        }
    }
}
