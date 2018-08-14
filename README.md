# APwidget
Some Android widget
这个库主要是自己编写的一些Android组件，会不定时更新与修改BUG，感兴趣的朋友可以Down来看看，其中每一个组件都会有对应Demo与说明。
# 目录
* 验证码文本框(DivideLineTextView)
* 仿IOS Activity侧滑退出组件(SwipeBackWrapper)
# 组件使用说明
##  验证码文本框(DivideLineTextView)
### 效果图
![](https://github.com/AdolphL/picture/blob/master/ap_widget/2.png)
### Demo地址:
[APwidget](https://github.com/AdolphL/APwidget/tree/master/app/src/main/java/com/adolph/test/divideLine)
### 使用方法
``` xml    
  <com.adolph.widget.DivideLineTextView
          android:id="@+id/divideLine2"
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          android:layout_margin="10dp"
          app:inputLength="4"
          app:lineTextColor="@color/red"
          app:cursorColor="@color/pink"
          app:lineColor="@color/green"
          app:lineTextSize="20sp"/>
```      
``` Java
  DivideLineTextView textView = findViewById(R.id.divideLine2);
  textView.setCompleteListener(vals -> {xxx}) //可以添加输入完成监听 
```
### Api
|方法名|参数|返回值|描述|
|:---|:---|:---|:---|
|clearInputText|void|void|清空当前DivideLineTextView的输入数据|
|inputText|String|void|将String最加到DivideLineTextView的数据尾部|
|deleteLastText|void|void|删除DivideLineTextView上一个数据|
|getValues|void|String[]|获取DivideLineTextView中的数据(以字符串数组形式返回)|
|getValueString|void|String|获取DivideLineTextView中的数据(以一个字符串的形式返回)|
|setCompleteListener|InputCompleteListener|void|设置当输入完成时的监听事件|
### DivideLineTextView 属性
``` xml
    <declare-styleable name="DivideLineTextView">
        <attr name="lineColor" format="color" />
        <attr name="cursorColor" format="color" />
        <attr name="lineTextColor" format="color" />
        <attr name="divideWidth" format="dimension" />
        <attr name="inputLength" format="integer" />
        <attr name="inputType" format="enum">  <!-- keyboard classic -->
            <enum name="INPUT_NULL" value="0x00000000" />
            <enum name="INPUT_NUMBER" value="0x00000002" />
        </attr>
        <attr name="isPassword" format="boolean" />
        <attr name="passwordStyle" format="enum">
            <enum name="STYLE_POINT" value="1" /> <!-- black point style -->
            <enum name="STYLE_START" value="2" /> <!-- **** style -->
        </attr>
        <attr name="lineTextSize" format="dimension" />
    </declare-styleable>
```

## 仿IOS Activity侧滑退出组件(SwipeBackWrapper)
### 效果图
![](https://github.com/AdolphL/picture/blob/master/ap_widget/swipe_back.gif)
### Demo地址
[APwidget](https://github.com/AdolphL/APwidget/tree/master/app/src/main/java/com/adolph/test/swipeback)
### 使用方法
这个组件编写的侵入性比较强，大家可以根据自己的选择决定要不要使用，并且在Demo里我也写了一套解决方案可供参考。
1.必要的设置 <br>
必须设置我们Activity的window背景为透明，否则我们只能看到window view的背景色，并不能看到上一个Activity。
在我们的Application的Style样式中添加：
``` xml
      <!-- Base application theme. -->
    <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
        <item name="android:windowIsTranslucent">true</item>
    </style>
```
并且我们需要屏蔽Activity自带的进入，退出动画使用此代码在你的BaseActivity的onCreate方法中：
``` Java
onCreate(@Nullable Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  overridePendingTransition(R.anim.slide_in_left_full, R.anim.slide_none); //用于屏蔽Activity自带的动画效果
}
```
2.无嵌套滑动的Activity <br>
因为是全局性的改造，所以最好再BaseActivity中加入此组件，当然你也可以在需要的Activity初始化，具体操作参考下列代码（在Demo中有详细）。
``` Java
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
```
3.涉及了嵌套滑动的Activity <br>
因为涉及了嵌套滑动，我这里取巧了，实现的方式比较简单，大家可以随意继承某个布局，并在其中覆盖以下方法（参考Demo SwipeBackLinear）。
``` Java
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
```
并在Xml布局文件中更改RootView，如下:
``` xml
<com.adolph.widget.SwipeBackLinear xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/swipe_group">
    
    xxxx //你的布局
  
</com.adolph.widget.SwipeBackLinear>
```
### Api
|方法名|参数|返回值|描述|
|:---|:---|:---|:---|
|closeActivity|void|void|关闭由此SwipeWrapper管理的当前Activity|
|isConsumerEvent|MotionEvent|boolean|如果触摸事件，SwipeWrapper返回此事件它是否能消费|
|isClosed|void|void|返回由此SwipeWrapper管理的当前Activity是否已经被关闭|
