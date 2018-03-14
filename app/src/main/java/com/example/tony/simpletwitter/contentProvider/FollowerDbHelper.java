package com.example.tony.simpletwitter.contentProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tony on 3/14/2018.
 */

public class FollowerDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

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
                FollowerContract.FollowersEntry.COLUMN_FOLLOWER_DESC + " TEXT);";
        db.execSQL(SQL_CREATE_FOLLOWER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FollowerContract.FollowersEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
