package com.adolph.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;


import static android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT;

/**
 * Created by Adolph on 2018/8/2.
 */

public class DivideLineTextView extends View {

    private float width;
    private float height;
    private float divideWidth;
    private int inputLength;
    private int lineWidth;
    private int cursorColor;
    private int lineColor;
    private int currentIndex;
    private String[] values;
    private float textSize;
    private int textColor;
    private int inputType;
    private boolean isPassword;
    private int passStyle;

    private boolean isShowCursor = false;
    private boolean isLive = false;

    private Paint p;

    private InputCompleteListener completeListener;

    public final static int STYLE_POINT = 1;
    public final static int STYLE_START = 2;

    public DivideLineTextView(Context context) {
        this(context, null);
    }

    public DivideLineTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DivideLineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStrokeWidth(2 * getResources().getDisplayMetrics().density);

        initConfiguration(context.obtainStyledAttributes(attrs, R.styleable.DivideLineTextView));
    }

    private void initConfiguration(TypedArray typedArray) {
        lineColor = typedArray.getColor(R.styleable.DivideLineTextView_lineColor, Color.BLUE);
        cursorColor = typedArray.getColor(R.styleable.DivideLineTextView_cursorColor, Color.GREEN);
        textColor = typedArray.getColor(R.styleable.DivideLineTextView_lineTextColor, Color.BLACK);

        inputLength = typedArray.getInt(R.styleable.DivideLineTextView_inputLength, 6);
        divideWidth = typedArray.getDimension(R.styleable.DivideLineTextView_divideWidth, (10 * getResources().getDisplayMetrics().density));
        textSize = typedArray.getDimension(R.styleable.DivideLineTextView_lineTextSize, 0);

        isPassword = typedArray.getBoolean(R.styleable.DivideLineTextView_isPassword, false);
        passStyle = typedArray.getInt(R.styleable.DivideLineTextView_passwordStyle, STYLE_POINT);
        inputType = typedArray.getInt(R.styleable.DivideLineTextView_inputType, InputType.TYPE_NULL);
        currentIndex = 0;

        values = new String[inputLength];
    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawLine(canvas);

        drawCursor(canvas);

        drawLabel(canvas);

    }

    private void drawLabel(Canvas canvas) {
        for (int i = 0; i < currentIndex; i++) {
            p.setTextSize(textSize);
            p.setColor(textColor);
            String label = getLabel(i);
            if (label == null) {
                float w = textSize * 0.6f;
                float sy = height - w > 0 ? (height - w) / 2 : 0;

                RectF rectF = new RectF(i * (lineWidth + divideWidth) + (lineWidth + divideWidth) / 2 - w / 2, sy, i * (lineWidth + divideWidth) + (lineWidth + divideWidth) / 2 + w / 2, w + sy);
                float rr = textSize / 2;
                canvas.drawRoundRect(rectF, rr, rr, p);
            } else {
                Rect rect = new Rect();
                p.getTextBounds(label, 0, label.length(), rect);
                float offsetY = height - rect.height() > 0 ? (height - rect.height()) / 2 : 0;
                canvas.drawText(label, i * (lineWidth + divideWidth) + (lineWidth + divideWidth) / 2 - rect.width() / 2, height - offsetY, p);
            }
        }

    }

    private String getLabel(int i) {
        if (!isPassword) {
            return values[i];
        } else if (passStyle == STYLE_START) {
            return "*";
        } else {
            return null;
        }
    }

    private void drawCursor(Canvas canvas) {
        if (isShowCursor) {
            p.setColor(cursorColor);

            float x = currentIndex * (lineWidth + divideWidth) + (lineWidth + divideWidth) / 2;
            float ey = height * 0.9f;

            canvas.drawLine(x, height * 0.1f, x, ey, p);
        }
    }

    private void drawLine(Canvas canvas) {
        int offset = (int) (divideWidth / 2);
        for (int i = 0; i < inputLength; i++) {
            if (i == currentIndex && hasFocus()) {
                p.setColor(cursorColor);
            } else {
                p.setColor(lineColor);
            }
            canvas.drawLine(i * lineWidth + offset, height, (i + 1) * lineWidth + offset, height, p);
            offset += divideWidth;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setFocusableInTouchMode(true); //important
                setFocusable(true);
                requestFocus();
                return true;
            case MotionEvent.ACTION_UP:
                try {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(this, InputMethodManager.RESULT_SHOWN);
                    imm.restartInput(this);
                } catch (Exception ignore) {
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = inputType;
        outAttrs.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI;

        return new BaseInputConnection(this, true) {
            @Override
            public boolean commitText(CharSequence text, int newCursorPosition) {
                if (currentIndex == inputLength) {
                    isShowCursor = false;
                    return false;
                }
                inputText(text.toString());

                invalidate();
                return true;
            }

            @Override
            public boolean deleteSurroundingText(int beforeLength, int afterLength) {
                for (int i = afterLength; i < beforeLength; i++) {
                    deleteLastText();
                }
                invalidate();
                return true;
            }

            @Override
            public boolean sendKeyEvent(KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    switch (event.getKeyCode()) {
                        case KeyEvent.KEYCODE_DEL:
                            if (currentIndex > 0) {
                                deleteSurroundingText(currentIndex, currentIndex - 1);
                            }
                            break;
                        case KeyEvent.KEYCODE_0:
                            commitText("0", currentIndex);
                            break;
                        case KeyEvent.KEYCODE_1:
                            commitText("1", currentIndex);
                            break;
                        case KeyEvent.KEYCODE_2:
                            commitText("2", currentIndex);
                            break;
                        case KeyEvent.KEYCODE_3:
                            commitText("3", currentIndex);
                            break;
                        case KeyEvent.KEYCODE_4:
                            commitText("4", currentIndex);
                            break;
                        case KeyEvent.KEYCODE_5:
                            commitText("5", currentIndex);
                            break;
                        case KeyEvent.KEYCODE_6:
                            commitText("6", currentIndex);
                            break;
                        case KeyEvent.KEYCODE_7:
                            commitText("7", currentIndex);
                            break;
                        case KeyEvent.KEYCODE_8:
                            commitText("8", currentIndex);
                            break;
                        case KeyEvent.KEYCODE_9:
                            commitText("9", currentIndex);
                            break;
                    }
                }
                return true;
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (40 * getResources().getDisplayMetrics().density), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        height = getMeasuredHeight() * 0.9f;

        lineWidth = (int) (width / inputLength - divideWidth);
        if (textSize == 0f) {
            textSize = height * 0.75f;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isLive = true;
        new Thread(() -> {
            while (isLive) {
                if (hasFocus()) {
                    isShowCursor = !isShowCursor;
                    postInvalidate();
                } else {
                    isShowCursor = false;
                }
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isLive = false;
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (!hasFocus()) {
            isShowCursor = false;
            postInvalidate();
        }
    }

    public void clearInputText() {
        currentIndex = 0;
        values = new String[inputLength];
    }

    public void inputText(String str) {
        values[currentIndex] = str;
        currentIndex++;
        if (currentIndex == inputLength) {
            try {
                ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getWindowToken(), SHOW_IMPLICIT);
            } catch (Exception ignore) {
            }

            if (completeListener != null) {
                completeListener.onComplete(values);
            }
        }
    }

    public void deleteLastText() {
        if (currentIndex > 0) {
            currentIndex--;
        }
        ;
        values[currentIndex] = null;
    }

    public String[] getValues() {
        return values;
    }

    public String getValueString() {
        StringBuilder sb = new StringBuilder();
        for (String str : values) {
            if (str != null)
                sb.append(str);
        }
        return sb.toString();
    }

    public void setCompleteListener(InputCompleteListener listener) {
        this.completeListener = listener;
    }

    public interface InputCompleteListener {
        void onComplete(String[] values);
    }

}
