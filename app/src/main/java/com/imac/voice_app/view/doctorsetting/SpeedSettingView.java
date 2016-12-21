package com.imac.voice_app.view.doctorsetting;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.module.Preferences;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isa on 2016/12/20.
 */

public class SpeedSettingView {
    @BindView(R.id.on_button)
    TextView onButton;
    @BindView(R.id.off_button)
    TextView offButton;


    @BindDrawable(R.drawable.item_self_setting_not_select)
    Drawable notSelectBackground;
    @BindDrawable(R.drawable.item_self_setting_select)
    Drawable selectBackground;
    private Activity activity;
    private Preferences preferences;

    public SpeedSettingView(Activity activity, View view) {
        this.activity = activity;
        ButterKnife.bind(this, view);
        preferences = new Preferences(activity);
        init();
    }

    private void init() {
        if (preferences.getSpeedDoctorSetting()) {
            onButton.setBackground(notSelectBackground);
            offButton.setBackground(selectBackground);
        } else {
            onButton.setBackground(selectBackground);
            offButton.setBackground(notSelectBackground);
        }
    }

    @OnClick(R.id.on_button)
    public void onOnButtonClick() {
        onButton.setBackground(selectBackground);
        offButton.setBackground(notSelectBackground);
        preferences.saveSpeedDoctorSetting(false);
    }

    @OnClick(R.id.off_button)
    public void onOffButtonClick() {
        onButton.setBackground(notSelectBackground);
        offButton.setBackground(selectBackground);
        preferences.saveSpeedDoctorSetting(true);
    }
}
