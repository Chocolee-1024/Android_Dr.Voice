package com.imac.voice_app.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.imac.voice_app.R;

/*
* 自定義類進度條
* Created by flowmaHuang on 2016/9/7.
*/
public class CustomProgressBar extends View {
    private int percent;
    private Paint insideCircle;
    private Paint outsideCircle;
    private Paint graduationPoint;
    private Paint graduationText;

    private Context mContext;

    public CustomProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;
        initPaintSetting();
    }

    private void initPaintSetting() {
        percent = 0;

        insideCircle = new Paint();
        insideCircle.setColor(getResources().getColor(R.color.speak_speed_idle));
        insideCircle.setStyle(Paint.Style.STROKE);
        insideCircle.setStrokeCap(Paint.Cap.ROUND);
        insideCircle.setStrokeWidth(15);

        outsideCircle = new Paint();
        outsideCircle.setColor(Color.BLUE);
        outsideCircle.setStyle(Paint.Style.STROKE);
        outsideCircle.setStrokeCap(Paint.Cap.ROUND);
        outsideCircle.setStrokeWidth(20);

        graduationPoint = new Paint();
        graduationPoint.setColor(getResources().getColor(R.color.speak_speed_point));

        graduationText = new Paint();
        graduationText.setColor(getResources().getColor(R.color.speak_speed_point));
        graduationText.setTextSize(sp2px(mContext, 12));
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

        RectF oval = new RectF(widthPercent * 5, heightPercent * 5, widthPercent * 95, heightPercent * 95);
        // 畫弧，參數依序是RectF、開始角度、經過角度、畫弧線/扇形、畫筆
        canvas.drawArc(oval, 120, 300, false, insideCircle);
        canvas.drawArc(oval, 120, anglePercent * percent, false, outsideCircle);

        float cx = canvas.getWidth() / 2 + widthPercent * 45 * (float) Math.cos(Math.toRadians(279));
        float cy = canvas.getHeight() / 2 + heightPercent * 45 * (float) Math.sin(Math.toRadians(279));
        canvas.drawCircle(cx, cy, 5, graduationPoint);
        canvas.drawText("160", cx - widthPercent * 5, cy + heightPercent * 7, graduationText);

        cx = canvas.getWidth() / 2 + widthPercent * 45 * (float) Math.cos(Math.toRadians(320));
        cy = canvas.getHeight() / 2 + heightPercent * 45 * (float) Math.sin(Math.toRadians(320));
        canvas.drawCircle(cx, cy, 5, graduationPoint);
        canvas.drawText("200", cx - widthPercent * 9, cy + heightPercent * 5, graduationText);
    }

    public void setAnglePercent(int percent) {
        this.percent = percent;
        if (percent > 66) {
            if (Build.VERSION.SDK_INT > 22) {
                outsideCircle.setColor(getResources().getColor(R.color.speak_speed_slower, null));
            } else {
                outsideCircle.setColor(getResources().getColor(R.color.speak_speed_slower));
            }
        } else if (percent > 53) {
            if (Build.VERSION.SDK_INT > 22) {
                outsideCircle.setColor(getResources().getColor(R.color.speak_speed_too_fast, null));
            } else {
                outsideCircle.setColor(getResources().getColor(R.color.speak_speed_too_fast));
            }
        } else {
            if (Build.VERSION.SDK_INT > 22) {
                outsideCircle.setColor(getResources().getColor(R.color.speak_speed_good, null));
            } else {
                outsideCircle.setColor(getResources().getColor(R.color.speak_speed_good));
            }
        }
    }

    private static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}