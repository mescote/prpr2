package com.example.tneve.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Event {
    private static Event mEvent;

    @Expose(serialize = false)
    @SerializedName("id")
    private long mId;

    @Expose
    @SerializedName("name")
    private String mName;

    @Expose
    @SerializedName("image")
    private String mImage;

    @Expose
    @SerializedName("location")
    private String mLocation;
    private Date mCreationDate;

    @Expose
    @SerializedName("description")
    private String mDescription;

    @Expose
    @SerializedName("eventStart_date")
    private String mStartDate;

    @Expose
    @SerializedName("eventEnd_date")
    private String mEndDate;

    @Expose
    @SerializedName("n_participators")
    private int mTotalParticipants;
    private int mCurrentParticipants;

    @Expose
    @SerializedName("type")
    private String mType;

    @Expose(serialize = false)
    @SerializedName("owner_id")
    private long mOwnerId;

    public Event() {

    }

    public Event(String name, String type, String description, int totalParticipants, String location, String image, String startDate, String endDate) {
        this.mName = name;
        this.mType = type;
        this.mDescription = description;
        this.mTotalParticipants = totalParticipants;
        this.mLocation = location;
        //this.mImage = "null";
        this.mImage = "https://i.imgur.com/ghy8Xx1.png";
        this.mStartDate = startDate;
        this.mEndDate = endDate;

        this.mCurrentParticipants = 0;
        this.mCreationDate = new Date();
    }

    public static Event getEvent() {
        if (mEvent == null) {
            mEvent = new Event();
        }

        return mEvent;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public int getTotalParticipants() {
        return mTotalParticipants;
    }

    public void setTotalParticipants(int totalParticipants) {
        this.mTotalParticipants = totalParticipants;
    }

    public int getCurrentParticipants() {
        return mCurrentParticipants;
    }

    public void setCurrentParticipants(int currentParticipants) {
        this.mCurrentParticipants = currentParticipants;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public Date getCreationDate() {
        return mCreationDate;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String startDate) {
        this.mStartDate = startDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public void setEndDate(String endDate) {
        this.mEndDate = endDate;
    }

    public long getOwnerId() {
        return mOwnerId;
    }

    public void setOwnerId(long ownerId) {
        this.mOwnerId = ownerId;
    }

}
