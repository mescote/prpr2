package com.example.tneve;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tneve.api.ApiService;
import com.example.tneve.api.ApiToken;
import com.example.tneve.model.User;
import com.example.tneve.model.UserObject;

import java.io.FileNotFoundException;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    Button signUpButton;
    EditText mFirstName, mLastName, mEmail, mPassword, mImgUrl;
    int SELECT_PHOTO = 1;
    Uri uri;
    ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirstName = findViewById(R.id.first_name_signup);
        mLastName = findViewById(R.id.last_name_signup);
        mEmail = findViewById(R.id.email_signup);
        mPassword = findViewById(R.id.password_signup);
        mImage = findViewById(R.id.profile_image_signup);
        mImgUrl = findViewById(R.id.img_url);

        signUpButton = (Button) findViewById(R.id.button_signup);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = mFirstName.getText().toString();
                String lastName = mLastName.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                String image = mImgUrl.getText().toString();

                Glide.with(mImage)
                        .load(image)
                        .error(R.drawable.default_profile)
                        .into(mImage);

                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fields Required", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.length() < 8) {
                        mPassword.setText("");
                        Toast toast = Toast.makeText(getApplicationContext(),
                                R.string.password_length,
                                Toast.LENGTH_SHORT);
                        View toastView = toast.getView();
                        toast.show();
                    } else if (!password.matches("^[a-zA-Z0-9]*$")) {
                        mPassword.setText("");
                        Toast toast = Toast.makeText(getApplicationContext(),
                                R.string.password_alphanumeric,
                                Toast.LENGTH_SHORT);
                    } else {
                        if (image.isEmpty()) {
                            image = "null";
                        }
                        User user = new User(image, firstName, lastName, email, password);
                        ApiService.getInstance().registerUser(user).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    authenticationUser(user);
                                } else {
                                    Toast.makeText(SignUpActivity.this, R.string.apiSignUpFail, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(SignUpActivity.this, R.string.apiConnectionFail, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            }
        });

    }

    private void authenticationUser(User user) {
        UserObject userObject = new UserObject(user.getEmail(), user.getPassword());
        ApiService.getInstance().authenticationUser(userObject).enqueue(new Callback<ApiToken>() {
            @Override
            public void onResponse(Call<ApiToken> call, Response<ApiToken> response) {
                if (response.isSuccessful()) {
                    user.setApiToken(response.body());
                    User.getUser().updateUser(user);

                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, R.string.apiLogInFail, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiToken> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, R.string.apiConnectionFail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                mImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onClickGoToSignin(View view) {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
    }
}
