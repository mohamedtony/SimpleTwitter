package com.example.tony.simpletwitter.contentProvider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Tony on 3/15/2018.
 */

public class TweetContract {

    public static final String CONTENT_AUTHORITY = "com.example.tony.simpletwitter.contentProvider.TweetProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_TWEETS = "tweets";


    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TWEETS).build();

    public static class TweetsEntry implements BaseColumns {
        public static final String TABLE_NAME = "tweets";

        public static final String COLUMN_SCREEN_NAME = "screen_name";
        public static final String COLUMN_TWEET_ID = "tweet_id";
        public static final String COLUMN_TWEET_TEXT= "tweet_text";
        public static final String COLUMN_FOLLOWER_ID= "follower_id";

    }

}
