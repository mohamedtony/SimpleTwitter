package com.example.tony.simpletwitter;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.tony.simpletwitter.adapters.FollowersAdapter;
import com.example.tony.simpletwitter.models.Follower;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class FollowersActivity extends AppCompatActivity {
    private List<Follower> twitterFriends=new ArrayList<>();
    private ArrayList<Follower>twitterFreindsArrayList=new ArrayList<>();
    private ArrayList friendsList = new ArrayList();
    private TwitterLoginButton loginButton;
    private long loggedUserTwitterId;
    private RecyclerView recyclerView;
    private  FollowersAdapter adapter;

    private String KEY_POSITION_ING = "KeyPositionIng";
    private static final String BUNDLE_RECYCLER_LAYOUT = "m.recycler.layout";
    private final String KEY_RECYCLER_STATE = "recycler_state";

    private static Bundle mBundleRecyclerViewState=null;
    public Parcelable listState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        //============================ for Recycler view==============
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FollowersAdapter(FollowersActivity.this, twitterFreindsArrayList);
        recyclerView.setAdapter(adapter);




    }

    public void getFollowerList() {

        //Creating a twitter session with result's data
        //   final TwitterSession session = result.data;


        final TwitterSession session = TwitterCore
                .getInstance()
                .getSessionManager()
                .getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();
        loggedUserTwitterId=session.getUserId();
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
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("onFailure", t.toString());
            }

        });

    }

    private List fetchResults(Response response) {
        com.example.tony.simpletwitter.models.FollowersResponse responseModel = (com.example.tony.simpletwitter.models.FollowersResponse) response.body();
        return responseModel.getUsers();
    }
    @Override
    public void onStart()
    {
        super.onStart();

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            if(mBundleRecyclerViewState.getParcelableArrayList("m")!=null){
                twitterFreindsArrayList=mBundleRecyclerViewState.getParcelableArrayList("m");
                adapter = new FollowersAdapter(FollowersActivity.this, twitterFreindsArrayList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //Toast.makeText(this, " Not null "+twitterFreindsArrayList.get(0).getName(), Toast.LENGTH_SHORT).show();
            }

            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);

            mBundleRecyclerViewState=null;
        }else{
            getFollowerList();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        mBundleRecyclerViewState.putParcelableArrayList("m",twitterFreindsArrayList);
       // mBundleRecyclerViewState.putParcelableArrayList("m",twitterFriends);
     //   mBundleRecyclerViewState.putParcelableArrayList("FollowersList", (ArrayList<? extends Parcelable>) twitterFriends);

    }
}
