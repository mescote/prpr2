package com.example.tneve;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tneve.adapter.EventAdapter;
import com.example.tneve.api.ApiService;
import com.example.tneve.model.Event;
import com.example.tneve.model.User;

import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsFragment extends Fragment {

    private RecyclerView mEventRecycler;
    private EventAdapter mEventAdapter;
    private TextView mEvents;

    public EventsFragment() {
        // Required empty private constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static EventsFragment newInstance() {
        return new EventsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loadEvents();
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        mEventRecycler = view.findViewById(R.id.events_recyclerview);

        mEventAdapter = new EventAdapter(this);
        mEventAdapter.setListener(this::onCLickStartEventActivity);

        mEventRecycler.setAdapter(mEventAdapter);
        mEventRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadEvents();

        return view;
    }

    public void loadEvents() {

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
                        EventAdapter.setEvents(events);
                        mEventAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {

            }
        });
    }

    private void onCLickStartEventActivity(int position) {
        Intent intent = EventActivity.newIntent(getActivity(), position);
        getActivity().startActivity(intent);
    }
}
