package com.example.tony.simpletwitter.views;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tony.simpletwitter.utilities.ChangeLanguage;
import com.example.tony.simpletwitter.utilities.ItemOffsetDecoration;
import com.example.tony.simpletwitter.utilities.MyTwitterApiClient;
import com.example.tony.simpletwitter.R;
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
    private static Bundle mBundleRecyclerViewState = null;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    public Parcelable listState;
    private List<Follower> twitterFriends = new ArrayList<>();
    private ArrayList<Follower> twitterFreindsArrayList = new ArrayList<>();
    private ArrayList friendsList = new ArrayList();
    private TwitterLoginButton loginButton;
    private long loggedUserTwitterId;
    private RecyclerView recyclerView;
    private FollowersAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ChangeLanguage changeLang;
    private static String SHARED_PREFS = "MyPref";
    private static String IS_LOGED_IN = "isLoged";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
       //    actionBar.setDisplayHomeAsUpEnabled(true);

        initViews();
        setUpRecyclerView();
        setUpSwipeToRefresh();
    }
    @Override
    public void onStart() {
        super.onStart();

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            if (mBundleRecyclerViewState.containsKey("twitter_arraylist")) {
                twitterFreindsArrayList = mBundleRecyclerViewState.getParcelableArrayList("twitter_arraylist");
                if (twitterFreindsArrayList != null && twitterFreindsArrayList.size() != 0) {

                    /*adapter = new FollowersAdapter(FollowersActivity.this, twitterFreindsArrayList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();*/
                    setUpRecyclerView();
                    adapter.notifyDataSetChanged();
                    changeLang.loadLocale();
                }
            }

            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);

            mBundleRecyclerViewState = null;
            swipeRefreshLayout.setRefreshing(false);
        } else {

            if (getNetwork()) {
                getFollowerList();
            } else {
                loadFromSqlit();
            }

        }
    }

    //===============================================================To save Recycler View State when rotate===========================

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        mBundleRecyclerViewState.putParcelableArrayList("twitter_arraylist", twitterFreindsArrayList);

    }


    //============================================== When Click back button toolbar==================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String lang="";
        switch (item.getItemId()) {
/*            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(FollowersActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;*/

            case R.id.english:
                lang = "en";
                finish();
                startActivity(getIntent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                changeLang.changeLang(lang);
                Toast.makeText(this, getString(R.string.lang_success), Toast.LENGTH_SHORT).show();
                return true;

            case R.id.arabic:
                lang = "ar";
                finish();
                startActivity(getIntent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                changeLang.changeLang(lang);
                Toast.makeText(this, getString(R.string.lang_success), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.log_out:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void logOut() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_LOGED_IN, false);
        editor.commit();
        Intent intent = new Intent(FollowersActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    //========================================================================================================
    private void initViews() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recycler_view);
        changeLang=new ChangeLanguage(this);
    }

    //==================================================================================
    private void setUpRecyclerView() {
        //============================ for Recycler view==============

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
           /* ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
            recyclerView.addItemDecoration(itemDecoration);*/

        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        adapter = new FollowersAdapter(FollowersActivity.this, twitterFreindsArrayList);
        recyclerView.setAdapter(adapter);
        // recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    //========================================================================================================
    private void setUpSwipeToRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your recyclerview reload logic function will be here!!!
                if (getNetwork()) {
                    getFollowerList();
                } else {
                    loadFromSqlit();
                }
            }
        });
    }

    public void getFollowerList() {

        //Creating a twitter session with result's data
        //==================================================Creating a twitter session with result's data====================
        //============== Ref: http://www.onlineicttutor.com/twitter-login-get-follower-list-send-message-android/ ===========
        twitterFreindsArrayList = new ArrayList<>();


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
                    //  twitterFreindsArrayList = new ArrayList<>();
                    twitterFriends = new ArrayList<>();
                    twitterFriends = fetchResults(response);

                    Log.e("onResponse", "twitterfriends:" + twitterFriends.size());

                    for (int k = 0; k < twitterFriends.size(); k++) {

                        Follower follower = new Follower();
                        follower.setScreenName(twitterFriends.get(k).getScreenName());
                        follower.setProfileBackgroundPictureUrl(twitterFriends.get(k).getProfileBackgroundPictureUrl());
                        follower.setDescription(twitterFriends.get(k).getDescription());
                        follower.setName(twitterFriends.get(k).getName());
                        follower.setProfilePictureUrl(twitterFriends.get(k).getProfilePictureUrl());
                        follower.setId(twitterFriends.get(k).getId());
                        twitterFreindsArrayList.add(follower);

                    }
                  /*  adapter = new FollowersAdapter(FollowersActivity.this, twitterFreindsArrayList);
                    recyclerView.setAdapter(adapter);*/
                    setUpRecyclerView();
                 //   adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
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
        //================================ saving to sqlite with content provider=============Ref : Android developer Nanodegree Scholarship ==================================


        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                for (int k = 0; k < twitterFreindsArrayList.size(); k++) {

                    if (getQuery(twitterFreindsArrayList.get(k).getId())) {
                        //  Toast.makeText(FollowersActivity.this, " add before " + twitterFreindsArrayList.get(k).getName(), Toast.LENGTH_SHORT).show();
                        Log.d(" m add", twitterFreindsArrayList.get(k).getName());
                    } else {

                        ContentValues values = new ContentValues();
                        values.put(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_NAME, twitterFreindsArrayList.get(k).getName());
                        values.put(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_ID, twitterFreindsArrayList.get(k).getId());
                        values.put(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_PHOTOPATH, twitterFreindsArrayList.get(k).getProfilePictureUrl());
                        values.put(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_DESC, twitterFreindsArrayList.get(k).getDescription());
                        values.put(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_BACK_PHOTO, twitterFreindsArrayList.get(k).getProfileBackgroundPictureUrl());
                        values.put(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_SCREEN_NAME, twitterFreindsArrayList.get(k).getScreenName());

                        Uri uri = getContentResolver().insert(FollowerContract.CONTENT_URI, values);
                        if (uri != null) {
                            //  Toast.makeText(FollowersActivity.this, " success " + uri.toString(), Toast.LENGTH_SHORT).show();
                            Log.d(" success ", uri.toString());

                        }
                    }

                }

                return null;
            }
        }.execute();


    }

    //======================================= To check is follower in sqlite or not ==========================================
    public boolean getQuery(long id) {
        Cursor cursor = getContentResolver().query(FollowerContract.CONTENT_URI,
                new String[]{FollowerContract.FollowersEntry.COLUMN_FOLLOWER_ID},
                FollowerContract.FollowersEntry.COLUMN_FOLLOWER_ID + "=?",
                new String[]{String.valueOf(id)},
                null);

        //  Toast.makeText(this, "cursor=" + cursor.toString(), Toast.LENGTH_SHORT).show();
        return (cursor != null) && (cursor.getCount() > 0);
    }

    //====================================================== To return list data response=====================================
    private List fetchResults(Response response) {
        com.example.tony.simpletwitter.models.FollowersResponse responseModel = (com.example.tony.simpletwitter.models.FollowersResponse) response.body();
        return responseModel.getUsers();
    }


    //=================================================== to retrieve data from sqlite ==================================================
    private void loadFromSqlit() {
        twitterFreindsArrayList = new ArrayList<>();
        Cursor cursor = getContentResolver().query(FollowerContract.CONTENT_URI, null, null, null, null);
        // ToDo: Just for logging, you can remove
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            Follower follower = new Follower();
            follower.setName(cursor.getString(cursor.getColumnIndex(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_NAME)));
            follower.setId(cursor.getLong(cursor.getColumnIndex(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_ID)));
            follower.setProfilePictureUrl(cursor.getString(cursor.getColumnIndex(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_PHOTOPATH)));
            follower.setDescription(cursor.getString(cursor.getColumnIndex(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_DESC)));
            follower.setProfileBackgroundPictureUrl(cursor.getString(cursor.getColumnIndex(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_BACK_PHOTO)));
            follower.setScreenName(cursor.getString(cursor.getColumnIndex(FollowerContract.FollowersEntry.COLUMN_FOLLOWER_NAME)));
            twitterFreindsArrayList.add(follower);
        }
      /*  adapter = new FollowersAdapter(FollowersActivity.this, twitterFreindsArrayList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();*/
        setUpRecyclerView();
        swipeRefreshLayout.setRefreshing(false);
    }


//======================================== To check network connectivity ==============================================

    public boolean getNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }

        return activeNetwork != null && activeNetwork.isConnected();
    }
}