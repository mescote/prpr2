package com.example.tneve;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tneve.api.ApiService;
import com.example.tneve.api.ApiToken;
import com.example.tneve.model.User;
import com.example.tneve.model.UserObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    Button signInButton;
    EditText mEmail, mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mEmail = findViewById(R.id.email_sigin);
        mPassword = findViewById(R.id.password_signin);

        signInButton = (Button) findViewById(R.id.button_signin);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.fieldsMissing, Toast.LENGTH_SHORT).show();
                } else {
                    ApiService.getInstance().authenticationUser(new UserObject(email, password)).enqueue(new Callback<ApiToken>() {
                        @Override
                        public void onResponse(Call<ApiToken> call, Response<ApiToken> response) {
                            if (response.isSuccessful()) {
                                getUser(response.body(), email, password);
                            } else {
                                Toast.makeText(SignInActivity.this, R.string.apiLogInFail, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiToken> call, Throwable t) {
                            Toast.makeText(SignInActivity.this, R.string.apiConnectionFail, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void getUser(ApiToken apiToken, String email, String password) {
        ApiService.getInstance().searchUser("Bearer " + apiToken.getApiToken(), email).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    User user = response.body().get(0);
                    user.setApiToken(apiToken);
                    user.setPassword(password);

                    User.getUser().updateUser(user);

                    Toast.makeText(getApplicationContext(), R.string.correctSignIn, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    public void onClickGoToSignup(View view) {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}