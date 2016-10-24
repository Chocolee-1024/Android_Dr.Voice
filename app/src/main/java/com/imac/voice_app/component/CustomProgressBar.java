package com.imac.voice_app.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.imac.voice_app.R;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.ButterKnife;

/*
* 自定義類進度條
* Created by flowmaHuang on 2016/9/7.
*/
public class CustomProgressBar extends View {
    @BindColor(R.color.speak_speed_idle)
    int colorStatusIdle;
    @BindColor(R.color.speak_speed_good)
    int colorStatusGood;
    @BindColor(R.color.speak_speed_too_fast)
    int colorStatusTooFast;
    @BindColor(R.color.speak_speed_slower)
    int colorStatusSlower;
    @BindColor(R.color.speak_speed_point)
    int colorPoint;
    @BindString(R.string.speak_graduation_standard)
    String graduationTextPaintStandard;
    @BindString(R.string.speak_graduation_faster)
    String graduationTextPaintFaster;
    private int percent;
    private Paint insideCirclePaint;
    private Paint outsideCirclePaint;
    private Paint graduationPoint;
    private Paint graduationTextPaint;
    private Paint graduationNumberPaint;

    private Context mContext;

    public CustomProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        ButterKnife.bind(this);
        mContext = context;
        initPaintSetting();
    }

    private void initPaintSetting() {
        percent = 0;

        insideCirclePaint = new Paint();
        insideCirclePaint.setColor(getResources().getColor(R.color.speak_speed_idle));
        insideCirclePaint.setStyle(Paint.Style.STROKE);
        insideCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        insideCirclePaint.setStrokeWidth(15);

        outsideCirclePaint = new Paint();
        outsideCirclePaint.setColor(Color.BLUE);
        outsideCirclePaint.setStyle(Paint.Style.STROKE);
        outsideCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        outsideCirclePaint.setStrokeWidth(20);

        graduationPoint = new Paint();
        graduationPoint.setColor(colorPoint);

        graduationTextPaint = new Paint();
        graduationTextPaint.setColor(getResources().getColor(R.color.speak_speed_point));
        graduationTextPaint.setTextSize(sp2px(mContext, 14));

        graduationNumberPaint = new Paint();
        graduationNumberPaint.setColor(getResources().getColor(R.color.speak_speed_point));
        graduationNumberPaint.setTextSize(sp2px(mContext, 16));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.getLayoutParams().height = canvas.getWidth();

        if (canvas.getWidth() > canvas.getHeight()) {
            this.getLayoutParams().width = canvas.getHeight();
        } else {
            this.getLayoutParams().height = canvas.getWidth();
        }

        Log.e("canvas getWidth", Integer.toString(canvas.getWidth()));
        Log.e("canvas getHeight", Integer.toString(canvas.getHeight()));

        float widthPercent = canvas.getWidth() / 100f;
        float heightPercent = canvas.getHeight() / 100f;
        float anglePercent = 300 / 100f;

        insideCirclePaint.setStrokeWidth(widthPercent * 3.5f);
        outsideCirclePaint.setStrokeWidth(widthPercent * 5f);

        RectF oval = new RectF(widthPercent * 10, heightPercent * 10, widthPercent * 90, heightPercent * 90);
        // 畫弧，參數依序是RectF、開始角度、經過角度、畫弧線/扇形、畫筆
        canvas.drawArc(oval, 120, 300, false, insideCirclePaint);
        canvas.drawArc(oval, 120, anglePercent * percent, false, outsideCirclePaint);

        float cx = canvas.getWidth() / 2 + widthPercent * 40 * (float) Math.cos(Math.toRadians(279));
        float cy = canvas.getHeight() / 2 + heightPercent * 40 * (float) Math.sin(Math.toRadians(279));
        canvas.drawCircle(cx, cy, widthPercent * 1.3f, graduationPoint);
        canvas.drawText("160", cx - widthPercent * 3, cy - heightPercent * 3, graduationNumberPaint);
        canvas.drawText(graduationTextPaintStandard, cx - widthPercent * 7, cy + heightPercent*8, graduationTextPaint);

        cx = canvas.getWidth() / 2 + widthPercent * 40 * (float) Math.cos(Math.toRadians(320));
        cy = canvas.getHeight() / 2 + heightPercent * 40 * (float) Math.sin(Math.toRadians(320));
        canvas.drawCircle(cx, cy, widthPercent * 1.3f, graduationPoint);
        canvas.drawText("200", cx + widthPercent * 3, cy - heightPercent*2, graduationNumberPaint);
        canvas.drawText(graduationTextPaintFaster, cx - widthPercent * 10, cy + heightPercent*5, graduationTextPaint);
    }

    public void setAnglePercent(int percent) {
        if (percent > 100) {
            this.percent = 100 ;
        } else {
            this.percent = percent;
        }
        if (percent > 66) {
            outsideCirclePaint.setColor(colorStatusTooFast);
        } else if (percent > 53) {
            outsideCirclePaint.setColor(colorStatusSlower);
        } else {
            outsideCirclePaint.setColor(colorStatusGood);
        }
    }

    private static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}