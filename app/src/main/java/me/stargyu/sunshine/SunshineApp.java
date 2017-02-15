package me.stargyu.sunshine;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class SunshineApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this); // Stetho 적용
    }
}
