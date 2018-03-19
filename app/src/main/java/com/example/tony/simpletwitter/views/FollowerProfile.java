package com.example.tony.simpletwitter.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.tony.simpletwitter.utilities.ChangeLanguage;
import com.example.tony.simpletwitter.utilities.MyTwitterApiClient;
import com.example.tony.simpletwitter.R;
import com.example.tony.simpletwitter.adapters.TweetAdapter;
import com.example.tony.simpletwitter.contentProvider.TweetContract;
import com.example.tony.simpletwitter.databinding.ActivityFollowerProfileBinding;
import com.example.tony.simpletwitter.models.Follower;
import com.example.tony.simpletwitter.models.TweetModel;
import com.example.tony.simpletwitter.viewModels.FollowerProfileViewModel;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.vatsal.imagezoomer.ZoomAnimation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowerProfile extends AppCompatActivity {
    private static Bundle mBundleRecyclerViewState = null;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    public Parcelable listState;
    private ArrayList<TweetModel> followerTweets = new ArrayList<>();
    private String screenName;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private ZoomAnimation zoomAnimation;
    private Follower mFollower;
    private TweetAdapter tweetAdapter;
    private RecyclerView recyclerView;
    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;
    private ChangeLanguage changeLanguage;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_follower_profile);
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("follower_obj")) {
                mFollower = getIntent().getExtras().getParcelable("follower_obj");

            }
        }

        //=================================== for data binding ==============================================================

        ActivityFollowerProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_follower_profile);
        FollowerProfileViewModel loginViewModel = new FollowerProfileViewModel(FollowerProfile.this, mFollower);
        binding.setFollowerProfile(loginViewModel);


        initViews();
        setUpRecyclerview();


        //============================================== To Zoom An Image When Click ===========================================
        //============================================Ref: https://stackoverflow.com/questions/7693633/android-image-dialog-popup===
        //==================================================== Ref: https://github.com/MikeOrtiz/TouchImageView ========================
        final ImageView touchImageView=findViewById(R.id.userProfile);
        final ImageView bakground=findViewById(R.id.user_image_back);
        collapsingToolbarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Display display = getWindowManager().getDefaultDisplay();
                int width = display.getWidth();
                int height = display.getHeight();
                loadPhoto(bakground,width,height);
            }
        });
        bakground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Display display = getWindowManager().getDefaultDisplay();
                int width = display.getWidth();
                int height = display.getHeight();
                loadPhoto(bakground,width,height);
            }
        });

        touchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Display display = getWindowManager().getDefaultDisplay();
                int width = display.getWidth();
                int height = display.getHeight();
                loadPhoto(touchImageView,width,height);
            }
        });


    }

    //============================================== To Make Image Zoom ==================================
    private void loadPhoto(ImageView imageView, int width, int height) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //dialog.setContentView(R.layout.custom_fullimage_dialog);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_fullimage_dialoge,
                (ViewGroup) findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        image.setImageDrawable(imageView.getDrawable());
        image.getLayoutParams().height = height;
        image.getLayoutParams().width = width;
       PhotoViewAttacher mAttacher = new PhotoViewAttacher(image);
        image.requestLayout();
        dialog.setContentView(layout);
        dialog.show();

    }

    private void setUpRecyclerview() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tweetAdapter = new TweetAdapter(FollowerProfile.this, followerTweets);
        recyclerView.setAdapter(tweetAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void initViews() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = findViewById(R.id.myAppBar);
        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        nestedScrollView = findViewById(R.id.activity_main_nestedscrollview);
        recyclerView = findViewById(R.id.my_recycler_view);
        progressBar=(ProgressBar)findViewById(R.id.myProgress);
        progressBar.setVisibility(View.VISIBLE);
        changeLanguage=new ChangeLanguage(this);

        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            if(toolbar!=null) {
                toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }
        }


        collapsingToolbarLayout.setTitleEnabled(false);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));


        //=======================================get status of the toolbar Collapsed or Expanded ==============================
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    //  Collapsed
                    if (actionBar != null)
                        if (mFollower != null)
                            actionBar.setTitle("@" + mFollower.getScreenName());
                    //    Toast.makeText(FollowerProfile.this, " Collapsed ", Toast.LENGTH_SHORT).show();
                } else {
                    //Expanded
                    if (actionBar != null)
                        actionBar.setTitle("");

                }
            }
        });

        //============================================== When Click back button toolbar==================================
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FollowerProfile.this.finish();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();


        // ==========================================restore RecyclerView state======================================
        if (mBundleRecyclerViewState != null) {
            if(mBundleRecyclerViewState.containsKey("followerTweets")) {
                followerTweets = mBundleRecyclerViewState.getParcelableArrayList("followerTweets");
                if (followerTweets != null && followerTweets.size() != 0) {
                    tweetAdapter = new TweetAdapter(FollowerProfile.this, followerTweets);
                    recyclerView.setAdapter(tweetAdapter);
                    tweetAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
                    changeLanguage.loadLocale();
                }
            }

            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);

            mBundleRecyclerViewState = null;

        } else {
            if (getNetwork()) {
                getTweetList();
            } else {
                loadFromSqlit();
            }
        }

    }

    //=============================================== saving recyclerview state when rotate =====================================
    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        mBundleRecyclerViewState.putParcelableArrayList("followerTweets", followerTweets);

    }

    //======================================================get network status=====================================================
    public boolean getNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }

        return activeNetwork != null && activeNetwork.isConnected();
    }


    //============================================load tweet list from sqlite when no network ====================================================
    private void loadFromSqlit() {
        followerTweets = new ArrayList<>();

        Cursor cursor = getContentResolver().query(TweetContract.CONTENT_URI, null, null, null, null);
        // ToDo: Just for logging, you can remove
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            //  Toast.makeText(this, " you r in sqlite ", Toast.LENGTH_SHORT).show();
            if (cursor.getLong(cursor.getColumnIndex(TweetContract.TweetsEntry.COLUMN_FOLLOWER_ID)) == (mFollower.getId())) {

                TweetModel tweetModel = new TweetModel();
                tweetModel.setText(cursor.getString(cursor.getColumnIndex(TweetContract.TweetsEntry.COLUMN_TWEET_TEXT)));
                tweetModel.setId(cursor.getLong(cursor.getColumnIndex(TweetContract.TweetsEntry.COLUMN_TWEET_ID)));
                followerTweets.add(tweetModel);
            }
        }
        tweetAdapter = new TweetAdapter(FollowerProfile.this, followerTweets);
        recyclerView.setAdapter(tweetAdapter);
        tweetAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.INVISIBLE);
    }


    //=========================================== get tweet list from the api when there is network connectivity ==========================
    //========================================== Ref: https://github.com/twitter/twitter-kit-android/wiki/Show-Tweets======================
    public void getTweetList() {

        final TwitterSession session = TwitterCore
                .getInstance()
                .getSessionManager()
                .getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();
        // loggedUserTwitterId = session.getUserId();
        MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(session);

        if (mFollower != null) {
            Call<List<TweetModel>> userCall = myTwitterApiClient.getMyCustomService().list(mFollower.getScreenName(), 10);
            userCall.enqueue(new Callback<List<TweetModel>>() {
                @Override
                public void onResponse(Call<List<TweetModel>> call, Response<List<TweetModel>> response) {
                    if (response.body() != null) {
                        followerTweets.addAll(response.body());
                        tweetAdapter = new TweetAdapter(FollowerProfile.this, followerTweets);
                        recyclerView.setAdapter(tweetAdapter);
                        tweetAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<List<TweetModel>> call, Throwable t) {
                    Log.e(getClass().getSimpleName() + "  failure", call.toString());
                    Log.e(getClass().getSimpleName() + "  failure", t.getMessage());


                }
            });
        }
    }
}
