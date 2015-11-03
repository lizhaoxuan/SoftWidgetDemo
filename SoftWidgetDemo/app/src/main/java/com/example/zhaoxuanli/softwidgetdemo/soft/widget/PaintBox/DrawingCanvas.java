package com.example.zhaoxuanli.softwidgetdemo.soft.widget.PaintBox;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by zhaoxuan.li on 2015/8/11.
 * 在这上面进行提前绘制
 */
public class DrawingCanvas extends Canvas {

    private Bitmap output;
    private Rect rect;
    private RectF rectF;
    private DrawingCanvas(Bitmap output_, float width_, float height_){
        super(output_);
        output = output_;
        rect = new Rect(0,0,(int)width_,(int)height_);
        rectF = new RectF(0, 0, width_, height_);
    }

    public static DrawingCanvas instance(float width_ , float height_){
        Bitmap bitmap = Bitmap.createBitmap((int) width_, (int) height_,
                Bitmap.Config.ARGB_8888);
        return new DrawingCanvas(bitmap,width_,height_);
    }


    public Bitmap getOutput(){
        return output;
    }
    public Rect getRect(){
        return rect;
    }
    public RectF getRectF(){
        return rectF;
    }
}
