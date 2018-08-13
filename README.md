# APwidget
Some Android widget
这个库主要是自己编写的一些Android组件，会不定时更新与修改BUG，感兴趣的朋友可以Down来看看，其中每一个组件都会有对应Demo与说明。
# 目录
* 验证码文本框
# 组件使用说明
##  验证码文本框
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
