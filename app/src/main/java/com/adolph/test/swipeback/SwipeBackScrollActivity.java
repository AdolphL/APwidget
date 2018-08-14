package com.adolph.test.swipeback;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.adolph.test.base.BaseActivity;
import com.adolph.widget.R;

import java.util.Random;

/**
 * Created by Adolph on 2018/8/14.
 */

public class SwipeBackScrollActivity extends BaseActivity {

    private int[] colors = {Color.RED, Color.BLACK, Color.BLUE, Color.CYAN, Color.YELLOW, Color.WHITE, Color.GREEN};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_back_scroll);

        Random r = new Random();
        ViewGroup group = findViewById(R.id.swipe_group);
        group.setBackground(new ColorDrawable(colors[Math.abs(r.nextInt()) % 7]));
    }

}
