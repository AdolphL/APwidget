package com.adolph.test.swipeback;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.adolph.test.base.BaseActivity;
import com.adolph.widget.R;

import java.util.Random;

/**
 * Created by Adolph on 2018/8/13.
 */

public class SwipeBackActivity extends BaseActivity {

    private int[] colors = {Color.RED, Color.BLACK, Color.BLUE, Color.CYAN, Color.YELLOW, Color.WHITE, Color.GREEN};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_back);

        Random r = new Random();
        ViewGroup group = findViewById(R.id.swipe_group);
        group.setBackground(new ColorDrawable(colors[Math.abs(r.nextInt()) % 7]));
    }

    public void addActivity(View view) {
        startActivity(new Intent(this, SwipeBackActivity.class));
    }

    public void addScrollActivity(View view) {
        startActivity(new Intent(this, SwipeBackScrollActivity.class));
    }

}
