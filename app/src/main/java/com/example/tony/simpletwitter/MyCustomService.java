package com.example.tony.simpletwitter;

import com.example.tony.simpletwitter.models.FollowersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Tony on 3/13/2018.
 */

public interface MyCustomService {
    @GET("1.1/followers/list.json")
    Call<FollowersResponse> list(@Query("user_id") long id);
}
