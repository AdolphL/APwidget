package com.adolph.test.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.adolph.widget.R;
import com.adolph.widget.SwipeBackWrapper;

/**
 * Created by Adolph on 2018/4/2.
 */

public class BaseActivity extends AppCompatActivity {

    protected boolean isSwipeBackSupport = true;
    private SwipeBackWrapper backWrapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_in_left_full, R.anim.slide_none);

        if(isSwipeBackSupport) {
            BaseApplication application = (BaseApplication) getApplication();
            if (application.getLastActivity() != null) {
                backWrapper = new SwipeBackWrapper(application.getLastActivity(), this);
            }
        }
    }

    @Override
    public void finish() {
        if(backWrapper != null && !backWrapper.isClosed()) {
            backWrapper.closeActivity();
        } else {
            super.finish();
        }
    }

    public boolean isSwipeBackCanConsumer(MotionEvent event) {
        return backWrapper != null && backWrapper.isConsumerEvent(event);
    }

}
