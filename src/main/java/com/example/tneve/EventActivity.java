package com.example.tneve;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tneve.adapter.EventAdapter;
import com.example.tneve.api.ApiService;
import com.example.tneve.model.Attend;
import com.example.tneve.model.Event;
import com.example.tneve.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventActivity extends AppCompatActivity {

    private ImageView mImage;
    private TextView mTitle, mDescription, mLocation, mStartDate, mEndDate, mType, mParticipants;
    private Button mAttendButton;

    private Event mEvent;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        int position = getIntent().getIntExtra("EVENT_POS", 0);
        mEvent = EventAdapter.getEvents().get(position);

        mImage = findViewById(R.id.individual_image);
        mTitle = findViewById(R.id.individual_title);
        mDescription = findViewById(R.id.individual_description);
        mStartDate = findViewById(R.id.start_date_info);
        mEndDate = findViewById(R.id.end_date_info);
        mLocation = findViewById(R.id.location_info);
        mType = findViewById(R.id.type_info);
        mParticipants = findViewById(R.id.participants_info);
        mAttendButton = findViewById(R.id.button_attend_event);

        // Change date format
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy", Locale.ENGLISH);
        LocalDate dateStart = LocalDate.parse(mEvent.getStartDate(), inputFormatter);
        LocalDate dateEnd = LocalDate.parse(mEvent.getEndDate(), inputFormatter);
        String formattedDateStart = outputFormatter.format(dateStart);
        String formattedDateEnd = outputFormatter.format(dateEnd);

        Glide.with(this)
                .load(mEvent.getImage())
                .placeholder(R.drawable.default_event)
                .into(mImage);

        mTitle.setText(mEvent.getName());
        mDescription.setText("Description: \n" + mEvent.getDescription());
        mStartDate.setText("Start Date: " + formattedDateStart);
        mEndDate.setText("End Date: " + formattedDateEnd);
        mLocation.setText("Location: " + mEvent.getLocation());
        mType.setText("Type: " + mEvent.getType());
        mParticipants.setText("Max Participants: " + mEvent.getTotalParticipants());

        User user = User.getUser();
        if (mEvent.getOwnerId() == user.getId()) {
            mAttendButton.setText(R.string.deleteEventButton);
            String token = User.getUser().getApiToken();
            ApiService.getInstance().deleteEvent(token, mEvent.getId()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } else if (mEvent.getStartDate().compareTo(String.valueOf(new Date())) > 0) {
            mAttendButton.setVisibility(View.GONE);
        } else {
            String token = User.getUser().getApiToken();
            ApiService.getInstance().getJoinedFutureEvents(token, User.getUser().getId()).enqueue(new Callback<List<Event>>() {
                @Override
                public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                    if (response.isSuccessful()) {
                        for (Event event : response.body()) {
                            if (event.getId() == mEvent.getId()) {
                                mAttendButton.setText(R.string.cancelAttendEventButton);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Event>> call, Throwable t) {

                }
            });
        }
    }

    public static Intent newIntent(Context packageContext, int eventPos) {
        Intent intent = new Intent(packageContext, EventActivity.class);
        intent.putExtra("EVENT_POS", eventPos);
        return intent;
    }

    public void onClickGoBackEvents(View view) {
        finish();
    }

    public void onClickAttendEvent(View view) {
        if (mAttendButton.getText().toString().equalsIgnoreCase("Attend")) {
            String token = User.getUser().getApiToken();

            ApiService.getInstance().confirmAttend(token, mEvent.getId(), 1, null).enqueue(new Callback<Attend>() {
                @Override
                public void onResponse(Call<Attend> call, Response<Attend> response) {
                    if (response.isSuccessful()) {
                        // Update button
                        mAttendButton.setText(R.string.cancelAttendEventButton);
                    }
                }

                @Override
                public void onFailure(Call<Attend> call, Throwable t) {

                }

            });
        } else {
            String token = User.getUser().getApiToken();
            User user = User.getUser();
            ApiService.getInstance().cancelAttendance(user.getId(), mEvent.getId(), token).enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        mAttendButton.setText(R.string.attendEventButton);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(EventActivity.this, "ok", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}