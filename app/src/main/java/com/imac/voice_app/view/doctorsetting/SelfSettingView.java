package com.imac.voice_app.view.doctorsetting;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.module.DataAppend;
import com.imac.voice_app.module.Preferences;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isa on 2016/12/8.
 */

public class SelfSettingView {
    @BindView(R.id.topic_one)
    TextView topicOne;
    @BindView(R.id.topic_two)
    TextView topicTwo;
    @BindView(R.id.topic_three)
    TextView topicThree;
    @BindView(R.id.topic_four)
    TextView topicFour;
    @BindView(R.id.topic_five)
    TextView topicFive;
    @BindView(R.id.topic_six)
    TextView topicSix;
    @BindView(R.id.topic_seven)
    TextView topicSeven;
    @BindView(R.id.save)
    TextView save;


    @BindDrawable(R.drawable.item_self_setting_not_select)
    Drawable notSelectBackground;
    @BindDrawable(R.drawable.item_self_setting_select)
    Drawable selectBackground;

    private Activity mActivity;
    private CallbackEvent callbackEvent;
    private Boolean[] selectStatus;
    private Preferences preferences;

    public SelfSettingView(Activity activity, View view) {
        this.mActivity = activity;
        ButterKnife.bind(this, view);
        preferences = new Preferences(activity);
        selectStatus = new Boolean[7];
        for (int i = 0; i < selectStatus.length; i++) selectStatus[i] = false;
        String dailyDoctorSettingValue = preferences.getWeeklyDoctorSetting();
        DataAppend dataAppend = new DataAppend();
        if (!"".equals(dailyDoctorSettingValue)) {
            Object[] value = dataAppend.formatBoolean(dailyDoctorSettingValue).toArray();
            selectStatus = Arrays.copyOf(value, value.length, Boolean[].class);
        }
        initTextBg();
    }

    private void initTextBg() {
        ArrayList<TextView> textViewList = new ArrayList<>(6);
        textViewList.add(topicOne);
        textViewList.add(topicTwo);
        textViewList.add(topicThree);
        textViewList.add(topicFour);
        textViewList.add(topicFive);
        textViewList.add(topicSix);
        textViewList.add(topicSeven);
        for (int i = 0; i < textViewList.size(); i++)
            textViewList.get(i).setBackground(selectStatus[i] ? selectBackground : notSelectBackground);
    }

    @OnClick({R.id.topic_one, R.id.topic_two, R.id.topic_three, R.id.topic_four, R.id.topic_five, R.id.topic_six, R.id.topic_seven})
    public void onClickEvent(View view) {
        if (null == callbackEvent) return;
        int position = 0;
        switch (view.getId()) {
            case R.id.topic_one:
                position = 0;
                break;
            case R.id.topic_two:
                position = 1;
                break;
            case R.id.topic_three:
                position = 2;
                break;
            case R.id.topic_four:
                position = 3;
                break;
            case R.id.topic_five:
                position = 4;
                break;
            case R.id.topic_six:
                position = 5;
                break;
            case R.id.topic_seven:
                position = 6;
                break;
        }
        selectStatus[position] = !selectStatus[position];
        if (selectStatus[position]) view.setBackground(selectBackground);
        else view.setBackground(notSelectBackground);
        callbackEvent.onCallbackEvent(view, position);
    }

    @OnClick(R.id.save)
    public void onSave() {
        if (null == callbackEvent) return;
        callbackEvent.onClickSave(selectStatus);
    }

    public void setCallbackEvent(CallbackEvent callbackEvent) {
        this.callbackEvent = callbackEvent;
    }


    public interface CallbackEvent {
        public void onCallbackEvent(View view, int position);

        public void onClickSave(Boolean[] status);
    }
}
