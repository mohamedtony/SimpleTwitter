package com.example.tony.simpletwitter;

import android.app.Application;
import android.util.Log;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
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
        ChangeLanguage changeLanguage=new ChangeLanguage(this);
        changeLanguage.loadLocale();


        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("eIvJW0unWhtbJdud5IuW6NmnF", "mpTKcZwt88UxXQeMLWP6XAia9UAJ85lpXlerCCBIygobXcXbG7"))
                .debug(true)
                .build();
        Twitter.initialize(config);


        //======================================= to chache images when no network connectivity===============================
        Picasso.Builder builder=new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built=builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

    }
}
