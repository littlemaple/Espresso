package com.rainbow.blue.espresso;

import android.app.Application;

import com.rainbow.blue.espresso.base.CoreDataBaseHelper;

/**
 * Created by blue on 2015/9/20.
 */
public class ApplicationEspresson extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CoreDataBaseHelper.init(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


}
