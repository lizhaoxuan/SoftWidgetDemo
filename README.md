#Android过度绘制深度优化---View提前绘制

##原理介绍：

上一篇文章对过度绘制和View优化做了大概的简介和介绍。我们知道，引起过度绘制的根本原因是  背景（背景包裹背景图片、颜色、形状、边框等等）。


那么通常你在网络查找过度绘制优化策略，不外乎减少View的层叠、多余的控件、一个多余的背景设置等等。可如果你真的去审查代码，会发现你并没有多少可优化的地方。这个时候，你就需要   **Android过度绘制深度优化---View提前绘制**


先说原理：**不做处理的View绘制过程像一把刷子一层层去绘制View，第一层刷一个形状，第二层刷背景图片/颜色，第三层刷文字等等，那么，如果我们将View提前画好，然后交给系统去绘制。这样不管你的View之前刷过多少次，系统只需要绘制一次。这样，过度绘制就可以轻松解决啦！**


先上效果图：
优化前   &  优化后：
<img src="http://img.blog.csdn.net/20151103211615749" width = "150" height = "250" alt="图片名称" align=center />        <img src="http://img.blog.csdn.net/20151103211244952" width = "150" height = "250" alt="图片名称" align=center />

**Tips：**
1.首先控件的实现方式略有不同（带图标的输入框），但对我们这片文章讲的东西并没有冲突。
2.优化后的图片，为了突出所以添加了带图案的背景图片

**颜色说明：**
优化前：

- Window（无色） 绘制一次
- 背景（蓝色） 绘制2次
- Logo,输入框带小图标的背景，按钮,忘记密码（绿色）绘制三次
- 输入框本身，按钮文字 （红色） 绘制三次
- 输入框内部文字 （深红色）  绘制四次

优化后：

- 背景（无色） 绘制一次   ： 背景设置到主题后，将取代原本Window默认背景。
- Logo，按钮，带形状和颜色的输入框背景，忘记密码文字 （蓝色） 绘制两次： 这里我们对输入框和按钮进行了提前绘制，并去掉了按钮默认背景，所以按钮加其内部文字只绘制一次。带形状、颜色的输入框只绘制一次。
- 文本框上面的图片和文字（绿色）绘制三次

##注意要点

View提前绘制可以从根本上去解决问题过度绘制问题，但并不是没有代价的，但你在做优化前，需要注意一下几个点：

- View的提前绘制只能应用到那些静态的View
- View的提前绘制并不能加过View的绘制速度，甚至会有小幅度的绘制时间增加。（提前在内存中进行View的绘制是有代价的）
- 界面是有一定刷新频率的，每一次刷新都会调用View的onDraw方法，而View提前绘制就是在onDraw中进行。所以你需要考虑效率和性能问题。**如：避免在onDraw创建对象，避免在onDraw进行绘制，应在构造函数中画好，交给onDraw。**
- 对于登录界面，提前绘制的工作量和其收益比起来是得不偿失的，所以你需要进行权衡，哪里需要提前绘制。**通常我是这样做的：大量被重用的控件，非常复杂的布局，布局之上有动画效果（过度绘制对动画的影响是极大的）**

也许在你看到View的提前绘制是有代价的，就决定不在进行优化，这是错误的。除非极其复杂的View，负责提前绘制带来收益是绝对大于那多出来的一丢丢绘制时间的。

另外即使类似于登录界面这样的简单布局没有必要进行过度绘制优化，但依然有大量的优秀公司进行了优化。我认为有两个点：
1.登录界面是应用打开的第一个界面，也是整个应用最简单的界面之一，用它来试手肯定是第一选择。
2.这是一个态度和逼格的问题。打开过度绘制调试后，别人家的应用蓝蓝的，怎么你的应用就是红彤彤一片？

##正文
啊，好吧，废话说的有点多了，下面我们上代码。

Github链接：https://github.com/lizhaoxuan/SoftWidgetDemo.git

首先看目录结构：

<img src="http://img.blog.csdn.net/20151107151634177" width = "180" height = "230" alt="图片名称" align=center />

有四个核心类：
DrawingCanvas.java 我们提前绘制就在这个类上画，然后交给onDraw，一气呵成。
PaintBox.java 里面包含了几个绘制方法，比如画背景颜色和形状

SRoundRect_Button.java  SRoundRect_LinearLayout.java 是两个自定义控件，从名字上可以看出来，两个圆角矩形的按钮和线性布局

然后看布局文件：

	<?xml version="1.0" encoding="utf-8"?>
	<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    android:padding="10dp">
	
	    <ImageView
	        android:id="@+id/logoView"
	        android:layout_width="100dp"
	        android:layout_height="100dp"
	        android:layout_centerHorizontal="true"
	        android:layout_marginTop="80dp"
	        android:src="@drawable/logo"/>
	
	    <com.example.zhaoxuanli.softwidgetdemo.soft.widget.SRoundRect_LinearLayout
	        android:id="@+id/accountLayout"
	        android:layout_width="match_parent"
	        android:layout_height="40dp"
	        android:layout_marginTop="50dp"
	        android:layout_marginRight="20dp"
	        android:layout_marginLeft="20dp"
	        android:layout_below="@id/logoView"
	        android:orientation="horizontal"
	        android:paddingLeft="6dip"
	        android:paddingRight="6dip"
	        android:paddingTop="8dip"
	        app:bgcolor="#CFCFCF">
	
	        <ImageView
	            android:layout_width="wrap_content"
	            android:layout_height="22dip"
	            android:layout_gravity="center_vertical"
	            android:layout_marginLeft="8dip"
	            android:layout_marginRight="8dip"
	            android:paddingBottom="6dip"
	            android:src="@drawable/ic_login_user" />
	
	        <EditText
	            android:id="@+id/accountEdit"
	            android:layout_width="0dp"
	            android:layout_height="wrap_content"
	            android:layout_gravity="center_vertical"
	            android:layout_marginRight="8dip"
	            android:layout_weight="1"
	            android:background="@null"
	            android:digits="\@0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.,_-"
	            android:hint="账号"
	            android:imeOptions="actionNext"
	            android:paddingBottom="8dip"
	            android:singleLine="true"
	            android:textColor="@color/black"
	            android:textCursorDrawable="@null" />
	    </com.example.zhaoxuanli.softwidgetdemo.soft.widget.SRoundRect_LinearLayout>
	
	    <com.example.zhaoxuanli.softwidgetdemo.soft.widget.SRoundRect_LinearLayout
	        android:id="@+id/passwordLayout"
	        android:layout_width="match_parent"
	        android:layout_height="40dp"
	        android:layout_marginTop="3dp"
	        android:layout_marginRight="20dp"
	        android:layout_marginLeft="20dp"
	        android:layout_below="@id/accountLayout"
	        android:orientation="horizontal"
	        android:paddingLeft="6dip"
	        android:paddingRight="6dip"
	        android:paddingTop="8dip"
	        app:bgcolor="#CFCFCF">
	
	        <ImageView
	            android:layout_width="wrap_content"
	            android:layout_height="22dip"
	            android:layout_gravity="center_vertical"
	            android:layout_marginLeft="8dip"
	            android:layout_marginRight="8dip"
	            android:paddingBottom="6dip"
	            android:src="@drawable/ic_login_password" />
	
	        <EditText
	            android:id="@+id/passwordEdit"
	            android:layout_width="0dp"
	            android:layout_height="wrap_content"
	            android:layout_gravity="center_vertical"
	            android:layout_marginRight="8dip"
	            android:layout_weight="1"
	            android:background="@null"
	            android:digits="\@0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.,_-"
	            android:hint="密码"
	            android:imeOptions="actionNext"
	            android:paddingBottom="8dip"
	            android:singleLine="true"
	            android:textColor="@color/black"
	            android:textCursorDrawable="@null"
	            android:inputType="textPassword"/>
	    </com.example.zhaoxuanli.softwidgetdemo.soft.widget.SRoundRect_LinearLayout>
	
	    <com.example.zhaoxuanli.softwidgetdemo.soft.widget.SRoundRect_Button
	        android:id="@+id/login_btn"
	        android:layout_width="fill_parent"
	        android:layout_height="50dp"
	        android:layout_marginTop="20dp"
	        android:layout_below="@id/passwordLayout"
	        android:layout_alignLeft="@id/passwordLayout"
	        android:layout_alignRight="@id/passwordLayout"
	        android:layout_gravity="center"
	        android:text="登录"
	        android:textColor="@color/white"
	        android:textSize="20sp"
	        app:backgroundColor="#af0308"/>
	
	
	    <TextView
	        android:id="@+id/forgetText"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_alignParentBottom="true"
	        android:layout_alignParentLeft="true"
	        android:layout_marginBottom="15dp"
	        android:layout_marginLeft="10dp"
	        android:gravity="center"
	        android:textColor="#af0308"
	        android:textSize="15sp"
	        android:text="忘记密码"/>
	
	    <TextView
	        android:id="@+id/registerText"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_alignParentBottom="true"
	        android:layout_alignParentRight="true"
	        android:layout_marginBottom="15dp"
	        android:layout_marginRight="20dp"
	        android:gravity="center"
	        android:textColor="#af0308"
	        android:textSize="15sp"
	        android:text="注册"/>
	</RelativeLayout>

这里要说一下：
不加背景和形状的TextView 不需要进行优化，它只进行一次绘制。
不加形状的ImageView不需要进行优化，即使设置了background或src属性，也只绘制一次。
Button 本身是有背景的，所以你需要手动将它原来的背景去掉，在代码里：this.setBackgroundResource(0);

通过代码我们可以看到，输入框的布局和Button是自定义控件，所以我对这两个做了提前绘制优化。理论上来说是可以将小图标也提前绘制进去的，这样输入框的小图标也将是蓝色，但计算位置复杂，也不利于扩展。


DrawingCanvas.java 

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

这个类继承了Canvas，并且已经传入一个Bitmap			

		super(output_);
所以我们可以直接在这上面画画。并且还提供了一些Rect，RectF 属性，供之后进行绘制时使用。

	PaintBox.java
	
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
	        paint.setAntiAlias(true);
	        paint.setColor(color);
	        paint.setAlpha(alpha);
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
	            paint.setAlpha(30);
	            canvas.drawRoundRect(canvas.getRectF(), 20, 20, paint);
	
	            paint.setStyle(Paint.Style.STROKE);
	            paint.setAlpha(70);
	            paint.setStrokeWidth(4);
	            canvas.drawRoundRect(canvas.getRectF(),20,20,paint);
	        }else{
	            drawRoundRect(canvas,color,alpha);
	        }
	
	
	    }
	
	    /**
	     * 填充颜色
	     * @param canvas 
	     * @param color
	     */
	    public static void drawColor(DrawingCanvas canvas ,int color ){
	        Paint paint = new Paint();
	        paint.setAntiAlias(true);
	        paint.setFilterBitmap(true);
	        paint.setDither(true);
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

这个类比较简单了，你有什么需求，就在这里创建一个方法就好了，然后传入一个Canvas和一些必要参数，它帮你把你需要的东西画到Canvas上。
值得注意的是最后一个方法：drawTextCenter。 将文字绘制到View中间。这个是没有直接办法实现的，所以你需要计算一下位置。

最后是我们的自定义控件，篇幅太长，所以我们只看一个Button的，LinearLayout的可以下载代码看

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

这就是一个提前绘制的自定义控件的实现了。
这里有一个疑问：android:background 属性，我们可以设置图片、颜色、甚至是xml写得背景。这个究竟是如何实现的？
因为实现不了这个效果，所以只能添加了自定义属性

	<attr name="backgroundPic" format="reference"/>
    <attr name="backgroundColor" format="color"/>
   
感觉很Low啊这样！！！还期待大神帮解决。

哦，CSDN也上传了源代码。
http://download.csdn.net/detail/u010255127/9217927
