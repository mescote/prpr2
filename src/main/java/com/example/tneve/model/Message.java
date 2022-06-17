package com.example.tneve.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Message {
    @Expose(serialize = false)
    @SerializedName("id")
    private long mMessageId;
    @Expose
    @SerializedName("content")
    private String mContent;
    @Expose
    @SerializedName("user_id_send")
    private long mSenderId;
    @Expose
    @SerializedName("user_id_recived")
    private long mReceiverId;
    @Expose(serialize = false)
    @SerializedName("timeStamp")
    private Date mDate;

    public Message() {

    }

    public Message(String content, long senderId, long receiverId, Date date) {
        this.mContent = content;
        this.mSenderId = senderId;
        this.mReceiverId = receiverId;
        this.mDate = date;
    }

    public long getMessageId() {
        return mMessageId;
    }

    public void setMessageId(long messageId) {
        this.mMessageId = messageId;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public long getSenderId() {
        return mSenderId;
    }

    public void setSenderId(long senderId) {
        this.mSenderId = senderId;
    }

    public long getReceiverId() {
        return mReceiverId;
    }

    public void setReceiverId(long receiverId) {
        this.mReceiverId = receiverId;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }
}
