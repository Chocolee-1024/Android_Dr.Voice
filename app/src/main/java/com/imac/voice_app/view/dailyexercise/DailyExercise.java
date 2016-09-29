package com.imac.voice_app.view.dailyexercise;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.component.ToolbarView;
import com.imac.voice_app.core.FragmentLauncher;
import com.imac.voice_app.util.dailyexercise.DailyExerciseInnerSelectFragment;
import com.imac.voice_app.util.dailyexercise.DailyExerciseSelectFragment;

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
        toolbar.setToolbarButtonCallBack(onToolBarClick());
        setFont();
    }
    private void setFont(){
//        FontManager.setFont(activity,FontManager.MEDIUM,counter);
    }
    public void changeSelectFragment() {
        FragmentLauncher.change(
                activity,
                R.id.daily_exercise_container,
                null,
                new DailyExerciseSelectFragment().getClass().getName()
        );
    }

    public void changeSelectInnerFragment() {
        FragmentLauncher.change(
                activity,
                R.id.daily_exercise_container,
                null,
                new DailyExerciseInnerSelectFragment().getClass().getName()
        );
    }

    private ToolbarView.toolbarCallBack onToolBarClick() {
        return new ToolbarView.toolbarCallBack() {
            @Override
            public void backButtonListener() {
                changeSelectFragment();
            }

            @Override
            public void menuButtonListener() {
                activity.finish();
                activity.overridePendingTransition(R.anim.anim_zoom_in_top, R.anim.anim_zoom_out_top);
            }
        };
    }

    public void hideCounter() {
        counterContainer.setVisibility(View.INVISIBLE);
    }
}
