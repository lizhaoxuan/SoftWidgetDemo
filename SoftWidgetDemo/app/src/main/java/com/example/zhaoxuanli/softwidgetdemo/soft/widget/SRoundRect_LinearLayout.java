package com.example.zhaoxuanli.softwidgetdemo.soft.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.example.zhaoxuanli.softwidgetdemo.R;
import com.example.zhaoxuanli.softwidgetdemo.soft.widget.PaintBox.DrawingCanvas;
import com.example.zhaoxuanli.softwidgetdemo.soft.widget.PaintBox.PaintBox;


/**
 * Created by zhaoxuan.li on 2015/8/12.
 */
public class SRoundRect_LinearLayout extends LinearLayout {
    private int backgroundColor;
    private DrawingCanvas localDrawingCanvas;
    public SRoundRect_LinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs,defStyle);
    }

    public SRoundRect_LinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public SRoundRect_LinearLayout(Context context) {
        super(context);
        init(context,null,0);
    }
    private void init(Context context, AttributeSet attrs, int defStyle){
        if(null == attrs)
            return ;
        setWillNotDraw(false);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SRoundRect_LinearLayout);
        backgroundColor = a.getColor(R.styleable.SRoundRect_LinearLayout_bgcolor,0X3f000000);

        //backgroundColor = getResources().getColor(R.color.white);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float width = this.getWidth();
        float height = this.getHeight();

        if(localDrawingCanvas==null){
            localDrawingCanvas = DrawingCanvas.instance(width,height);
            PaintBox.drawRoundRect(localDrawingCanvas, backgroundColor, 40);
        }
        canvas.drawBitmap(localDrawingCanvas.getOutput(),0,0,null);
    }
}

