package com.example.tony.simpletwitter.contentProvider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Tony on 3/14/2018.
 */

public class FollowerContract   {

    public static final String CONTENT_AUTHORITY = "com.example.tony.simpletwitter.contentProvider.FollowersProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FOLLOWERS = "followers";


    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FOLLOWERS).build();
    public static class FollowersEntry implements BaseColumns {
        public static final String TABLE_NAME = "followers";
        public static final String COLUMN_FOLLOWER_ID = "follower_id";
        public static final String COLUMN_FOLLOWER_NAME = "follower_name";
        public static final String COLUMN_FOLLOWER_PHOTOPATH= "follower_photo";
        public static final String COLUMN_FOLLOWER_DESC = "follower_desc";
        public static final String COLUMN_FOLLOWER_BACK_PHOTO = "follower_back";
        public static final String COLUMN_FOLLOWER_SCREEN_NAME = "follower_screen";

    }

}
