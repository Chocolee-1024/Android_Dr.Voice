package com.imac.voice_app.view.doctorsetting;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.imac.voice_app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isa on 2016/12/6.
 */

public class DoctorSettingMenuView {
    @BindView(R.id.daily_button)
    Button dailyButton;
    @BindView(R.id.self_button)
    Button selfButton;
    @BindView(R.id.speed_button)
    Button speedButton;

    private Activity mActivity;
    private CallbackEvent callbackEvent;

    public DoctorSettingMenuView(Activity activity, View view) {
        this.mActivity = activity;
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.daily_button)
    public void onDailyClick() {
        if (null != callbackEvent)
            callbackEvent.onDailyClick();
    }

    @OnClick(R.id.self_button)
    public void onSelfClick() {
        if (null != callbackEvent)
            callbackEvent.onSelfClick();
    }

    @OnClick(R.id.speed_button)
    public void onSpeedButton(){
        if (null != callbackEvent)
            callbackEvent.onSpeedClick();
    }

    public void setCallbackEvent(CallbackEvent callbackEvent) {
        this.callbackEvent = callbackEvent;
    }


    public interface CallbackEvent {
        void onDailyClick();

        void onSelfClick();

        void onSpeedClick();
    }
}
