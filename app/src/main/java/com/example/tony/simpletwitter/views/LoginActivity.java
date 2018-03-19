package com.example.tony.simpletwitter.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.tony.simpletwitter.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class LoginActivity extends AppCompatActivity {

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


        //================================Login Button Ref:==> https://github.com/twitter/twitter-kit-android/wiki/Log-In-with-Twitter ================
        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken twitterAuthToken = twitterSession.getAuthToken();

                Toast.makeText(LoginActivity.this, getString(R.string.login_success) + result.data.getUserName(), Toast.LENGTH_LONG).show();

                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(IS_LOGED_IN, true);
                editor.putLong(USER_ID, result.data.getUserId());
                editor.putString(AuthToken, String.valueOf(result.data.getAuthToken()));
                editor.commit();

                //============================ To Start The Second Activity =========================
                Intent intent = new Intent(LoginActivity.this, FollowersActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Log.d(this.getClass().getName(), exception.getMessage());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

}
