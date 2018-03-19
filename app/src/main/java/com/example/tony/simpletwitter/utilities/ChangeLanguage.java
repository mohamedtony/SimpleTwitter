package com.example.tony.simpletwitter.utilities;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import java.util.Locale;

/**
 * Created by Tony on 3/18/2018.
 */

public class ChangeLanguage extends Application {

    //================================================== Ref:  https://github.com/mohamedtony/7awaiat13/blob/MohamedTony/app/src/main/java/com/example/dev/hawaiat/views/SherdLanguageClass.java =================

    public static final String LOCALE_PREFRENCE = "locale_prefrence";
    public static final String LOCALE_KEYVALUE = "Saved_locale";
    public static SharedPreferences mSharedPreferences;
    public static SharedPreferences.Editor editor;
    public static Locale mLocale;
    public Context context;


    public ChangeLanguage(Context ctx) {
        this.context = ctx;
        try {

            mSharedPreferences = context.getSharedPreferences(LOCALE_PREFRENCE, Context.MODE_PRIVATE);
            editor = mSharedPreferences.edit();
        }catch (Exception e){
            Log.d("Error In SherdLanguage",e.getMessage());
        }
    }

    public void changeLang(final String lang) {

                try {
                    if (lang.equalsIgnoreCase(""))
                        return;
                    mLocale = new Locale(lang);
                    saveLocale(lang);
                    Locale.setDefault(mLocale);
                    Configuration config = new Configuration();
                    config.locale = mLocale;
                    context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
                }catch (Exception e) {
                    Log.d("Error In SherdLanguage", e.getMessage());
                }
    }

    private void saveLocale(String lang) {
        try {
            editor.putString(LOCALE_KEYVALUE, lang);
            editor.commit();
        }catch (Exception e){
            Log.d("Error In SherdLanguage",e.getMessage());
        }

    }

    public void loadLocale() {
        try {
            String language = mSharedPreferences.getString(LOCALE_KEYVALUE, "");
            changeLang(language);
        }catch (Exception e){
            Log.d("Error In SherdLanguage",e.getMessage());
        }
    }

}
