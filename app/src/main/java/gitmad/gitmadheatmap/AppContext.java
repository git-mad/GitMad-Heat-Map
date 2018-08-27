package gitmad.gitmadheatmap;

import android.app.Application;
import android.content.Context;

/**
 * Class that is used throughout our code to get the application context.
 * This class allows us to use contexts in any location, but is mainly used in
 * non-activity instances, non-fragment instances, or instances that do not have an assoc. context.
 */
public class AppContext extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

}
