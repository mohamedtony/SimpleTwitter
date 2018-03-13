package com.example.tony.simpletwitter;

import android.app.Application;
import android.util.Log;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

/**
 * Created by Tony on 3/13/2018.
 */

public class SimpleTwitter extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("eIvJW0unWhtbJdud5IuW6NmnF", "mpTKcZwt88UxXQeMLWP6XAia9UAJ85lpXlerCCBIygobXcXbG7"))
                .debug(true)
                .build();
        Twitter.initialize(config);

    }
}
