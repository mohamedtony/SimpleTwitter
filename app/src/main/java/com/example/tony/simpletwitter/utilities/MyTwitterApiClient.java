package com.example.tony.simpletwitter.utilities;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

/**
 * Created by Tony on 3/13/2018.
 */

public class MyTwitterApiClient extends TwitterApiClient {

    public MyTwitterApiClient(TwitterSession session) {
        super(session);
    }

    /**
     * Provide CustomService with defined endpoints
     */
    public MyCustomService getMyCustomService() {
        return getService(MyCustomService.class);
    }

}

