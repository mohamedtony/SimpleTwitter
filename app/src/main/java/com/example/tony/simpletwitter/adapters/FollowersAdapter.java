package com.example.tony.simpletwitter.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tony.simpletwitter.R;
import com.example.tony.simpletwitter.databinding.FollowerItemBinding;
import com.example.tony.simpletwitter.models.Follower;
import com.example.tony.simpletwitter.viewModels.FollowerViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tony on 3/14/2018.
 */

public class FollowersAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<Follower> mFollowers;

    public FollowersAdapter(Context mContext, ArrayList<Follower> mFollowers) {
        this.mContext = mContext;
        this.mFollowers = mFollowers;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FollowerItemBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.follower_item, parent, false);
        return new FollowerViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FollowerViewModel followerViewModel=new FollowerViewModel(mContext,mFollowers.get(position));
        ((FollowerViewHolder) holder).binding.setFollower(followerViewModel);
    }

    @Override
    public int getItemCount() {
        return mFollowers.size();
    }

    public class FollowerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        FollowerItemBinding binding;

        public FollowerViewHolder(FollowerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int pos =getAdapterPosition();
            Toast.makeText(mContext, " you clicked item "+pos, Toast.LENGTH_LONG).show();
        }
    }
}