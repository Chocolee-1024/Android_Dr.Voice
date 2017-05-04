package com.imac.voice_app.view.homepage;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.module.Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isa on 2016/9/8.
 */
public class HomePage {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.start)
    Button start;
    @BindView(R.id.developer)
    TextView developer;
    private OnClickEvent event;
    private Activity activity;
    private String account = "";
//    private String name = "";
    private String dailyExercise = "";
    private String weeklyExercise = "";
    private Preferences preferences;

    public HomePage(Activity activity, OnClickEvent event) {
        ButterKnife.bind(this, activity);
        preferences = new Preferences(activity);
        this.event = event;
        this.activity = activity;
        setFontType();
    }

    private void setFontType() {
    }

//    private boolean isLogin() {
//        boolean isLogin;
//        account = preferences.getAccounnt();
////        name = preferences.getName();
////        dailyExercise = (String) sharePreferencesManager.get(LoginActivity.KEY_DAILY_EXERCISE, PreferencesHelper.Type.STRING);
////        weeklyExercise = (String) sharePreferencesManager.get(LoginActivity.KEY_WEEKLY_EXERCISE, PreferencesHelper.Type.STRING);
//        if ("".equals(account)) {
//            isLogin = false;
//        } else {
//            isLogin = true;
//        }
//        return isLogin;
//    }

    @OnClick(R.id.start)
    public void clickStart() {
        event.onClick( dailyExercise, weeklyExercise);
    }

    public interface OnClickEvent {
        void onClick(String dailyExercise, String weeklyExercise);
    }
}
