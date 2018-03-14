package com.example.tony.simpletwitter;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.tony.simpletwitter.adapters.FollowersAdapter;
import com.example.tony.simpletwitter.contentProvider.FollowerContract;
import com.example.tony.simpletwitter.models.Follower;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class FollowersActivity extends AppCompatActivity {
    private List<Follower> twitterFriends = new ArrayList<>();
    private ArrayList<Follower> twitterFreindsArrayList = new ArrayList<>();
    private ArrayList friendsList = new ArrayList();
    private TwitterLoginButton loginButton;
    private long loggedUserTwitterId;
    private RecyclerView recyclerView;
    private FollowersAdapter adapter;

    private String KEY_POSITION_ING = "KeyPositionIng";
    private static final String BUNDLE_RECYCLER_LAYOUT = "m.recycler.layout";
    private final String KEY_RECYCLER_STATE = "recycler_state";

    private static Bundle mBundleRecyclerViewState = null;
    public Parcelable listState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        //============================ for Recycler view==============
        recyclerView = findViewById(R.id.recycler_view);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        adapter = new FollowersAdapter(FollowersActivity.this, twitterFreindsArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    public void getFollowerList() {

        //Creating a twitter session with result's data
        //   final TwitterSession session = result.data;


        final TwitterSession session = TwitterCore
                .getInstance()
                .getSessionManager()
                .getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();
        loggedUserTwitterId = session.getUserId();
        MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(session);
        myTwitterApiClient.getMyCustomService().list(loggedUserTwitterId).enqueue(new retrofit2.Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("onResponse", response.toString());

                if (response.code() == 200) {
                    twitterFriends = fetchResults(response);

                    Log.e("onResponse", "twitterfriends:" + twitterFriends.size());

                    for (int k = 0; k < twitterFriends.size(); k++) {

                        twitterFreindsArrayList.add(twitterFriends.get(k));

                        friendsList.add(twitterFriends.get(k).getName());
                        Log.e("Twitter Friends", "Id:" + twitterFriends.get(k).getId() + " Name:" + twitterFriends.get(k).getName() + " pickUrl:" + twitterFriends.get(k).getProfilePictureUrl() + " desc " + twitterFriends.get(k).getDescription());
                    }
                    //  listAdapter.notifyDataSetChanged();
                    adapter.notifyDataSetChanged();
                    saveTOCache();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("onFailure", t.toString());
            }

        });

    }

    private void saveTOCache() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                for (int k = 0; k < twitterFreindsArrayList.size(); k++) {

                    if (getQuery(twitterFreindsArrayList.get(k).getId())) {
                      //  Toast.makeText(FollowersActivity.this, " add before " + twitterFreindsArrayList.get(k).getName(), Toast.LENGTH_SHORT).show();
                        Log.d(" m add",twitterFreindsArrayList.get(k).getName());
                    } else {

                        ContentValues values = new ContentValues();
                        values.put(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_NAME, twitterFreindsArrayList.get(k).getName());
                        values.put(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_ID, twitterFreindsArrayList.get(k).getId());
                        values.put(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_PHOTOPATH, twitterFreindsArrayList.get(k).getProfilePictureUrl());
                        values.put(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_DESC, twitterFreindsArrayList.get(k).getDescription());

                        Uri uri = getContentResolver().insert(FollowerContract.CONTENT_URI, values);
                        if (uri != null) {
                          //  Toast.makeText(FollowersActivity.this, " success " + uri.toString(), Toast.LENGTH_SHORT).show();
                            Log.d(" success " , uri.toString());

                        }
                    }

                }

                return null;
            }
        }.execute();


    }

    private List fetchResults(Response response) {
        com.example.tony.simpletwitter.models.FollowersResponse responseModel = (com.example.tony.simpletwitter.models.FollowersResponse) response.body();
        return responseModel.getUsers();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getNetwork()) {
            // restore RecyclerView state
            if (mBundleRecyclerViewState != null) {
                if (mBundleRecyclerViewState.getParcelableArrayList("m") != null) {
                    twitterFreindsArrayList = mBundleRecyclerViewState.getParcelableArrayList("m");
                    adapter = new FollowersAdapter(FollowersActivity.this, twitterFreindsArrayList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    //Toast.makeText(this, " Not null "+twitterFreindsArrayList.get(0).getName(), Toast.LENGTH_SHORT).show();
                }

                Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                recyclerView.getLayoutManager().onRestoreInstanceState(listState);

                mBundleRecyclerViewState = null;
            } else {
                getFollowerList();
            }
        } else {
            loadFromSqlit();
        }

    }

    private void loadFromSqlit() {

        Cursor cursor = getContentResolver().query(FollowerContract.CONTENT_URI, null, null, null, null);
        // ToDo: Just for logging, you can remove
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            Follower follower = new Follower();
            follower.setName(cursor.getString(cursor.getColumnIndex(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_NAME)));
            follower.setId(cursor.getLong(cursor.getColumnIndex(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_ID)));
            follower.setProfilePictureUrl(cursor.getString(cursor.getColumnIndex(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_PHOTOPATH)));
            follower.setDescription(cursor.getString(cursor.getColumnIndex(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_DESC)));
            twitterFreindsArrayList.add(follower);
        }
        adapter = new FollowersAdapter(FollowersActivity.this, twitterFreindsArrayList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        mBundleRecyclerViewState.putParcelableArrayList("m", twitterFreindsArrayList);
        // mBundleRecyclerViewState.putParcelableArrayList("m",twitterFriends);
        //   mBundleRecyclerViewState.putParcelableArrayList("FollowersList", (ArrayList<? extends Parcelable>) twitterFriends);

    }

    public boolean getQuery(long id) {
        Cursor cursor = getContentResolver().query(FollowerContract.CONTENT_URI,
                new String[]{FollowerContract.FollowersEntry.COLUMN_FOLLOWER_ID},
                FollowerContract.FollowersEntry.COLUMN_FOLLOWER_ID + "=?",
                new String[]{String.valueOf(id)},
                null);

        //  Toast.makeText(this, "cursor=" + cursor.toString(), Toast.LENGTH_SHORT).show();
        return (cursor != null) && (cursor.getCount() > 0);
    }

    public boolean getNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        Boolean stateChanged = activeNetwork != null && activeNetwork.isConnected();

        return stateChanged;
    }
}
