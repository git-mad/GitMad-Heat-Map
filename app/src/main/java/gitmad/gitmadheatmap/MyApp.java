package gitmad.gitmadheatmap;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class MyApp extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

}
