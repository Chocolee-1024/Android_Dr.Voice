package com.imac.voice_app.view.login;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.imac.voice_app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isa on 2016/8/25.
 */
public class Login {
    @BindView(R.id.login)
    protected Button loginButton;
    @BindView(R.id.account)
    protected EditText accountEditText;
    private Activity activity;
    private DataChangeListener dataChangeListener;

    public Login( Activity activity, DataChangeListener dataChangeListener,View view) {
        this.activity = activity;
        this.dataChangeListener = dataChangeListener;
        ButterKnife.bind(this,activity);
    }

    @OnClick(R.id.login)
    public void loginClick(View view) {
        if (TextUtils.isEmpty(accountEditText.getText().toString())) {
            Toast.makeText(activity, activity.getString(R.string.login_nonempty), Toast.LENGTH_SHORT).show();
        } else {
            dataChangeListener.onDataChange(accountEditText);
        }
    }

    public Button getLoginButton() {
        return this.loginButton;
    }

    public EditText getAccountEditText() {
        return this.accountEditText;
    }
}
