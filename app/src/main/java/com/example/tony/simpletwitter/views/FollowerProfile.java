package com.example.tony.simpletwitter.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tony.simpletwitter.MyTwitterApiClient;
import com.example.tony.simpletwitter.R;
import com.example.tony.simpletwitter.adapters.FollowersAdapter;
import com.example.tony.simpletwitter.adapters.TweetAdapter;
import com.example.tony.simpletwitter.databinding.ActivityFollowerProfileBinding;
import com.example.tony.simpletwitter.databinding.ContentNestedscrollBinding;
import com.example.tony.simpletwitter.databinding.FollowerItemBinding;
import com.example.tony.simpletwitter.models.Follower;
import com.example.tony.simpletwitter.models.TweetModel;
import com.example.tony.simpletwitter.viewModels.FollowerProfileViewModel;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;
import com.vatsal.imagezoomer.ZoomAnimation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowerProfile extends AppCompatActivity {
    private ArrayList<TweetModel> myTweets=new ArrayList<>();
    private String screenName;
    private CollapsingToolbarLayout collapsingToolbarLayout;
   private ZoomAnimation zoomAnimation;
   private Follower mFollower;
   private TweetAdapter tweetAdapter;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState = null;
    public Parcelable listState;
    private  RecyclerView recyclerView;
    private NestedScrollView nestedScrollView;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_follower_profile);
        if(getIntent().getExtras()!=null){
            screenName=getIntent().getExtras().get("screen_name").toString();
            //   getSupportActionBar().setTitle(screenName);
            if(getIntent().getExtras().containsKey("follower_obj")) {
                mFollower = getIntent().getExtras().getParcelable("follower_obj");
                if(mFollower!=null) {
                    Toast.makeText(FollowerProfile.this, " name " + mFollower.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        ActivityFollowerProfileBinding binding= DataBindingUtil.setContentView(this,R.layout.activity_follower_profile);
        FollowerProfileViewModel loginViewModel= new FollowerProfileViewModel(FollowerProfile.this,mFollower);
        binding.setFollowerProfile(loginViewModel);






        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
       // actionBar.setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
       /* collapsingToolbarLayout.setTitle("Tony");*/
        collapsingToolbarLayout.setTitleEnabled(false);
        collapsingToolbarLayout.setContentScrimColor(Color.parseColor("#00aced"));
       // actionBar.setTitle("My Title");
       actionBar.setTitle("");

        AppBarLayout appBarLayout=findViewById(R.id.myAppBar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
                {
                    //  Collapsed
                    actionBar.setTitle("@"+screenName);
                //    Toast.makeText(FollowerProfile.this, " Collapsed ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Expanded
                   actionBar.setTitle("");

                }
            }
        });

toolbar.setNavigationOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        FollowerProfile.this.finish();
    }
});


//recycler view

        nestedScrollView=findViewById(R.id.activity_main_nestedscrollview);
         recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tweetAdapter = new TweetAdapter(FollowerProfile.this, myTweets);
        recyclerView.setAdapter(tweetAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


//////////============================================click on image =======================================
        final Activity activity = getParent();
        ImageButton imageButton = (ImageButton) findViewById(R.id.userProfile);
        final long duration = 200;

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 zoomAnimation = new ZoomAnimation(FollowerProfile.this);
                zoomAnimation.zoom(v,duration);
                //zoomAnimation.set1.end();
            }
        });

/*        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZoomAnimation zoomAnimation = new ZoomAnimation(activity);
                zoomAnimation.zoomReverse(v, duration);
            }
        });*/
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getNetwork()) {
            // restore RecyclerView state
            if (mBundleRecyclerViewState != null) {
                if (mBundleRecyclerViewState.getParcelableArrayList("m") != null) {
                    myTweets = mBundleRecyclerViewState.getParcelableArrayList("m");
                    tweetAdapter = new TweetAdapter(FollowerProfile.this, myTweets);
                    recyclerView.setAdapter(tweetAdapter);
                    tweetAdapter.notifyDataSetChanged();
                    //Toast.makeText(this, " Not null "+twitterFreindsArrayList.get(0).getName(), Toast.LENGTH_SHORT).show();
                }

                Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                recyclerView.getLayoutManager().onRestoreInstanceState(listState);

                mBundleRecyclerViewState = null;
            } else {
               getTweetList();
            }
        } else {
            //loadFromSqlit();
        }

    }
    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        mBundleRecyclerViewState.putParcelableArrayList("m", myTweets);
        // mBundleRecyclerViewState.putParcelableArrayList("m",twitterFriends);
        //   mBundleRecyclerViewState.putParcelableArrayList("FollowersList", (ArrayList<? extends Parcelable>) twitterFriends);

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

    public void getTweetList() {

        final TwitterSession session = TwitterCore
                .getInstance()
                .getSessionManager()
                .getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();
        // loggedUserTwitterId = session.getUserId();
        MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(session);

        Call<List<TweetModel>> userCall = myTwitterApiClient.getMyCustomService().list(screenName,10);
        userCall.enqueue(new Callback<List<TweetModel>>() {
            @Override
            public void onResponse(Call<List<TweetModel>> call, Response<List<TweetModel>> response) {
                if (response.body()!= null) {
                    myTweets.addAll(response.body());
                    tweetAdapter = new TweetAdapter(FollowerProfile.this, myTweets);
                    recyclerView.setAdapter(tweetAdapter);
                    tweetAdapter.notifyDataSetChanged();
                   // tweetAdapter.notifyDataSetChanged();
                    Log.i(getClass().getSimpleName() + "Tweet List", myTweets.toString());
                }/*else myTweets.add("Sorry no tweets retrieved for this user");*/
               /* tweetListAdapter =  new TweetListAdapter((ArrayList)tweets,context);
                listView.setAdapter(tweetListAdapter);*/
            }

            @Override
            public void onFailure(Call<List<TweetModel>> call, Throwable t) {
                Log.e(getClass().getSimpleName() + "  failure" , call.toString());
                Log.e(getClass().getSimpleName() + "  failure" , t.getMessage());


            }
        });
    }
}
