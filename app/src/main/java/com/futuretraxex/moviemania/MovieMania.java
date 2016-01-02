package com.futuretraxex.moviemania;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by dragonSlayer on 12/10/2015.
 */
public class MovieMania extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
