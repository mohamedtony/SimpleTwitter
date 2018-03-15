package com.example.tony.simpletwitter.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tony.simpletwitter.R;
import com.example.tony.simpletwitter.databinding.FollowerItemBinding;
import com.example.tony.simpletwitter.databinding.TweetItemBinding;
import com.example.tony.simpletwitter.models.Follower;
import com.example.tony.simpletwitter.models.TweetModel;
import com.example.tony.simpletwitter.viewModels.FollowerViewModel;
import com.example.tony.simpletwitter.viewModels.TweetViewModel;
import com.example.tony.simpletwitter.views.FollowerProfile;

import java.util.ArrayList;

/**
 * Created by Tony on 3/15/2018.
 */

public class TweetAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<TweetModel> mTeets;

    public TweetAdapter(Context mContext, ArrayList<TweetModel> mTeets) {
        this.mContext = mContext;
        this.mTeets = mTeets;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TweetItemBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.tweet_item, parent, false);
        return new TweetAdapter.TweetViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TweetViewModel tweetViewModel=new TweetViewModel(mContext,mTeets.get(position));
        ((TweetAdapter.TweetViewHolder) holder).binding.setTweet(tweetViewModel);
    }

    @Override
    public int getItemCount() {
        return mTeets.size();
    }

    public class TweetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TweetItemBinding binding;

        public TweetViewHolder(TweetItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int pos =getAdapterPosition();
            Toast.makeText(mContext, " you clicked item "+pos, Toast.LENGTH_LONG).show();
/*            Intent intent=new Intent(mContext, FollowerProfile.class);
            intent.putExtra("screen_name",mFollowers.get(pos).getScreenName());
            intent.putExtra("follower_obj",mFollowers.get(pos));
            mContext.startActivity(intent);*/
        }
    }
}
