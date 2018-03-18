package com.example.tony.simpletwitter.viewModels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.example.tony.simpletwitter.BR;
import com.example.tony.simpletwitter.models.Follower;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by Tony on 3/14/2018.
 */

public class FollowerViewModel extends BaseObservable{
    private Context mContext;
    private Follower mFollower;

    public FollowerViewModel(Context mContext, Follower mUser) {
        this.mContext = mContext;
        this.mFollower = mUser;
    }
    @Bindable
    public String getName(){
       return mFollower.getName();
    }
    public void setName(String name){
        mFollower.setName(name);
        notifyPropertyChanged(BR.name);
    }
    @Bindable
    public String getPhoto(){;
        return mFollower.getProfilePictureUrl();
    }
    public void setPhoto(String photo){
        mFollower.setProfilePictureUrl(photo);
        notifyPropertyChanged(BR.photo);
    }
    @Bindable
    public String getDesc(){
        return mFollower.getDescription();
    }
    public void setDesc(String desc){
        mFollower.setDescription(desc);
        notifyPropertyChanged(BR.desc);
    }

    //========================================================== to set image res=========================
    @BindingAdapter("app:imageRes")
    public static void bindImage(final ImageView view, final String img) {
        if(img!=null) {
           // Picasso.with(view.getContext()).load(img).resize(100,100).into(view);
            Picasso.with(view.getContext()).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(view, new Callback() {
                @Override
                public void onSuccess() {
                    /// success in ofline mode
                }

                @Override
                public void onError() {
                    Picasso.with(view.getContext()).load(img).into(view);
                }
            });
        }
    }
}
