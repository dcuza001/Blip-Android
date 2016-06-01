package com.Blipster.horay.Blip;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by HoRay on 4/13/2016.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
