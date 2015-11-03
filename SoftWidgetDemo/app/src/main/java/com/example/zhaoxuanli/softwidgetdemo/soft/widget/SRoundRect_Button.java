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
 * 圆角矩形Button，有边框。  可以放一个颜色当做背景，
 */

//
//字符编码 UTF-8  无BOM
public class SRoundRect_Button extends Button {
    private String text;
    private int textColor;
    private int textSize;
    private int backgroundColor;
    private DrawingCanvas localDrawingCanvas;

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
        this.setBackgroundResource(0);
        if(null == attrs)
            return ;
        Resources resources = getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SRoundRect_Button);
        text = a.getString(R.styleable.SRoundRect_Button_android_text);
        textColor = a.getColor(R.styleable.SRoundRect_Button_android_textColor, resources.getColor(R.color.white));
        textSize = a.getDimensionPixelSize(R.styleable.SRoundRect_Button_android_textSize, 20);
        backgroundColor = a.getColor(R.styleable.SRoundRect_Button_backgroundColor, 0Xaf0308);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        float width = this.getWidth();
        float height = this.getHeight();

        if(localDrawingCanvas==null){
            localDrawingCanvas = DrawingCanvas.instance(width,height);
            PaintBox.drawRoundRect(localDrawingCanvas, backgroundColor, 30);
            PaintBox.drawTextCenter(localDrawingCanvas,text,textColor,textSize);
        }


        canvas.drawBitmap(localDrawingCanvas.getOutput(),0,0,null);
    }
}

