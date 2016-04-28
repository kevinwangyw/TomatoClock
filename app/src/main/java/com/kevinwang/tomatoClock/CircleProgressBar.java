package com.kevinwang.tomatoClock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lenovo on 2016/4/15.
 */
public class CircleProgressBar extends View{
    private int maxProgress = 30;
    private int progress;
    private int progressStrokeWidth = 8;  //绘制图形线宽
    private Context context;
    private int startAngle = -90;
    int center;
    int radius;
    //画圆所在矩形区域
    RectF oval;
    Paint ringPaint;
    Paint progressPaint;

    public int getProgress() {
        return progress;
    }

    public CircleProgressBar(Context context) {
        super(context);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        oval = new RectF();
        ringPaint = new Paint();
        progressPaint = new Paint();

        ringPaint.setColor(Color.GRAY); //设置圆环的颜色
        ringPaint.setStyle(Paint.Style.STROKE); //设置空心
        ringPaint.setStrokeWidth(progressStrokeWidth); //设置圆环宽度
        ringPaint.setAntiAlias(true);
        //只绘圆周，不含圆心, 不填充扫描过的扇形
        progressPaint.setColor(Color.rgb(0xcc,0x0,0x0));
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(progressStrokeWidth);
        progressPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(center, center, radius, ringPaint);
        float angle = 360 * progress / maxProgress;
        canvas.drawArc(oval, startAngle, angle, false, progressPaint);
        //oval：指定圆弧的外轮廓矩形区域。。
        //startAngle：圆弧的起始角度。
        //sweepAngle：圆弧扫过的角度，顺时针方向，单位为度。
        //useCenter：是否显示半径连线，true表示显示圆弧与圆心的半径连线，false表示不显示。
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*onMeasure方法是测量view和它的内容，决定measured width和measured height的，这个方法由 measure(int, int)方法唤起，
        子类可以覆写onMeasure来提供更加准确和有效的测量。有一个约定：在覆写onMeasure方法的时候，必须调用 setMeasuredDimension(int,int)
        来存储这个View经过测量得到的measured width and height。如果没有这么做，将会由measure(int, int)方法抛出一个IllegalStateException。*/
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);  //子类有责任确保measured height and width至少为这个View的最小height和width。
        setMeasuredDimension(min, min);
        center = width/2; //获取圆心的x坐标
        radius = (int) (center - progressStrokeWidth/2); //圆环的半径
        oval.set(center - radius, center - radius, center
                + radius, center + radius);
    }

    public int getMaxProgress(){
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress){
        this.maxProgress = maxProgress;
    }

    public void setProgress(int progress){
        this.progress = progress;
        this.invalidate();
        //Invalidate the whole view. If the view is visible, onDraw(android.graphics.Canvas) will be called at some point in the future.
    }

    public void setProgressNotInUiThread(int progress){
        this.progress = progress;
        this.postInvalidate();
    }
}
