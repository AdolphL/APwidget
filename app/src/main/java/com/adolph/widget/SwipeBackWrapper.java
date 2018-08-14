package com.adolph.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.adolph.widget.R;


/**
 * Created by Adolph on 2018/7/24.
 */

public class SwipeBackWrapper {

    private Activity pre;
    private Activity cur;
    private ViewGroup curContentView;
    private ViewGroup preContextView;
    private boolean isDown = false;
    private int screenWidth;
    private int maxDownBoundary;
    private int closeBoundary;
    private float lastX = 0;
    private int minMove = 5;

    private int animator_duration = 300;

    private boolean isClosed = false;

    private float downBoundaryRatio = 0.05f;
    private float closeBoundaryRatio = 0.4f;
    private float preSlideRatio = 0.3f;

    public SwipeBackWrapper(@NonNull Activity pre, @NonNull Activity cur) {
        this.pre = pre;
        this.cur = cur;
        initialize();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initialize() {
        ViewGroup decorView = (ViewGroup) cur.getWindow().getDecorView();
        decorView.setBackgroundColor(Color.TRANSPARENT);

        curContentView = cur.findViewById(android.R.id.content);

        cur.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        screenWidth = cur.getResources().getDisplayMetrics().widthPixels;
        maxDownBoundary = (int) (screenWidth * downBoundaryRatio);
        closeBoundary = (int) (screenWidth * closeBoundaryRatio);

        preContextView = pre.findViewById(android.R.id.content);
        startSlideAnimator(preContextView, animator_duration, 0, -screenWidth * preSlideRatio, null);

        curContentView.setOnTouchListener((View v, MotionEvent event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(event.getRawX() < maxDownBoundary) {
                            isDown = true;
                            lastX = event.getRawX();
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(isDown) {
                            if(Math.abs(event.getRawX() - lastX) > minMove){
                                v.setTranslationX(event.getRawX() - minMove);
                                preContextView.setTranslationX((-screenWidth + event.getRawX()) * preSlideRatio);
                            }
                            lastX = event.getRawX();
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if(isDown){
                            if(event.getRawX() > closeBoundary){
                                closeActivity();
                            } else {
                                startSlideAnimator(v, animator_duration, lastX, 0, null);
                                startSlideAnimator(preContextView, animator_duration, (- screenWidth + lastX) * preSlideRatio, -screenWidth * preSlideRatio, null);
                            }
                            isDown = false;
                            return true;
                        }
                        break;
                }

                return false;
            }
        );
    }

    public void closeActivity() {
        if(!isClosed) {
            startSlideAnimator(curContentView, animator_duration, lastX, screenWidth, () -> {
                cur.finish();
                cur.overridePendingTransition(R.anim.slide_none, R.anim.slide_none);
            });
            startSlideAnimator(preContextView, animator_duration, (- screenWidth + lastX) * preSlideRatio, 0, null);
            isClosed = true;
        }
    }

    public boolean isConsumerEvent(MotionEvent event) {
        return event.getRawX() < maxDownBoundary || isDown;
    }

    public boolean isClosed() {
        return isClosed;
    }

    private void startSlideAnimator(View v, int duration, float startX, float endX, Callback callback) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setFloatValues(startX, endX);

        valueAnimator.addUpdateListener(anim -> {
            float x = (float) anim.getAnimatedValue();
            v.setTranslationX(x);
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(callback != null) {
                    callback.onFinish();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        valueAnimator.start();
    }

    interface Callback {
        void onFinish();
    }

}
