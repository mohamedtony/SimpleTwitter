package com.example.tony.simpletwitter.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Tony on 3/13/2018.
 */

public class Follower implements Parcelable{
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("profile_image_url_https")
    @Expose
    private String profilePictureUrl;

    @SerializedName("screen_name")
    @Expose
    private String screenName;


    @SerializedName("profile_background_image_url_https")
    @Expose
    private String profileBackgroundPictureUrl;

    protected Follower(Parcel in) {
        id = in.readLong();
        description = in.readString();
        name = in.readString();
        profilePictureUrl = in.readString();
        screenName = in.readString();
        profileBackgroundPictureUrl = in.readString();
    }

    public static final Creator<Follower> CREATOR = new Creator<Follower>() {
        @Override
        public Follower createFromParcel(Parcel in) {
            return new Follower(in);
        }

        @Override
        public Follower[] newArray(int size) {
            return new Follower[size];
        }
    };

    public String getProfileBackgroundPictureUrl() {
        return profileBackgroundPictureUrl;
    }

    public void setProfileBackgroundPictureUrl(String profileBackgroundPictureUrl) {
        this.profileBackgroundPictureUrl = profileBackgroundPictureUrl;
    }


    public Follower(){

    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeLong(id);
        parcel.writeString(description);
        parcel.writeString(name);
        parcel.writeString(profilePictureUrl);
        parcel.writeString(screenName);
        parcel.writeString(profileBackgroundPictureUrl);
    }

}


