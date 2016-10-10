package com.imac.voice_app.view.homepage;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.core.PreferencesHelper;
import com.imac.voice_app.module.FontManager;
import com.imac.voice_app.util.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isa on 2016/9/8.
 */
public class HomePage extends PreferencesHelper {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.start)
    Button start;
    @BindView(R.id.developer)
    TextView developer;
    private OnClickEvent event;
    private Activity activity;
    private String account = "";
    private String name = "";
    private String dailyExercise = "";

    public HomePage(Activity activity, OnClickEvent event) {
        super(activity);
        ButterKnife.bind(this, activity);
        this.event = event;
        this.activity = activity;
        setFontType();
    }

    private void setFontType() {
        FontManager.setFont(activity, FontManager.LIGHT, title, developer);
        FontManager.setFont(activity, FontManager.MEDIUM, start);
    }

    private boolean isLogin() {
        boolean isLogin = false;
        if ("".equals(get(LoginActivity.KEY_LOGIN_ACCOUNT, Type.STRING)) ||
                "".equals(get(LoginActivity.KEY_LOGIN_NAME, Type.STRING)) ||
                "".equals(get(LoginActivity.KEY_DAILY_EXERCISE, Type.STRING))) {
        } else {
            account = (String) get(LoginActivity.KEY_LOGIN_ACCOUNT, Type.STRING);
            name = (String) get(LoginActivity.KEY_LOGIN_NAME, Type.STRING);
            dailyExercise = (String) get(LoginActivity.KEY_DAILY_EXERCISE, Type.STRING);
            isLogin = true;
        }
        return isLogin;
    }

    @OnClick(R.id.start)
    public void clickStart() {
        event.onClick(isLogin(), account, name, dailyExercise);
    }

    @Override
    public String getClassName() {
        return activity.getPackageName();
    }

    public interface OnClickEvent {
        void onClick(boolean isLogin, String account, String name, String dailyExercise);
    }
}
