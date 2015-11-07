package com.example.zhaoxuanli.softwidgetdemo.soft.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Button;

import com.example.zhaoxuanli.softwidgetdemo.R;
import com.example.zhaoxuanli.softwidgetdemo.soft.widget.PaintBox.DrawingCanvas;
import com.example.zhaoxuanli.softwidgetdemo.soft.widget.PaintBox.PaintBox;

/**
 * Created by zhaoxuan.li on 2015/8/12.
 * 圆角矩形Button，无边框。  可以放一个颜色当做背景，
 */

//
//字符编码 UTF-8  无BOM
public class SRoundRect_Button extends Button {
    private String text;
    private int textColor;
    private int textSize;
    private int backgroundColor;
    /**
     * 提前绘制的画布
     * 之前为了追求性能，所以用了软引用，但是在红米2上发现Bug
     * DrawingCanvas被系统收回了，导致空指针异常。这里还要好好研究一下
     * 反编译Path源码后，这里是采用软引用的
     */
    private DrawingCanvas localDrawingCanvas;

    /**
     * 一下是超类的三个构造方法
     * @param context
     */
    public SRoundRect_Button(Context context) {
        super(context);
        init(context,null , 0);
    }

    public SRoundRect_Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs , 0);
    }

    public SRoundRect_Button(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs , defStyle);
    }

    private void init(Context context , AttributeSet attrs , int defStyle ){   //初始化操作
        this.setBackgroundResource(0);  //去掉Button原来的背景
        if(null == attrs)
            return ;

        /**
         * View属性的提取
         */
        Resources resources = getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SRoundRect_Button);
        text = a.getString(R.styleable.SRoundRect_Button_android_text);
        textColor = a.getColor(R.styleable.SRoundRect_Button_android_textColor, resources.getColor(R.color.white));
        textSize = a.getDimensionPixelSize(R.styleable.SRoundRect_Button_android_textSize, 20);
        backgroundColor = a.getColor(R.styleable.SRoundRect_Button_backgroundColor, 0Xaf0308);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        //取得View的长宽信息，因为每一次View的载入长款
        float width = this.getWidth();
        float height = this.getHeight();
        /**
         * 因为界面有一定的刷新率，每一次刷新都会调用onDraw方法
         * 所以为了效率和性能考虑，需要做一些判断避免重复判断
         */
        if(localDrawingCanvas==null){
            localDrawingCanvas = DrawingCanvas.instance(width,height);
            PaintBox.drawRoundRect(localDrawingCanvas, backgroundColor, 30);
            PaintBox.drawTextCenter(localDrawingCanvas,text,textColor,textSize);
        }


        canvas.drawBitmap(localDrawingCanvas.getOutput(),0,0,null);
    }
}

