package com.imac.voice_app.component;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.imac.voice_app.R;
import com.imac.voice_app.core.DpToPx;

/**
 * Created by isa on 2016/10/20.
 */
public class Histogram extends View {
    private DpToPx equalizer = null;
    private Paint drawTriangle = null;
    private Paint drawLine = null;
    private Paint drawChart = null;
    private Paint drawText = null;
    private Path path = null;
    private int dataCount = 1;
    private String[] date = new String[0];
    private String[] topicPoint = new String[0];
    private float[] point = new float[0];
    private ValueAnimator[] animator = null;
    private boolean isAnimator = false;


    public Histogram(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Histogram);
        isAnimator = typedArray.getBoolean(R.styleable.Histogram_isAnimator, false);
        int chartColor = typedArray.getInteger(R.styleable.Histogram_chartColor, Color.BLACK);
        int textColor = typedArray.getInteger(R.styleable.Histogram_textColor, Color.BLACK);
        int baseLineColor = typedArray.getInteger(R.styleable.Histogram_chartBaseLineColor, Color.GRAY);
        float chartStrokeDp = typedArray.getDimension(R.styleable.Histogram_chartStrokeDp, 0f);
        float chartLineDp = typedArray.getDimension(R.styleable.Histogram_chartLineDp, 0f);
        float chartTextSizeDp = typedArray.getDimension(R.styleable.Histogram_chartTextSizeDp, 0f);

        equalizer = new DpToPx(context);

        path = new Path();
        drawTriangle = new Paint();
        drawTriangle.setColor(baseLineColor);
        drawTriangle.setStyle(Paint.Style.FILL);

        drawChart = new Paint();
        drawChart.setColor(chartColor);
        drawChart.setStrokeWidth(chartStrokeDp);

        drawLine = new Paint();
        drawLine.setColor(baseLineColor);
        drawLine.setStyle(Paint.Style.FILL);
        drawLine.setStrokeWidth(chartLineDp);

        drawText = new Paint();
        drawText.setTextSize(chartTextSizeDp);
        drawText.setTextAlign(Paint.Align.CENTER);
        drawText.setColor(textColor);

        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();
//-----------------------------x-------------------
        float leftTriangleStartX = width * 0.05f;
        float RightTriangleStartX = width - leftTriangleStartX;
//-----------------------------y-------------------
        float triangleYOffset = height * 0.01f;
        float triangleStartY = height * 0.8f;
        float triangleTopY = triangleStartY - triangleYOffset;
        float triangleBottomY = triangleStartY + triangleYOffset;
//-------------------------------------------------------------------
        path.moveTo(0, triangleStartY);
        path.lineTo(leftTriangleStartX, triangleBottomY);
        path.lineTo(leftTriangleStartX, triangleTopY);
        path.close();
        canvas.drawPath(path, drawTriangle);
        path.moveTo(width, triangleStartY);
        path.lineTo(RightTriangleStartX, triangleBottomY);
        path.lineTo(RightTriangleStartX, triangleTopY);
        path.close();
        canvas.drawPath(path, drawTriangle);
        canvas.drawLine(leftTriangleStartX, triangleStartY, width - leftTriangleStartX, triangleStartY, drawLine);
//-------------------------------------------------------------------
        float lineWidth = width - 2 * leftTriangleStartX;
        float offset = lineWidth / dataCount;
        float[] offsetCenter = new float[dataCount];
        for (int i = 0; i < dataCount; i++) {
            offsetCenter[i] = leftTriangleStartX + (2 * i + 1) * offset / 2;
        }
        for (int i = 0; i < offsetCenter.length; i++) {
            float chartValue = isAnimator ? (float) animator[i].getAnimatedValue() : 1 - point[i];
            canvas.drawLine(offsetCenter[i], triangleStartY, offsetCenter[i], (triangleStartY) * chartValue, drawChart);
            canvas.drawText(topicPoint[i], offsetCenter[i], (triangleStartY) * chartValue - 0.03f * width, drawText);
            canvas.drawText(date[i], offsetCenter[i], triangleStartY + height * 0.1f, drawText);
            if (animator[i].isRunning()) {
                invalidate();
            }
        }
    }

    public void startDraw(String[] date, float[] pointPercent,String [] topicPoint) {
        this.date = date;
        this.point = pointPercent;
        this.topicPoint = topicPoint;
        dataCount = date.length;
        animator = new ValueAnimator[dataCount];
        startAnimator();
        invalidate();
    }

    public void startAnimator() {
        for (int i = 0; i < animator.length; i++) {
            if (point[i] == 1)
                animator[i] = ValueAnimator.ofFloat(1.0f, 0.1f);
            else if (point[i] == 0)
                animator[i] = ValueAnimator.ofFloat(1.0f, 1.0f);
            else
                animator[i] = ValueAnimator.ofFloat(1.0f, (1 - point[i] + 0.1f));
            animator[i].setDuration(1000);
            if (isAnimator) {
                animator[i].start();
            }
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width;
        int height;
        if (isInEditMode()) {
            width = measureDimension(300, widthMeasureSpec);
            height = measureDimension(400, heightMeasureSpec);
        } else {
            width = measureDimension(equalizer.dp(300), widthMeasureSpec);
            height = measureDimension(equalizer.dp(400), heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int resultSize = defaultSize;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            resultSize = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            resultSize = Math.min(resultSize, specSize);
        } else {
            resultSize = defaultSize;
        }
        return resultSize;
    }
}
