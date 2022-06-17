package com.example.tneve;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tneve.adapter.FriendAdapter;
import com.example.tneve.api.ApiService;
import com.example.tneve.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsFragment extends Fragment {

    private RecyclerView mFriendRecycler;
    private FriendAdapter mFriendAdapter;

    private SearchView mSearchView;

    public FriendsFragment() {
        // Required empty public constructor
    }

    public static FriendsFragment newInstance() {
        return new FriendsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        getFriends();
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        mFriendRecycler = view.findViewById(R.id.friends_recyclerview);

        mFriendAdapter = new FriendAdapter(this);
        mFriendAdapter.setListener(this::onClickFriendReq);

        mFriendRecycler.setAdapter(mFriendAdapter);
        mFriendRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSearchView = view.findViewById(R.id.friends_searchView);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchUsers(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    getFriends();
                }
                return false;
            }
        });

        return view;
    }

    private void onClickFriendReq(int position) {
        if (isAdded()) {
        }
    }

    /**
     *
     * @param query
     */
    private void searchUsers(String query) {
        String token = User.getUser().getApiToken();
        ApiService.getInstance().searchUser(token, query).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    FriendAdapter.setFriends(response.body());
                    mFriendAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    /**
     *
     */
    private void getFriends() {
        String token = User.getUser().getApiToken();
        ApiService.getInstance().getFriends(token).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> friendships = response.body();
                    User.getUser().setFriendships(friendships);

                    FriendAdapter.setFriends(friendships);
                    mFriendAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
        ApiService.getInstance().getFriendshipRequests(token).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> friendRequests = response.body();
                    User.getUser().setFriendRequests(friendRequests);
                    FriendAdapter.addFriends(friendRequests);
                    mFriendAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });

        //String token2 = User.getUser().getApiToken();
    }
}