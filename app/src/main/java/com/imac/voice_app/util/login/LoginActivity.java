package com.imac.voice_app.util.login;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.imac.voice_app.module.PermissionsActivity;
import com.imac.voice_app.module.PermissionsChecker;
import com.imac.voice_app.module.SearchName;
import com.imac.voice_app.module.base.BaseGoogleDrive;
import com.imac.voice_app.util.mainmenu.MainActivity;
import com.imac.voice_app.view.login.DataChangeListener;
import com.imac.voice_app.view.login.Login;

import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements DataChangeListener {
    private ProgressDialog progressDialog;
    private Login login;
    private Activity activity;
    private PermissionsChecker permissionsChecker;
    private final static int ASK_PERMISSION_CODE = 0;
    private final String[] permission = new String[]{
            Manifest.permission.GET_ACCOUNTS
    };
    public final static  String KEY_LOGIN_ACCOUNT="key_login_account";
    public final static  String KEY_LOGIN_NAME="key_login_name";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        ButterKnife.bind(this);
        permissionsChecker = new PermissionsChecker(this);
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
            public void onSuccessful(final String account) {
                SearchName search=new SearchName(LoginActivity.this, account, new SearchName.onCallBackEvent() {
                    @Override
                    public void onSearchResult(String search) {
                        Bundle bundle =new Bundle();
                        bundle.putString(KEY_LOGIN_ACCOUNT,account);
                        bundle.putString(KEY_LOGIN_NAME,search);
                        ActivityLauncher.go(LoginActivity.this, MainActivity.class, bundle);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onSearchFail() {
                        Toast.makeText(LoginActivity.this,"此帳號未設定姓名",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
                search.execute();

            }

            @Override
            public void onFail() {
                Toast.makeText(LoginActivity.this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
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
            }
        };
    }

    @Override
    public void onDataChange(View view) {
        if (permissionsChecker.missingPermissions(permission)) {
            PermissionsActivity.startPermissionsForResult(this, ASK_PERMISSION_CODE, permission);
        } else {
            LoginChecker loginChecker = new LoginChecker(LoginActivity.this, setEventCallBack());
            loginChecker.checkFile(((EditText) view).getText().toString());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == BaseGoogleDrive.ASK_ACCOUNT) {
            String account = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            LoginChecker loginChecker = new LoginChecker(LoginActivity.this, setEventCallBack());
            if (account.equals(LoginChecker.ACCOUNT_NAME))
                loginChecker.getCredential().setSelectedAccountName(account);
            loginChecker.checkFile(login.getAccountEditText().getText().toString());

        }
    }
}