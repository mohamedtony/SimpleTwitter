package com.example.tony.simpletwitter.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.tony.simpletwitter.utilities.ChangeLanguage;
import com.example.tony.simpletwitter.R;

public class SplashScreen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private static String SHARED_PREFS = "MyPref";
    private static String IS_LOGED_IN = "isLoged";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ChangeLanguage changeLanguage=new ChangeLanguage(this);
        changeLanguage.loadLocale();

         final SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if(pref.getBoolean(IS_LOGED_IN,false)){
                    Intent intent=new Intent(SplashScreen.this, FollowersActivity.class);
                    // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
