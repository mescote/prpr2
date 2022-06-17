package com.example.tneve.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tneve.R;
import com.example.tneve.model.Event;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineHolder> {

    private static List<Event> mEvents = new ArrayList<>();
    private TimelineAdapter.TimelineListener mListener;

    private Fragment mFragment;

    public TimelineAdapter(Fragment fragment) {
        this.mFragment = fragment;
    }

    public static List<Event> getEvents() {
        return mEvents;
    }

    public static void setEvents(List<Event> events) {
        TimelineAdapter.mEvents = events;
    }

    public void setListener(TimelineAdapter.TimelineListener timelineListener) {
        this.mListener = timelineListener;
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    @NonNull
    @Override
    public TimelineAdapter.TimelineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeline_card_layout, parent, false);
        return new TimelineHolder(cardView);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull TimelineAdapter.TimelineHolder holder, final int position) {
        CardView cardView = holder.cardView;
        Event event = mEvents.get(position);

        // Change date format
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy", Locale.ENGLISH);
        LocalDate dateStart = LocalDate.parse(event.getStartDate(), inputFormatter);
        String formattedDateStart = outputFormatter.format(dateStart);

        TextView imageView = cardView.findViewById(R.id.timeline_event_name);
        TextView typeTextView = cardView.findViewById(R.id.timeline_event_date);

        imageView.setText(event.getName());
        typeTextView.setText(formattedDateStart);

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
    public interface TimelineListener {
        void onClick(int position);
    }

    // View Holder
    public static class TimelineHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public TimelineHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }
}
