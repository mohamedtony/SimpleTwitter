package com.example.tony.simpletwitter.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.tony.simpletwitter.FollowersActivity;
import com.example.tony.simpletwitter.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class MainActivity extends AppCompatActivity {

    private static String SHARED_PREFS = "MyPref";
    private static String IS_LOGED_IN = "isLoged";
    private static String USER_ID = "user_id";
    private static String AuthToken = "auth_token";
    private TwitterLoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);


        if(pref.getBoolean(IS_LOGED_IN,false)){
            Intent intent=new Intent(MainActivity.this, FollowersActivity.class);
           // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else {

            loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
            loginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    // Do something with result, which provides a TwitterSession for making API calls
                    TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
                    TwitterAuthToken twitterAuthToken = twitterSession.getAuthToken();

                    Toast.makeText(MainActivity.this, getString(R.string.login_success) + result.data.getUserName(), Toast.LENGTH_LONG).show();

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(IS_LOGED_IN, true);
                    editor.putLong(USER_ID, result.data.getUserId());
                    editor.putString(AuthToken, String.valueOf(result.data.getAuthToken()));
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this, FollowersActivity.class);
                    startActivity(intent);
                }

                @Override
                public void failure(TwitterException exception) {
                    // Do something on failure
                    Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

}
