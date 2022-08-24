package com.imac.dr.voice_app.util.homepage;

import android.Manifest;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.core.ActivityLauncher;
import com.imac.dr.voice_app.module.AlarmConstantManager;
import com.imac.dr.voice_app.module.DataAppend;
import com.imac.dr.voice_app.module.permission.PermissionsActivity;
import com.imac.dr.voice_app.module.permission.PermissionsChecker;
import com.imac.dr.voice_app.util.mainmenu.MainActivity;
import com.imac.dr.voice_app.view.homepage.HomePage;

import butterknife.ButterKnife;

/**
 * Created by isa on 2016/9/8.
 */
public class HomePageActivity extends AppCompatActivity {
    private final static int ASK_PERMISSION_CODE = 1;
    private String mode = "";
    private final String[] permission = new String[]{
            //允許應用程序寫入外部存儲
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private PermissionsChecker mPermissionsChecker;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_layout);
        ButterKnife.bind(this);//奶油刀綁定
        mPermissionsChecker=new PermissionsChecker(this);
        getBundle();
        init();
    }
    //拿取模式(Mode)
    private void getBundle() {
        Bundle whichMode = getIntent().getExtras();
        //判斷是否為null
        if (null != whichMode) {
            mode = whichMode.getString(AlarmConstantManager.INTENT_MODE, "");
        }
    }

    private void init() {
        //new出HomePage。
        HomePage homePage = new HomePage(this, new HomePage.OnClickEvent() {
            @Override
            public void onClick(String dailyExercise, String weeklyExercise) {
                    //new出DataAppend
                    DataAppend dataAppend = new DataAppend();
                    dataAppend.formatString(dailyExercise);
                    //跳頁
                    ActivityLauncher.go(HomePageActivity.this, MainActivity.class, null);
            }
        });
        //判斷mode是否不等於""。
        if (!"".equals(mode)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //如果為MODE_DAILY。
            if (mode.equals(AlarmConstantManager.MODE_DAILY)) {
                AlertDialog alertDialog = builder.
                        setTitle("Voice 嗓音自我照護").
                        setMessage("開始做每日練習").
                        show();
            //如果為MODE_WEEK。
            } else if (mode.equals(AlarmConstantManager.MODE_WEEK)) {
                AlertDialog alertDialog = builder.
                        setTitle("Voice 嗓音自我照護").
                        setMessage("開始做每週練習").
                        show();
            }
        }

        //判斷是否有權限。
        if (mPermissionsChecker.missingPermissions(permission)){
            //要權限
            PermissionsActivity.startPermissionsForResult(this, ASK_PERMISSION_CODE, permission);
        }
    }

}
