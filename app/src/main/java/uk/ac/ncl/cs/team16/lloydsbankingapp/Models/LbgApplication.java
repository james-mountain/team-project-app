package uk.ac.ncl.cs.team16.lloydsbankingapp.Models;

import android.app.Application;
import android.content.Context;

/**
 * Created by Aleksander on 22/02/2015.
 */
public class LbgApplication extends Application {
    private static LbgApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static LbgApplication getInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}
