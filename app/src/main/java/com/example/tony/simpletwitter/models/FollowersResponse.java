package com.example.tony.simpletwitter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tony on 3/13/2018.
 */

public class FollowersResponse {
    @SerializedName("users")
    @Expose
    private List<Follower> users = new ArrayList<>();
    @SerializedName("next_cursor")
    @Expose
    private Long nextCursor;
    @SerializedName("next_cursor_str")
    @Expose
    private String nextCursorStr;
    @SerializedName("previous_cursor")
    @Expose
    private Integer previousCursor;
    @SerializedName("previous_cursor_str")
    @Expose
    private String previousCursorStr;

    public List<Follower> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Follower> users) {
        this.users = users;
    }

    public Long getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(Long nextCursor) {
        this.nextCursor = nextCursor;
    }

    public String getNextCursorStr() {
        return nextCursorStr;
    }

    public void setNextCursorStr(String nextCursorStr) {
        this.nextCursorStr = nextCursorStr;
    }

    public Integer getPreviousCursor() {
        return previousCursor;
    }

    public void setPreviousCursor(Integer previousCursor) {
        this.previousCursor = previousCursor;
    }

    public String getPreviousCursorStr() {
        return previousCursorStr;
    }

    public void setPreviousCursorStr(String previousCursorStr) {
        this.previousCursorStr = previousCursorStr;
    }

}

