package com.imac.voice_app.view.speakspeed;

import android.app.Activity;
import android.content.Context;
import android.support.percent.PercentRelativeLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.component.CustomProgressBar;
import com.imac.voice_app.module.FontManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * SpeakSpeedActivity view
 * Created by flowmaHuang on 2016/9/5.
 */
public class SpeakSpeedView {
    @BindView(R.id.tv_time)
    TextView mTimeText;
    @BindView(R.id.tv_date)
    TextView mDateText;
    @BindView(R.id.rl_detail_data)
    PercentRelativeLayout mDetailContainer;
    @BindView(R.id.bar_view)
    CustomProgressBar mCustomBarView;
    @BindView(R.id.tv_calculate_num)
    TextView mCalculateSpeed;
    @BindView(R.id.tv_status_hint)
    TextView mStatusHintText;
    @BindView(R.id.btn_Check)
    ImageView mCheckButton;
    @BindView(R.id.btn_close)
    ImageView mCloseButton;

    @BindColor(R.color.speak_speed_idle)
    int colorStatusIdle;
    @BindColor(R.color.speak_speed_good)
    int colorStatusGood;
    @BindColor(R.color.speak_speed_too_fast)
    int colorStatusTooFast;
    @BindColor(R.color.speak_speed_slower)
    int colorStatusSlower;

    private Context mContext;

    public interface callBackListener {
        void checkButton();

        void closeButton();
    }

    private callBackListener listener = null;

    public SpeakSpeedView(Activity activity, callBackListener listener) {
        ButterKnife.bind(this, activity);
        mContext = activity.getApplicationContext();
        this.listener = listener;
        initViewSet();
        Log.e("colorG",Integer.toString(colorStatusIdle));
    }

    private void initViewSet(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String todayDate = format.format(date);
        mDateText.setText(todayDate);

        int viewLength;
        viewLength = mDetailContainer.getLayoutParams().width;
        mDetailContainer.getLayoutParams().height = viewLength;
        mDetailContainer.getLayoutParams().width = viewLength;
        Log.e("123", Integer.toString(viewLength));

        //        思源字體在數字使用上會造成上下間距變為極大
        FontManager.setFont(mContext,FontManager.MEDIUM,
                mTimeText,mDateText,mStatusHintText);
    }

    @OnClick({R.id.btn_Check, R.id.btn_close})
    public void startButtonListener(ImageView view) {
        switch (view.getId()) {
            case R.id.btn_Check:
                listener.checkButton();
                break;
            case R.id.btn_close:
                listener.closeButton();
                break;
        }
    }

    public void setTimeTextViewText(String s) {
        mTimeText.setText(s);
    }

    public void setButtonStatus(boolean status) {
        if (status) {
            mCheckButton.setImageResource(R.drawable.stop_button);
            mCloseButton.setVisibility(View.VISIBLE);
        } else {
            mCheckButton.setImageResource(R.drawable.mike_button);
            mCloseButton.setVisibility(View.INVISIBLE);
        }
    }

    public void setCalculateSpeedText(String s, int percent) {
        if (Integer.parseInt(s) > 200) {
            mStatusHintText.setText(R.string.speak_too_fast);
            mStatusHintText.setTextColor(colorStatusTooFast);
            mCalculateSpeed.setTextColor(colorStatusTooFast);
        } else if (Integer.parseInt(s) > 160) {
            mStatusHintText.setText(R.string.speak_slower);
            mStatusHintText.setTextColor(colorStatusSlower);
            mCalculateSpeed.setTextColor(colorStatusSlower);
        } else {
            mStatusHintText.setText(R.string.speak_good);
            mStatusHintText.setTextColor(colorStatusGood);
            mCalculateSpeed.setTextColor(colorStatusGood);
        }
        mCalculateSpeed.setText(s);

        mCustomBarView.setAnglePercent(percent);
        mCustomBarView.invalidate();
    }

    public void stopButtonResetView() {
        mStatusHintText.setTextColor(colorStatusIdle);
        mCalculateSpeed.setTextColor(colorStatusIdle);
        mCustomBarView.setAnglePercent(0);
        mCustomBarView.invalidate();
        mTimeText.setText(mContext.getText(R.string.speak_speed_time_default));
        mStatusHintText.setText(mContext.getText(R.string.speak_start_hint_default));
        mCalculateSpeed.setText(mContext.getText(R.string.zero));

    }
}
