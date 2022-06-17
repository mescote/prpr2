package com.example.tneve.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tneve.model.Message;
import com.example.tneve.model.User;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tneve.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    public static final int MESSAGE_RECEIVED = 0;
    public static final int MESSAGE_SENT = 1;

    private static List<Message> mMessages = new ArrayList<>();

    public static List<Message> getMessages() {
        return mMessages;
    }

    public static void setMessages(List<Message> messages) {
        MessageAdapter.mMessages = messages;
    }

    public static void clearMessages() {
        mMessages.clear();
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        long userId = User.getUser().getId();
        long senderId = mMessages.get(position).getSenderId();
        return (senderId == userId) ? MESSAGE_SENT : MESSAGE_RECEIVED;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = (viewType == MESSAGE_SENT) ? R.layout.chat_right : R.layout.chat_left;

        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);

        return new MessageHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, final int position) {
        CardView cardView = holder.cardView;
        Message message = mMessages.get(position);

        TextView messageContent = cardView.findViewById(R.id.chat_text);
        messageContent.setText(message.getContent());
    }

    // View Holder
    public static class MessageHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public MessageHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }
}
