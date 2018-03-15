package com.example.tony.simpletwitter.contentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Tony on 3/15/2018.
 */

public class TweetProvider extends ContentProvider {
    static final int CODE_TWEETS= 200;
    static final int CODE_TWEET_ID = 201;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FollowerDbHelper mOpenHelper;

    static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TweetContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, TweetContract.PATH_TWEETS, CODE_TWEETS);
        matcher.addURI(authority, TweetContract.PATH_TWEETS + "/#", CODE_TWEET_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        Context context=getContext();
        mOpenHelper = new FollowerDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor mCursor = null;
        SQLiteDatabase database = mOpenHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        switch (match) {

            case CODE_TWEETS:
                mCursor = database.query(TweetContract.TweetsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new IllegalStateException("cant Query this URI ! ");

        }

        mCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return mCursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case CODE_TWEETS:
                long id = db.insert(TweetContract.TweetsEntry.TABLE_NAME, null, contentValues);
                if ( id > 0 )
                {
                    returnUri = ContentUris.withAppendedId(TweetContract.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default: {
                throw new UnsupportedOperationException("Unknown uri:  " + uri);


            }
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;

        switch (sUriMatcher.match(uri)) {

            case CODE_TWEETS:
                selection = TweetContract.TweetsEntry.COLUMN_TWEET_ID + "=?";
                // selectionArgs = new String[]
                // {String.valueOf(ContentUris.parseId(uri))};
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        TweetContract.TweetsEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
