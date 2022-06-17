package com.example.tneve;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tneve.api.ApiService;
import com.example.tneve.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingsFragment extends DialogFragment implements OnClickListener {

    Button mDeleteAccountButton, mSignOutAccountButton;
    Communicator communicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setCancelable(false);
        getDialog().setTitle("Title");

        View view = inflater.inflate(R.layout.fragment_settings, null, false);

        mDeleteAccountButton = (Button) view.findViewById(R.id.deleteAccountButton);
        mSignOutAccountButton = (Button) view.findViewById(R.id.signOutAccountButton);

        mDeleteAccountButton.setOnClickListener(this);
        mSignOutAccountButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.deleteAccountButton:
                dismiss();
                String token = User.getUser().getApiToken();
                ApiService.getInstance().deleteUser(token).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Intent intent = WelcomeActivity.newIntent(getActivity());
                            getActivity().startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

                break;

            case R.id.signOutAccountButton:
                dismiss();
                Intent intent2 = WelcomeActivity.newIntent(getActivity());
                getActivity().startActivity(intent2);
                break;
        }

    }

    public interface Communicator {
        public void message(String data);
    }

}