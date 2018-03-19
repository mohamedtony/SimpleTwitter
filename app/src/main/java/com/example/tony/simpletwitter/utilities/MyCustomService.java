package com.example.tony.simpletwitter.utilities;

import com.example.tony.simpletwitter.models.FollowersResponse;
import com.example.tony.simpletwitter.models.TweetModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Tony on 3/13/2018.
 */

public interface MyCustomService {
    @GET("1.1/followers/list.json")
    Call<FollowersResponse> list(@Query("user_id") long id);

    @GET("/1.1/statuses/user_timeline.json")
    Call<List<TweetModel>> list(
            @Query("screen_name") String screen_name,
            @Query("count") int count);
}
