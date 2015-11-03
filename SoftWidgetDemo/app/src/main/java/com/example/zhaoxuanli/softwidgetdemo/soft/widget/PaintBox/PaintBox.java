package com.example.zhaoxuanli.softwidgetdemo.soft.widget.PaintBox;

import android.graphics.Paint;


/**
 * Created by zhaoxuan.li on 2015/8/12.
 * 用来画图的盒子，里面放着画图的方法
 */
public class PaintBox {
    /**
     * 画圆角矩形
     * @param canvas 画布
     * @param color 填充颜色
     * @param alpha 透明度
     */
    public static void drawRoundRect(DrawingCanvas canvas ,int color , int alpha){

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        //paint.setAntiAlias(true);
        paint.setColor(color);
        //paint.setAlpha(alpha);
        canvas.drawRoundRect(canvas.getRectF(), 20, 20, paint);

        
    }

    /**
     * 画圆角矩形
     * @param canvas 画布
     * @param color 填充颜色
     * @param alpha 透明度
     * @param border 带边框
     */
    public static void drawRoundRect(DrawingCanvas canvas ,int color , int alpha,boolean border){
        if(border){
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            paint.setColor(color);
            //paint.setAlpha(40);
            canvas.drawRoundRect(canvas.getRectF(), 20, 20, paint);

            paint.setStyle(Paint.Style.STROKE);
            //paint.setAlpha(70);
            paint.setStrokeWidth(4);
            canvas.drawRoundRect(canvas.getRectF(),20,20,paint);
        }else{
            drawRoundRect(canvas,color,alpha);
        }


    }

    public static void drawColor(DrawingCanvas canvas ,int color ){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //paint.setFilterBitmap(true);
        //paint.setDither(true);
        paint.setColor(color);
        canvas.drawPaint(paint);
    }


    /**
     * 写字在View正中间
     * @param canvas  画布
     * @param text   文字
     * @param color  文字颜色
     * @param size   文字大小
     */
    public static void drawTextCenter(DrawingCanvas canvas , String text , int color,int size){
        //创建画笔
        Paint pp = new Paint();
        pp.setAntiAlias(true);
        pp.setColor(color);
        pp.setStrokeWidth(3);
        pp.setTextSize(size);
        pp.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = pp.getFontMetricsInt();
        float vertical = canvas.getRectF().top + (canvas.getRectF().bottom - canvas.getRectF().top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(text, canvas.getRectF().centerX(), vertical, pp);
    }
}
