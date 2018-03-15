package com.example.tony.simpletwitter.viewModels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.tony.simpletwitter.BR;
import com.example.tony.simpletwitter.models.TweetModel;

/**
 * Created by Tony on 3/15/2018.
 */

public class TweetViewModel extends BaseObservable {
    private Context mContext;
    private TweetModel mTweet;

    public TweetViewModel(Context mContext, TweetModel mTweet) {
        this.mContext = mContext;
        this.mTweet = mTweet;
    }
    @Bindable
    public String getTweetText(){
        return mTweet.getText();
    }
    public void setTweetText(String tweetText){
        mTweet.setText(tweetText);
        notifyPropertyChanged(BR.tweetText);
    }
}