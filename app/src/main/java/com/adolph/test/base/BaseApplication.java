package com.adolph.test.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.LinkedList;


/**
 * Created by Adolph on 2018/4/2.
 */

public class BaseApplication extends Application {

    protected LinkedList<Activity> activityLinked;

    @Override
    public void onCreate() {
        super.onCreate();

        activityLinked = new LinkedList<>();

        this.registerActivityLifecycleCallbacks(new ActivityLifecycle()); //register activity listener
    }

    public void killCurrentActivity() {
        activityLinked.getLast().finish();
    }

    public Activity getCurrentActivity() {
        return activityLinked.getLast();
    }

    public Activity getLastActivity() {
        return activityLinked.size() > 1 ? activityLinked.get(activityLinked.size() - 2) : null;
    }

    class ActivityLifecycle implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            activityLinked.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            activityLinked.remove(activity);
        }
    }

}
