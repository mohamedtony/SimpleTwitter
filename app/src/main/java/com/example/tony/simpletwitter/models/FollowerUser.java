package com.example.tony.simpletwitter.models;

/**
 * Created by Tony on 3/14/2018.
 */

public class FollowerUser {
    private String followerName;
    private String followerPhoto;
    private String followerDesc;

    public FollowerUser(String followerName, String followerPhoto, String followerDesc) {
        this.followerName = followerName;
        this.followerPhoto = followerPhoto;
        this.followerDesc = followerDesc;
    }

    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }

    public String getFollowerPhoto() {
        return followerPhoto;
    }

    public void setFollowerPhoto(String followerPhoto) {
        this.followerPhoto = followerPhoto;
    }

    public String getFollowerDesc() {
        return followerDesc;
    }

    public void setFollowerDesc(String followerDesc) {
        this.followerDesc = followerDesc;
    }
}
