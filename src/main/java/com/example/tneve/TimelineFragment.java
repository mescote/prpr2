package com.example.tneve;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tneve.adapter.TimelineAdapter;
import com.example.tneve.api.ApiService;
import com.example.tneve.model.Event;
import com.example.tneve.model.User;

import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimelineFragment extends Fragment {

    private RecyclerView mTimelineRecycler;
    private TimelineAdapter mTimelineAdapter;
    private TextView mSwitch;

    public TimelineFragment() {
        // Required empty public constructor
    }

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        mTimelineRecycler = view.findViewById(R.id.timeline_recyclerview);

        mTimelineAdapter = new TimelineAdapter(this);

        mTimelineRecycler.setAdapter(mTimelineAdapter);
        mTimelineRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        TextView futureEvents = view.findViewById(R.id.future_events);
        futureEvents.setOnClickListener(this::onClickChangeTab);

        TextView pastEvents = view.findViewById(R.id.past_events);
        pastEvents.setOnClickListener(this::onClickChangeTab);

        mSwitch = futureEvents;
        mSwitch.setSelected(true);

        getFutureEvents();

        return view;
    }

    public void onClickChangeTab(View view) {
        if (view.getId() != mSwitch.getId()) {
            mSwitch.setSelected(false);
            mSwitch = view.findViewById(view.getId());
            mSwitch.setSelected(true);
            TextView futureEvents = view.findViewById(R.id.future_events);
            TextView pastEvents = view.findViewById(R.id.past_events);
            if (mSwitch.getText().toString().equals("Future Events")) {
                getFutureEvents();
            } else {
                getOldEvents();
            }
        }
    }

    private void getFutureEvents() {
        String token = User.getUser().getApiToken();
        long userId = User.getUser().getId();

        ApiService.getInstance().getJoinedFutureEvents(token, userId).enqueue(new Callback<List<Event>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    List<Event> events = response.body();
                    if (events != null) {
                        events.sort(Comparator.comparing(Event::getStartDate));
                        TimelineAdapter.setEvents(events);
                        mTimelineAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.apiConnectionFail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOldEvents() {
        String token = User.getUser().getApiToken();
        long userId = User.getUser().getId();

        ApiService.getInstance().getJoinedPastEvents(token, userId).enqueue(new Callback<List<Event>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    List<Event> events = response.body();
                    if (events != null) {
                        events.sort(Comparator.comparing(Event::getStartDate));
                        TimelineAdapter.setEvents(events);
                        mTimelineAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {

            }
        });
    }
}