package com.example.tneve.model;

import com.example.tneve.api.ApiToken;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static User mUser;

    @Expose
    @SerializedName("image")
    private String mImage;

    @Expose
    @SerializedName("name")
    private String mFirstName;

    @Expose
    @SerializedName("last_name")
    private String mLastName;

    @Expose
    @SerializedName("email")
    private String mEmail;

    @Expose
    @SerializedName("password")
    private String mPassword;

    private ApiToken mApiToken;
    @Expose(serialize = false)
    @SerializedName("id")
    private long mId;

    private List<User> mFriendships = new ArrayList<>();
    private List<User> mFriendshipRequests = new ArrayList<>();

    public User() {
    }

    public User(String image, String firstName, String lastName, String email, String password) {
        this.mImage = image;
        this.mFirstName = firstName;
        this.mLastName = lastName;
        this.mEmail = email;
        this.mPassword = password;
    }

    public static User getUser() {
        if (mUser == null) {
            mUser = new User();
        }
        return mUser;
    }

    public static void nullUser() {
        mUser = null;
    }

    public String toString()
    {
        return String.format("User[ID:%d, name=%s, surname=%s", mId, mFirstName, mLastName);
    }

    public void updateUser(User user) {
        this.mId = user.mId;
        this.mApiToken = user.mApiToken;
        this.mFirstName = user.mFirstName;
        this.mLastName = user.mLastName;
        this.mEmail = user.mEmail;
        this.mPassword = user.mPassword;
        this.mImage = user.mImage;
    }

    public String getApiToken() {
        return mApiToken.getApiToken();
    }

    public void setApiToken(ApiToken apiToken) {
        apiToken.extension();
        this.mApiToken = apiToken;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public String getName() {
        return mFirstName;
    }

    public void setName(String firstName) {
        this.mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }


    /**
     *
     * @return
     */
    public List<User> getFriendships() {
        return mFriendships;
    }

    /**
     *
     * @param friends
     */
    public void setFriendships(List<User> friends) {
        mFriendships = friends;
    }

    /**
     *
     * @return
     */
    public List<User> getFriendshipRequests() {
        return mFriendshipRequests;
    }

    /**
     *
     * @param users
     */
    public void setFriendRequests(List<User> users)
    {
        mFriendshipRequests = users;
    }
}
