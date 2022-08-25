package com.imac.dr.voice_app.view.dailyexercise;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.component.ToolbarView;
import com.imac.dr.voice_app.core.FragmentLauncher;
import com.imac.dr.voice_app.util.dailyexercise.DailyExerciseInnerSelectFragment;
import com.imac.dr.voice_app.util.dailyexercise.DailyExerciseSelectFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isa on 2016/9/20.
 */
public class DailyExercise {
    private Activity activity;

    @BindView(R.id.toolbar)
    ToolbarView toolbar;
    @BindView(R.id.daily_exercise_container)
    FrameLayout dailyExerciseContainer;
    @BindView(R.id.counter)
    TextView counter;
    @BindView(R.id.counter_container)
    RelativeLayout counterContainer;

    public DailyExercise(Activity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);
        init();
    }

    private void init() {
        //設定toolbar listener實作方法
        toolbar.setToolbarButtonCallBack(onToolBarClick());
        setFont();
    }
    private void setFont(){
    }
    public void changeSelectFragment() {
        //換到尚未開始的Fragment
        FragmentLauncher.change(
                activity,
                R.id.daily_exercise_container,
                null,
                new DailyExerciseSelectFragment().getClass().getName()
        );
    }

    public void changeSelectInnerFragment() {
        FragmentLauncher.changeToBack(
                activity,
                R.id.daily_exercise_container,
                null,
                new DailyExerciseInnerSelectFragment().getClass().getName()
        );
    }

    private ToolbarView.toolbarCallBack onToolBarClick() {
        return new ToolbarView.toolbarCallBack() {
            //按下上一頁
            @Override
            public void backButtonListener() {
                //就如下排的返回建，返回上一頁
                activity.onBackPressed();
//                changeSelectFragment();
            }
            //按下回主選單
            @Override
            public void menuButtonListener() {
                //不同於onDestory會直接關閉activity，而是以下程式會繼續跑完。
                activity.finish();
                //轉場動畫(第一個參數為進入，第二個參數為退出)
                activity.overridePendingTransition(R.anim.anim_zoom_in_top, R.anim.anim_zoom_out_top);
            }
        };
    }

    public void hideCounter() {
        //隱藏counterContainer
        counterContainer.setVisibility(View.INVISIBLE);
    }
}
