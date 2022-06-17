package com.example.tneve;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tneve.api.ApiService;
import com.example.tneve.model.User;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        User user = User.getUser();

        EditText mName = (EditText) view.findViewById(R.id.profile_name);
        EditText mLastName = (EditText) view.findViewById(R.id.profile_last_name);
        EditText mEmail = (EditText) view.findViewById(R.id.profile_email);
        EditText mPassword = (EditText) view.findViewById(R.id.profile_password);
        ImageView mImg = (ImageView) view.findViewById(R.id.profile_img_editable);

        mName.setEnabled(false);
        mLastName.setEnabled(false);
        mEmail.setEnabled(false);
        mPassword.setEnabled(false);

        mName.setText(user.getName());
        mLastName.setText(user.getLastName());
        mEmail.setText(user.getEmail());
        mPassword.setText(user.getPassword());

        Glide.with(this)
                .load(user.getImage())
                .error(R.drawable.default_profile)
                .into(mImg);

        Button mEditProfileButton = (Button) view.findViewById(R.id.button_edit_profile);
        mEditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditProfileButton.getText().toString().equalsIgnoreCase("EDIT PROFILE")) {
                    mName.setEnabled(true);
                    mLastName.setEnabled(true);
                    mEmail.setEnabled(true);
                    mPassword.setEnabled(true);
                    mEditProfileButton.setText(R.string.saveNewProfileInfo);
                } else {
                    User user = User.getUser();
                    User.getUser().updateUser(user);
                    String token = user.getApiToken();

                    //user.setImage(mImg.getText().toString());
                    user.setName(mName.getText().toString());
                    user.setLastName(mLastName.getText().toString());
                    user.setEmail(mEmail.getText().toString());
                    user.setPassword(mPassword.getText().toString());

                    ApiService.getInstance().updateUser(user, token).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {

                                mName.setEnabled(false);
                                mLastName.setEnabled(false);
                                mEmail.setEnabled(false);
                                mPassword.setEnabled(false);
                                mEditProfileButton.setText(R.string.editProfileButton);

                                user.setName(mName.getText().toString());
                                user.setLastName(mLastName.getText().toString());
                                user.setEmail(mEmail.getText().toString());
                                user.setPassword(mPassword.getText().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });

                }
            }
        });

        return view;
    }
}