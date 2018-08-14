package com.adolph.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.adolph.test.base.BaseActivity;

/**
 * Created by Adolph on 2018/7/24.
 */

public class SwipeBackLinear extends LinearLayout {

    public SwipeBackLinear(Context context) {
        super(context);
    }

    public SwipeBackLinear(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeBackLinear(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (getContext() instanceof BaseActivity) {
            if(((BaseActivity) getContext()).isSwipeBackCanConsumer(ev)){
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getContext() instanceof BaseActivity) {
            if(((BaseActivity) getContext()).isSwipeBackCanConsumer(event)){
                return false;
            }
        }
        return super.onTouchEvent(event);
    }
}
