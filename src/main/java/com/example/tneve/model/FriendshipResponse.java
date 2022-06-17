package com.example.tneve.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FriendshipResponse
{
    @Expose
    @SerializedName("fieldCount")
    private int mFieldCount;

    @Expose
    @SerializedName("affectedRows")
    private int mAffectedRows;

    @Expose
    @SerializedName("insertId")
    private int mInsertId;

    @Expose
    @SerializedName("info")
    private String mInfo;

    @Expose
    @SerializedName("serverStatus")
    private int mServerStatus;

    @Expose
    @SerializedName("warningStatus")
    private int mWarningStatus;

    public FriendshipResponse(
        int fieldCount,
        int affectedRows,
        int insertId,
        String info,
        int serverStatus,
        int warningStatus
    )
    {
        mFieldCount = fieldCount;
        mAffectedRows = affectedRows;
        mInsertId = insertId;
        mInfo = info;
        mServerStatus = serverStatus;
        mWarningStatus = warningStatus;
    }
}
