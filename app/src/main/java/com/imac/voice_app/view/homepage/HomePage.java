package com.imac.voice_app.view.homepage;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.core.PreferencesHelper;
import com.imac.voice_app.module.FontManager;
import com.imac.voice_app.module.SharePreferencesManager;
import com.imac.voice_app.util.login.LoginActivity;

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
    private String name = "";
    private String dailyExercise = "";
    private SharePreferencesManager sharePreferencesManager;

    public HomePage(Activity activity, OnClickEvent event) {
        ButterKnife.bind(this, activity);
        sharePreferencesManager = SharePreferencesManager.getInstance(activity);
        this.event = event;
        this.activity = activity;
        setFontType();
    }

    private void setFontType() {
        FontManager.setFont(activity, FontManager.LIGHT, title, developer);
        FontManager.setFont(activity, FontManager.MEDIUM, start);
    }

    private boolean isLogin() {
        boolean isLogin;
        account = (String) sharePreferencesManager.get(LoginActivity.KEY_LOGIN_ACCOUNT, PreferencesHelper.Type.STRING);
        name = (String) sharePreferencesManager.get(LoginActivity.KEY_LOGIN_NAME, PreferencesHelper.Type.STRING);
        dailyExercise = (String) sharePreferencesManager.get(LoginActivity.KEY_DAILY_EXERCISE, PreferencesHelper.Type.STRING);
        if ("".equals(account) || "".equals(name) || "".equals(dailyExercise)) {
            isLogin = false;
        } else {
            isLogin = true;
        }
        return isLogin;
    }

    @OnClick(R.id.start)
    public void clickStart() {
        event.onClick(isLogin(), account, name, dailyExercise);
    }

    public interface OnClickEvent {
        void onClick(boolean isLogin, String account, String name, String dailyExercise);
    }
}
