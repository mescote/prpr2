package com.example.tneve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tneve.adapter.MessageAdapter;
import com.example.tneve.adapter.UserAdapter;
import com.example.tneve.api.ApiService;
import com.example.tneve.model.User;
import com.example.tneve.model.Message;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatActivity extends AppCompatActivity {

    private EditText mInputText;
    private TextView mUserChatName;
    private User mFriend;
    private RecyclerView mMessageRecycler;
    private MessageAdapter mMessageAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private int mNumMessages;
    private Timer mTimer;

    public static Intent newIntent(Context packageContext, int friendPos, long friendId) {
        Intent intent = new Intent(packageContext, ChatActivity.class);
        intent.putExtra("FRIEND_POS", friendPos);
        intent.putExtra("FRIEND_ID", friendId);
        return intent;
    }
    @Override
    protected void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        MessageAdapter.clearMessages();
        long friendId = getIntent().getLongExtra("FRIEND_ID", 0);
        ScrollView autoScroller = (ScrollView)findViewById(R.id.message_scroller);

        if (friendId == -1) {
            int position = getIntent().getIntExtra("FRIEND_POS", 0);
            mFriend = UserAdapter.getFriends().get(position);
            setInfo();
            mTimer = new Timer();
            mTimer.schedule(new TimerTask()
            {
                @Override
                public void run() {
                    getMessages();
                    autoScroller.scrollTo(0, autoScroller.getBottom());
                }
            }, 0, 2000L);
        }
        else {
            String token = User.getUser().getApiToken();
            ApiService.getInstance().getUser(token, friendId).enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if (response.isSuccessful()) {
                        mFriend = response.body().get(0);
                        setInfo();
                        mTimer = new Timer();
                        mTimer.schedule(new TimerTask()
                        {
                            @Override
                            public void run() {
                                getMessages();
                                autoScroller.scrollTo(0, autoScroller.getBottom());
                            }
                        }, 0, 2000L);
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {

                }
            });
        }

        mMessageRecycler = findViewById(R.id.individual_chat_recyclerview);
        mMessageRecycler.setHasFixedSize(true);
        mMessageAdapter = new MessageAdapter();
        mMessageRecycler.setAdapter(mMessageAdapter);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mMessageRecycler.setLayoutManager(mLinearLayoutManager);
    }

    private void setInfo() {
        ShapeableImageView profileImageView = findViewById(R.id.user_image_chat);
        TextView nameTextView = findViewById(R.id.user_name_chat);

        Glide.with(this)
                .load(mFriend.getImage())
                .error(R.drawable.default_profile)
                .into(profileImageView);

        String username = mFriend.getName() + mFriend.getLastName();
        nameTextView.setText(username);
        mInputText = findViewById(R.id.chat_text_write);
    }

    public void onCLickSendMessage(View view) {
        String input = mInputText.getText().toString();

        if (!input.isEmpty()) {
            sendMessage(input);
            mInputText.setText("");
        }
    }

    public void onClickGoBackChats(View view) {
        finish();
    }

    private void sendMessage(String input) {
        String token = User.getUser().getApiToken();
        long senderId = User.getUser().getId();
        long receiverId = mFriend.getId();

        ApiService.getInstance().sendMessage(token, new Message(input, senderId, receiverId, new Date())).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    MessageAdapter.getMessages().add(new Message(input, 1, mFriend.getId(), new Date()));
                    mLinearLayoutManager.scrollToPosition(MessageAdapter.getMessages().size() - 1);
                    mMessageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }

    private void getMessages() {
        String token = User.getUser().getApiToken();
        long userId = mFriend.getId();

        ApiService.getInstance().getMessages(token, userId).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > mNumMessages) {
                        MessageAdapter.setMessages(response.body());
                        mNumMessages = response.body().size();
                        mMessageAdapter.notifyDataSetChanged();
                        mLinearLayoutManager.scrollToPosition(MessageAdapter.getMessages().size() - 1);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {

            }
        });
    }
}
