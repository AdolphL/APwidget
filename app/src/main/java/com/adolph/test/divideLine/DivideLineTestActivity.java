package com.adolph.test.divideLine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.adolph.widget.DivideLineTextView;
import com.adolph.widget.R;

public class DivideLineTestActivity extends AppCompatActivity {

    DivideLineTextView textView1;
    DivideLineTextView textView2;
    DivideLineTextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divide_line);

        textView1 = findViewById(R.id.divideLine1);
        textView2 = findViewById(R.id.divideLine2);
        textView3 = findViewById(R.id.divideLine3);

        textView1.setCompleteListener(this::showInputText);
        textView2.setCompleteListener(this::showInputText);
        textView3.setCompleteListener(this::showInputText);
    }

    private void showInputText(String[] values) {
        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            sb.append(value);
        }
        Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
    }

    public void clearText(View view) {
        textView1.clearInputText();
        textView2.clearInputText();
        textView3.clearInputText();
    }

    public void showText(View view) {
        Toast.makeText(this, textView1.getValueString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, textView2.getValueString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, textView3.getValueString(), Toast.LENGTH_SHORT).show();
    }

}
