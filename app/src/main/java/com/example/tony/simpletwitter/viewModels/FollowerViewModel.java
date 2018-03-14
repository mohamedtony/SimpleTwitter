package com.example.tony.simpletwitter.viewModels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.widget.ImageView;

import com.example.tony.simpletwitter.BR;
import com.example.tony.simpletwitter.models.Follower;
import com.example.tony.simpletwitter.models.FollowerUser;
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

    @BindingAdapter("app:imageRes")
    public static void bindImage(ImageView view, String img) {
        if(img!=null) {
            Picasso.with(view.getContext()).load(img).resize(100,100).into(view);
        }
    }

}
