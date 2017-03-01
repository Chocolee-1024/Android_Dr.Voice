package com.imac.voice_app.util.homepage;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.imac.voice_app.R;
import com.imac.voice_app.core.ActivityLauncher;
import com.imac.voice_app.module.AlarmConstantManager;
import com.imac.voice_app.module.DataAppend;
import com.imac.voice_app.module.permission.PermissionsActivity;
import com.imac.voice_app.module.permission.PermissionsChecker;
import com.imac.voice_app.util.login.LoginActivity;
import com.imac.voice_app.util.mainmenu.MainActivity;
import com.imac.voice_app.view.homepage.HomePage;

import butterknife.ButterKnife;

/**
 * Created by isa on 2016/9/8.
 */
public class HomePageActivity extends AppCompatActivity {
    private final static int ASK_PERMISSION_CODE = 1;
    private String mode = "";
    private final String[] permission = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private PermissionsChecker mPermissionsChecker;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_layout);
        ButterKnife.bind(this);
        mPermissionsChecker=new PermissionsChecker(this);
        getBundle();
        init();
    }

    private void getBundle() {
        Bundle whichMode = getIntent().getExtras();
        if (null != whichMode) {
            mode = whichMode.getString(AlarmConstantManager.INTENT_MODE, "");
        }
    }

    private void init() {
        HomePage homePage = new HomePage(this, new HomePage.OnClickEvent() {
            @Override
            public void onClick(boolean isLogin, String account, String name, String dailyExercise, String weeklyExercise) {
                if (!isLogin) {
                    ActivityLauncher.go(HomePageActivity.this, LoginActivity.class, null);
                } else {
                    DataAppend dataAppend = new DataAppend();
                    dataAppend.formatString(dailyExercise);
                    Bundle bundle = new Bundle();
                    bundle.putString(LoginActivity.KEY_LOGIN_ACCOUNT, account);
                    bundle.putString(LoginActivity.KEY_LOGIN_NAME, name);
                    ActivityLauncher.go(HomePageActivity.this, MainActivity.class, bundle);
                }
            }

        });

        if (!"".equals(mode)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if (mode.equals(AlarmConstantManager.MODE_DAILY)) {
                AlertDialog alertDialog = builder.
                        setTitle("Voice 嗓音自我照護").
                        setMessage("開始做每日練習").
                        show();

            } else if (mode.equals(AlarmConstantManager.MODE_WEEK)) {
                AlertDialog alertDialog = builder.
                        setTitle("Voice 嗓音自我照護").
                        setMessage("開始做每週練習").
                        show();
            }
        }
        if (mPermissionsChecker.missingPermissions(permission)){
            PermissionsActivity.startPermissionsForResult(this, ASK_PERMISSION_CODE, permission);
        }
    }

}
