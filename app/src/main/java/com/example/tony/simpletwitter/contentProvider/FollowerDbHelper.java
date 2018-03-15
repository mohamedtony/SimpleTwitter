package com.example.tony.simpletwitter.contentProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tony on 3/14/2018.
 */

public class FollowerDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_NAME = "follower.db";

    public FollowerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        final String SQL_CREATE_FOLLOWER_TABLE = "CREATE TABLE " + FollowerContract.FollowersEntry.TABLE_NAME + " (" +
                FollowerContract.FollowersEntry.COLUMN_FOLLOWER_NAME + " TEXT NOT NULL, " +
                FollowerContract.FollowersEntry.COLUMN_FOLLOWER_ID+ " LONG NOT NULL UNIQUE, " +
                FollowerContract.FollowersEntry.COLUMN_FOLLOWER_PHOTOPATH + " TEXT, " +
                FollowerContract.FollowersEntry.COLUMN_FOLLOWER_DESC + " TEXT, "+
                FollowerContract.FollowersEntry.COLUMN_FOLLOWER_BACK_PHOTO + " TEXT, " +
                FollowerContract.FollowersEntry.COLUMN_FOLLOWER_SCREEN_NAME + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_FOLLOWER_TABLE);

        final String SQL_CREATE_TWEET_TABLE = "CREATE TABLE " + TweetContract.TweetsEntry.TABLE_NAME + " (" +
                TweetContract.TweetsEntry.COLUMN_FOLLOWER_ID+ " LONG NOT NULL, " +
                TweetContract.TweetsEntry.COLUMN_SCREEN_NAME + " TEXT NOT NULL, " +
                TweetContract.TweetsEntry.COLUMN_TWEET_ID+ " LONG NOT NULL UNIQUE, " +
                TweetContract.TweetsEntry.COLUMN_TWEET_TEXT + " TEXT);";
        db.execSQL(SQL_CREATE_TWEET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FollowerContract.FollowersEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +  TweetContract.TweetsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
