package com.imac.voice_app.view.speakspeed;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.percent.PercentRelativeLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.component.CustomProgressBar;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * SpeakSpeedActivity view
 * Created by flowmaHuang on 2016/9/5.
 */
public class SpeakSpeedView {
    @BindView(R.id.tv_date)
    TextView mDateText;
    @BindView(R.id.rl_detail_data)
    PercentRelativeLayout mDetailContainer;
    @BindView(R.id.bar_view)
    CustomProgressBar mCustomBarView;
    @BindView(R.id.iv_status_emoticon)
    ImageView mEmoticonImageView;
    @BindView(R.id.tv_explanation)
    TextView mPleaseStartTextView;
    @BindView(R.id.tv_status_hint)
    TextView mStatusHintText;
    @BindView(R.id.btn_Check)
    ImageView mCheckButton;
    @BindView(R.id.btn_close)
    ImageView mCloseButton;

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

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String todayDate = format.format(date);

        mDateText.setText(todayDate);
        setSquareView();
        setFontStyle();
    }

    private void setSquareView() {
        int viewLength;

        viewLength = mDetailContainer.getLayoutParams().width;
        mDetailContainer.getLayoutParams().height = viewLength;
        mDetailContainer.getLayoutParams().width = viewLength;
        Log.e("123", Integer.toString(viewLength));
    }

    private void setFontStyle() {
        Typeface fontTypeMedium = Typeface.createFromAsset(mContext.getAssets(), "font/SourceHanSansTWHK-Medium.otf");
        mDateText.setTypeface(fontTypeMedium);
//        思源字體在數字使用上會造成上下間距變為極大
//        mCalculateSpeed.setTypeface(fontTypeMedium);
        mStatusHintText.setTypeface(fontTypeMedium);
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
            mEmoticonImageView.setImageResource(R.drawable.toofast);
            if (Build.VERSION.SDK_INT > 22) {
                mStatusHintText.setTextColor(mContext.getResources().getColor(R.color.speak_speed_too_fast, null));
            } else {
                mStatusHintText.setTextColor(mContext.getResources().getColor(R.color.speak_speed_too_fast));
            }
        } else if (Integer.parseInt(s) > 160) {
            mStatusHintText.setText(R.string.speak_slower);
            mEmoticonImageView.setImageResource(R.drawable.slower);
            if (Build.VERSION.SDK_INT > 22) {
                mStatusHintText.setTextColor(mContext.getResources().getColor(R.color.speak_speed_slower, null));
            } else {
                mStatusHintText.setTextColor(mContext.getResources().getColor(R.color.speak_speed_slower));
            }
        } else {
            mStatusHintText.setText(R.string.speak_good);
            mEmoticonImageView.setImageResource(R.drawable.good);
            if (Build.VERSION.SDK_INT > 22) {
                mStatusHintText.setTextColor(mContext.getResources().getColor(R.color.speak_speed_good, null));
            } else {
                mStatusHintText.setTextColor(mContext.getResources().getColor(R.color.speak_speed_good));
            }
        }
        mCustomBarView.setAnglePercent(percent);
        mCustomBarView.invalidate();
    }

    public void setStartTextViewVisibility (boolean viewVisibility){
        if (viewVisibility) {
            mPleaseStartTextView.setVisibility(View.VISIBLE);
        } else {
            mPleaseStartTextView.setVisibility(View.INVISIBLE);
        }
    }

    public void setmStatusHintText(String text){
        mStatusHintText.setText(text);
    }
}
