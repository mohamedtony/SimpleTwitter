package com.example.tony.simpletwitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
    List<Follower> twitterFriends;
    ArrayList friendsList = new ArrayList();
    private TwitterLoginButton loginButton;
    private long loggedUserTwitterId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        login();
    }

    public void login() {

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
                        friendsList.add(twitterFriends.get(k).getName());
                        Log.e("Twitter Friends", "Id:" + twitterFriends.get(k).getId() + " Name:" + twitterFriends.get(k).getName() + " pickUrl:" + twitterFriends.get(k).getProfilePictureUrl() + " desc " + twitterFriends.get(k).getDescription());
                    }
                    //  listAdapter.notifyDataSetChanged();
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
}
