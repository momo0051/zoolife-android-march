package com.zoolife.app;

import android.app.Application;
import android.content.SharedPreferences;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();
    public static MyApplication instance;
    public SharedPreferences userLocaleHolder;

    public MyApplication() {
        instance = this;
    }

    public static MyApplication getAppInstance() {
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
//        FirebaseApp.initializeApp(this);
        Fresco.initialize(this);
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
    }


    public void saveHiddenModeSetting(String which) {
        this.userLocaleHolder = getSharedPreferences("locale", 0);
        SharedPreferences.Editor editor = this.userLocaleHolder.edit();
        editor.putString("lang", which);
        editor.commit();
    }

    public String getHiddenModeSettings() {
        this.userLocaleHolder = getSharedPreferences("locale", 0);
        if (this.userLocaleHolder != null) {
            return this.userLocaleHolder.getString("lang", "ar");
        }
        return "ar";
    }
}