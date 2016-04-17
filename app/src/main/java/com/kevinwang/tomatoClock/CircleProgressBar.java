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
    private int maxProgress = 10;
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
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
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
    }

    public void setProgressNotInUiThread(int progress){
        this.progress = progress;
        this.postInvalidate();
    }
}
