package com.wangxingxing.mydemo;

/**
 * author : 王星星
 * date : 2023/5/29 14:34
 * email : 1099420259@qq.com
 * description :
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;

public class CustomCircleProgressBar extends View {
    private int progress = 0;
    private Paint backgroundPaint;
    private Paint progressPaint;
    private Paint textPaint;

    private int backgroundColor = Color.GRAY;
    private int progressColor = Color.BLUE;
    private int textColor = Color.BLACK;

    public CustomCircleProgressBar(Context context) {
        super(context);
        init();
    }

    public CustomCircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(ConvertUtils.dp2px(3));

        progressPaint = new Paint();
        progressPaint.setColor(progressColor);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(ConvertUtils.dp2px(3));

        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(ConvertUtils.sp2px(10));
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        backgroundPaint.setColor(backgroundColor);
        progressPaint.setColor(progressColor);
        textPaint.setColor(textColor);

        int viewWidth = getWidth();
        int viewHeight = getHeight();
        int centerX = viewWidth / 2;
        int centerY = viewHeight / 2;
        int radius = Math.min(viewWidth, viewHeight) / 2 - 20;

        // 绘制背景圆
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint);

        // 绘制进度条
        RectF progressRect = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        float sweepAngle = 360 * ((float) progress / 100);
        canvas.drawArc(progressRect, -90, sweepAngle, false, progressPaint);

        // 绘制进度文本
        String progressText = progress + "%";
        Rect textBounds = new Rect();
        textPaint.getTextBounds(progressText, 0, progressText.length(), textBounds);
        float textX = centerX;
        float textY = centerY - (textBounds.top + textBounds.bottom) / 2; // 调整文本位置向上移动
        canvas.drawText(progressText, textX, textY, textPaint);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate(); // 重新绘制视图
    }

    public void setBackgroundColor(int color) {
        backgroundColor = color;
        invalidate();
    }

    public void setProgressColor(int color) {
        progressColor = color;
        invalidate();
    }

    public void setTextColor(int color) {
        textColor = color;
        invalidate();
    }

}
