package com.example.tneve;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tneve.api.ApiService;
import com.example.tneve.model.Event;
import com.example.tneve.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

public class CreateEventFragment extends Fragment {

    private EditText mEventTitle, mEventDescription, mEventType, mEventLocation, mNumParticipants;
    private TextView mEventStartDate, mEventEndDate, mEventStartTime, mEventEndTime;

    CreateEventFragment() {
        // Required empty private constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);

        mEventTitle = view.findViewById(R.id.name_event);
        mEventDescription = view.findViewById(R.id.description_event);
        mEventType = view.findViewById(R.id.type_event);
        mEventStartDate = view.findViewById(R.id.start_date_event);
        mEventEndDate = view.findViewById(R.id.end_date_event);
        mEventLocation = view.findViewById(R.id.location_event);
        mNumParticipants = view.findViewById(R.id.num_participants_event);
        mEventStartTime = view.findViewById(R.id.start_time_event);
        mEventEndTime = view.findViewById(R.id.end_time_event);

        onClickCreateEvents(view);

        return view;
    }

    // ================================================
    // TODO: add event image when clicked
    // ================================================
    public void onCLickAddEventImg(View view) {
        Toast.makeText(getActivity(), "TODO: Add img", Toast.LENGTH_LONG).show();
    }

    public void onClickCreateEvents(View view) {

        Button mCreateEventButton = (Button) view.findViewById(R.id.button_create_event);
        mCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                int maxParticipants;

                String startDate = "null";
                String endDate = "null";

                String image = "noImage";
                String name = mEventTitle.getText().toString();
                String description = mEventDescription.getText().toString();
                String type = mEventType.getText().toString();
                Date startDayIn = null;
                Date endDayIn = null;
                String location = mEventLocation.getText().toString();
                String numParticipants = mNumParticipants.getText().toString();
                String startTime = mEventStartTime.getText().toString();
                String endTime = mEventEndTime.getText().toString();

                String startDay = "null";
                String endDay = "null";

                try {
                    String sDate = (mEventStartDate.getText().toString());
                    String eDate = (mEventEndDate.getText().toString());

                    startDayIn = new SimpleDateFormat("dd/MM/yyyy").parse(sDate);
                    endDayIn = new SimpleDateFormat("dd/MM/yyyy").parse(eDate);

                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    sdf1.format(startDayIn);
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                    sdf2.format(endDayIn);

                    startDay = sdf1.format(startDayIn);
                    endDay = sdf2.format(endDayIn);

                    startDate = (startDay + "T" + startTime + ":00.000" + "Z");
                    endDate = (endDay + "T" + endTime + ":00.000" + "Z");

                    //Toast.makeText(getActivity(), "full start date = " + startDate, Toast.LENGTH_LONG).show();
                    //Toast.makeText(getActivity(), "full end date = " + endDate + ", " + endDay, Toast.LENGTH_LONG).show();

                } catch (java.text.ParseException ignored) {
                }

                if (name.isEmpty() || description.isEmpty() || type.isEmpty() || startDay.equals("null")
                        || endDay.equals("null") || numParticipants.isEmpty() || location.isEmpty()
                        || startTime.equals("null") || endTime.equals("null")) {
                    Toast.makeText(getActivity(), "Empty fields", Toast.LENGTH_SHORT).show();
                } else {
                    maxParticipants = Integer.parseInt(numParticipants);
                    String token = User.getUser().getApiToken();
                    Event event = new Event(name, type, description, maxParticipants, location, image, startDate, endDate);
                    ApiService.getInstance().createEvent(token, event).enqueue(new retrofit2.Callback<Event>() {

                        @Override
                        public void onResponse(Call<Event> call, Response<Event> response) {
                            getActivity().finish();
                        }

                        @Override
                        public void onFailure(Call<Event> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }
}