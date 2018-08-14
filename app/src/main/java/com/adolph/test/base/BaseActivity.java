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

    protected boolean isSwipeBackSupport = true;  //某些Activity不想拥有滑动退出的效果，只要在父类onCreate之前将isSwipeBackSupport更改为false即可
    private SwipeBackWrapper backWrapper; //wrapper实例

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_in_left_full, R.anim.slide_none); //用于屏蔽Activity自带的动画效果

        if(isSwipeBackSupport) {
            BaseApplication application = (BaseApplication) getApplication();
            if (application.getLastActivity() != null) {
                backWrapper = new SwipeBackWrapper(application.getLastActivity(), this); //此组件实例化的参数是两个Activity，上一个Activity可以通过Application中存储的Activity栈获得（这里需要自己实现，可以参考Demo）。
            }
        }
    }

    @Override
    public void finish() {
        if(backWrapper != null && !backWrapper.isClosed()) {  //当用户点击back按钮时，依旧使用我们的退出样式
            backWrapper.closeActivity();
        } else {
            super.finish();
        }
    }

    public boolean isSwipeBackCanConsumer(MotionEvent event) { //用于嵌套滑动
        return backWrapper != null && backWrapper.isConsumerEvent(event);
    }

}
