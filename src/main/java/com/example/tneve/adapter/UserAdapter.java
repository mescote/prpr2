package com.example.tneve.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tneve.model.User;
import com.google.android.material.imageview.ShapeableImageView;
import com.example.tneve.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.FriendHolder> {
    private static List<User> mFriends = new ArrayList<>();
    private FriendListener mListener;

    private Fragment mFragment;

    public UserAdapter(Fragment fragment) {
        this.mFragment = fragment;
    }

    public static List<User> getFriends() {
        return mFriends;
    }

    public static void setFriends(List<User> friends) {
        UserAdapter.mFriends = friends;
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
                .inflate(R.layout.chat_card_layout, parent, false);
        return new FriendHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, final int position) {
        CardView cardView = holder.cardView;
        User friend = mFriends.get(position);

        ShapeableImageView profileImageView = cardView.findViewById(R.id.card_chat_image);
        TextView nameTextView = cardView.findViewById(R.id.card_chat_name);
        TextView email = cardView.findViewById(R.id.card_chat_email);

        Glide.with(mFragment)
                .load(friend.getImage())
                .error(R.drawable.default_profile)
                .into(profileImageView);

        String username = friend.getName() + friend.getLastName();
        nameTextView.setText(username);
        email.setText(friend.getEmail());

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
