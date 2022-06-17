package com.example.tneve.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tneve.R;

import com.bumptech.glide.Glide;
import com.example.tneve.model.Event;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {

    private static List<Event> mEvents = new ArrayList<>();
    private EventListener mListener;

    private Fragment mFragment;

    public EventAdapter(Fragment fragment) {
        this.mFragment = fragment;
    }

    public static List<Event> getEvents() {
        return mEvents;
    }

    public static void setEvents(List<Event> events) {
        EventAdapter.mEvents = events;
    }

    public void setListener(EventListener eventListener) {
        this.mListener = eventListener;
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    @NonNull
    @Override
    public EventAdapter.EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        return new EventHolder(cardView);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, final int position) {

        CardView cardView = holder.cardView;
        Event event = mEvents.get(position);

        // Change date format
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy", Locale.ENGLISH);
        LocalDate dateStart = LocalDate.parse(event.getStartDate(), inputFormatter);
        String formattedDateStart = outputFormatter.format(dateStart);

        ImageView imageView = cardView.findViewById(R.id.card_event_image);
        TextView typeTextView = cardView.findViewById(R.id.card_event_type);
        TextView dateTextView = cardView.findViewById(R.id.card_event_date);
        TextView nameTextView = cardView.findViewById(R.id.card_event_name);
        TextView locationTextView = cardView.findViewById(R.id.card_event_location);
        TextView numParticipantsTextView = cardView.findViewById(R.id.card_event_participants);

        Glide.with(mFragment)
                .load(event.getImage())
                .placeholder(R.drawable.default_event)
                .into(imageView);

        nameTextView.setText(event.getName());
        typeTextView.setText("Type: " + event.getType());
        dateTextView.setText("Start date: " + formattedDateStart);
        locationTextView.setText(event.getLocation());
        String participants = "Max Participants: " + event.getTotalParticipants();
        numParticipantsTextView.setText(participants);

        // Set onCLick operation
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(position);
                }
            }
        });
    }

    // Interface to decouple the Adapter
    public interface EventListener {
        void onClick(int position);
    }

    // View Holder
    public static class EventHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public EventHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }
}
