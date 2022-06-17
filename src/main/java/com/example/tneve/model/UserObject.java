package com.example.tneve.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserObject {
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("password")
    private String password;

    public UserObject(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
