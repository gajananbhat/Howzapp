package com.simulterra.myapplication;

import com.firebase.client.Firebase;
/**
 * Created by Srini on 8/20/15.
 */
public class MyApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }
}
