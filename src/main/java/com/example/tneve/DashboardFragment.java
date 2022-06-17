package com.example.tneve;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tneve.adapter.EventAdapter;
import com.example.tneve.api.ApiService;
import com.example.tneve.model.Event;
import com.example.tneve.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private RecyclerView mEventRecycler;
    private EventAdapter mEventAdapter;
    private TextView mEvents;
    private SearchView mSearchView;

    public DashboardFragment() {
        // Required empty private constructor
    }

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String token = User.getUser().getApiToken();
        ApiService.getInstance().getEvents(token).enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    EventAdapter.setEvents(filterPastEvents(response.body()));
                    mEventAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.apiConnectionFail, Toast.LENGTH_SHORT).show();
            }
        });
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        onClickSearchEvents(view);
        return view;
    }

    public void onClickSearchEvents(View view) {

        mEventRecycler = view.findViewById(R.id.dashboard_recyclerview);

        mEventAdapter = new EventAdapter(this);
        mEventAdapter.setListener(this::onCLickStartEventActivity);

        mEventRecycler.setAdapter(mEventAdapter);
        mEventRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSearchView = view.findViewById(R.id.dashboard_searchView);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getEvents(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                }
                return false;
            }

        });
    }

    private void getEvents(String query) {

        ApiService.getInstance().searchEvents(query).enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    EventAdapter.setEvents(filterPastEvents(response.body()));
                    mEventAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.apiConnectionFail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Event> filterPastEvents(List<Event> events) {
        List<Event> filteredEvents = new ArrayList<>();

        for (Event e : events) {
            filteredEvents.add(e);

        }
        return filteredEvents;
    }

    private void onCLickStartEventActivity(int position) {
        Intent intent = EventActivity.newIntent(getActivity(), position);
        getActivity().startActivity(intent);
    }
}
