package com.example.tneve.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiToken {
    @Expose
    @SerializedName("accessToken")
    private String apiToken;

    public ApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void extension() {
        if (apiToken != null && !apiToken.startsWith("Bearer")) {
            this.apiToken = "Bearer " + apiToken;
        }
    }
}
