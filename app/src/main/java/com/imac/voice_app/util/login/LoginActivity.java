package com.imac.voice_app.util.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.imac.voice_app.R;
import com.imac.voice_app.core.ActivityLauncher;
import com.imac.voice_app.module.LoginChecker;
import com.imac.voice_app.util.MainActivity;
import com.imac.voice_app.view.login.DataChangeListener;
import com.imac.voice_app.view.login.Login;

import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements DataChangeListener {
    private ProgressDialog progressDialog;
    private Login login;
    private Activity activity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);

        ButterKnife.bind(this);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.activity_main, null);
        activity = this;
        login = new Login(activity, this, view);
    }

    private LoginChecker.eventCallBack setEventCallBack() {
        return new LoginChecker.eventCallBack() {
            @Override
            public void onSuccessful() {
                ActivityLauncher.go(LoginActivity.this, MainActivity.class, null);
            }

            @Override
            public void onFail() {
                Toast.makeText(LoginActivity.this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void showProgress() {
                progressDialog = ProgressDialog.show(LoginActivity.this,
                        getString(R.string.login_loading),
                        getString(R.string.login_wait),
                        true
                );
            }

            @Override
            public void hideProgress() {
                progressDialog.dismiss();
            }
        };
    }

    @Override
    public void onDataChange(View view) {
        LoginChecker checker = new LoginChecker(LoginActivity.this, setEventCallBack());
        checker.checkFile(((EditText) view).getText().toString());
    }
}