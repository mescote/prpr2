package com.example.tneve.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tneve.R;
import com.example.tneve.api.ApiService;
import com.example.tneve.model.FriendshipResponse;
import com.example.tneve.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendHolder> {
    private static List<User> mFriends = new ArrayList<>();
    private FriendListener mListener;

    private Fragment mFragment;

    public FriendAdapter(Fragment fragment) {
        this.mFragment = fragment;
    }

    public static List<User> getFriends() {
        return mFriends;
    }

    public static void setFriends(List<User> friends) {
        FriendAdapter.mFriends = friends;
    }

    public static void addFriends(List<User> friends) {
        FriendAdapter.mFriends.addAll(friends);
    }

    public void setListener(FriendListener friendListener) {
        this.mListener = friendListener;
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_card_layout, parent, false);
        return new FriendHolder(cardView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, final int position) {
        CardView cardView = holder.cardView;
        User friend = mFriends.get(position);

        TextView nameTextView = cardView.findViewById(R.id.card_friend_name);
        TextView nameEmail = cardView.findViewById(R.id.card_friend_email);

        Button mFriendReq = cardView.findViewById(R.id.friend_req_box);
        Button mFriendReject = cardView.findViewById(R.id.friend_reject);
        mFriendReject.setVisibility(View.INVISIBLE);

        String username = friend.getName() + friend.getLastName();
        nameTextView.setText(username);
        nameEmail.setText(friend.getEmail());

        User user = User.getUser();

        System.out.println("getFriendships -> " + user.getFriendships());
        System.out.println("getFriendshipRequests() -> " + user.getFriendshipRequests());
        Optional<User> isFriend = user.getFriendships().stream()
                .filter(u -> u.getId() == friend.getId())
                .findFirst();

        Optional<User> isFriendReq = user.getFriendshipRequests().stream()
                .filter(u -> u.getId() == friend.getId())
                .findFirst();

        if (isFriendReq.isPresent()) {
            System.out.println("User friendship request!!!!!");

            // Show button to reject user friendship
            mFriendReject.setVisibility(View.VISIBLE);
            mFriendReq.setText("Accept");
        } else if (isFriend.isPresent()) {
            mFriendReject.setVisibility(View.INVISIBLE);
            mFriendReq.setText("Following");
        }

        mFriendReject.setOnClickListener(new View.OnClickListener() {
             @RequiresApi(api = Build.VERSION_CODES.N)
             @Override
             public void onClick(View view) {
                 System.out.println("Reject friendship -> " + friend.getId() + " name: "+ friend.getName());

                 String token = User.getUser().getApiToken();
                 ApiService.getInstance().rejectFriendship(token, friend.getId()).enqueue(new Callback<FriendshipResponse>() {
                     @Override
                     public void onResponse(Call<FriendshipResponse> call, Response<FriendshipResponse> response) {
                         if (response.isSuccessful()) {
                             // Change req button text to follow after this user has been rejecgted
                             mFriendReq.setText("Follow");

                             // Hide button used to reject user request once this has been rejected
                             mFriendReject.setVisibility(View.INVISIBLE);
                         }
                     }
                     @Override
                     public void onFailure(Call<FriendshipResponse> call, Throwable t) {
                         System.err.println("Reject friendship -> onFailure: " + t.getMessage());
                     }
                 });
             }
         });

        mFriendReq.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onClick(position);

                    if (mFriendReq.getText().equals("Follow")) {
                        System.err.println("Request friendship -> " + friend.getId());
                        String token = User.getUser().getApiToken();
                        ApiService.getInstance().requestFriendship(token, friend.getId()).enqueue(new Callback<FriendshipResponse>() {
                            @Override
                            public void onResponse(Call<FriendshipResponse> call, Response<FriendshipResponse> response) {
                                if (response.isSuccessful()) {
                                    mFriendReq.setText("Pending");
                                }
                            }
                            @Override
                            public void onFailure(Call<FriendshipResponse> call, Throwable t) {
                                System.err.println("Request friendship -> onFailure: " + t.getMessage());
                            }
                        });
                    }

                    if (mFriendReq.getText().equals("Accept")) {
                        System.err.println("Accept friendship -> " + friend.getId());
                        String token = User.getUser().getApiToken();
                        mFriendReject.setVisibility(View.INVISIBLE);
                        ApiService.getInstance().acceptFriendship(token, friend.getId()).enqueue(new Callback<FriendshipResponse>() {
                            @Override
                            public void onResponse(Call<FriendshipResponse> call, Response<FriendshipResponse> response) {
                                if (response.isSuccessful()) {
                                    mFriendReject.setVisibility(View.INVISIBLE);
                                    mFriendReq.setText("Following");
                                }
                            }
                            @Override
                            public void onFailure(Call<FriendshipResponse> call, Throwable t) {
                                System.err.println("Accept friendship -> onFailure: " + t.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }

    // Interface to decouple the Adapter
    public interface FriendListener {
        void onClick(int position);
    }

    // View Holder
    public static class FriendHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public FriendHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }
}
