package com.example.tneve;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tneve.adapter.UserAdapter;
import com.example.tneve.api.ApiService;
import com.example.tneve.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesFragment extends Fragment {

    private RecyclerView mUserRecycler;
    private UserAdapter mUserAdapter;

    private SearchView mSearchView;

    public MessagesFragment() {
        // Required empty public constructor
    }

    public static MessagesFragment newInstance() {
        return new MessagesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        mUserRecycler = view.findViewById(R.id.chats_recyclerview);

        mUserAdapter = new UserAdapter(this);
        mUserAdapter.setListener(this::onCLickStartChatActivity);

        mUserRecycler.setAdapter(mUserAdapter);
        mUserRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        getChats();

        mSearchView = view.findViewById(R.id.messages_searchView);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchUser(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    getChats();
                }
                return false;
            }
        });
        return view;
    }

    private void onCLickStartChatActivity(int position) {
        if (isAdded()) {
            Intent intent = ChatActivity.newIntent(getActivity(), position, -1);
            getActivity().startActivity(intent);
        }
    }

    private void searchUser(String query) {
        String token = User.getUser().getApiToken();
        ApiService.getInstance().searchUser(token, query).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    UserAdapter.setFriends(response.body());
                    mUserAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
            }
        });
    }

    private void getChats() {
        String token = User.getUser().getApiToken();
        ApiService.getInstance().getMessages(token).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    UserAdapter.setFriends(response.body());
                    mUserAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
            }
        });
    }
}
