package com.imac.dr.voice_app.view.dailyexercise;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.ViewPager;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.component.CustomIndicator;
import com.imac.dr.voice_app.core.FragmentLauncher;
import com.imac.dr.voice_app.util.dailyexercise.DailyExerciseActivity;
import com.imac.dr.voice_app.util.dailyexercise.DailyExerciseInnerSelectFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isa on 2016/9/20.
 */
public class DailySelectExerciseView implements ViewPager.OnPageChangeListener {
    public static final String KEY_INNER_FRAGMENT_INDEX = "key_inner_fragment_index";
    private Activity activity;
    @BindView(R.id.fragment_container)
    ViewPager fragmentContainer;
    @BindView(R.id.next_step_button)
    Button nextStepButton;
    @BindView(R.id.previous_step_button)
    Button previousStepButton;
    @BindView(R.id.indicator)
    CustomIndicator indicator;
    @BindView(R.id.start_button)
    Button startButton;
    private DailySelectExerciseAdapter adapter;

    ArrayList<Integer> topic;
    boolean[] isFinish;

    public DailySelectExerciseView(Activity activity, View view) {
        this.activity = activity;
        ButterKnife.bind(this, view);
        //拿取要做的Topic
        topic = ((DailyExerciseActivity) activity).getTopic();
        //傳入哪些Topic尚未做完
        isFinish = ((DailyExerciseActivity) activity).isFinish();
        init();
    }

    private void init() {
//        setFont();
        //呼叫 hideCountDown
        hideCountDown();
        //建立 放Topic圖的adapter
        adapter = new DailySelectExerciseAdapter(activity, topic);
        //給予viewPager "Adapter"
        fragmentContainer.setAdapter(adapter);
        //設定viewPager換頁監聽(使用this時可以使用implements，來建立要複寫的funtion)
        fragmentContainer.addOnPageChangeListener(this);
        //用來建立頁面底下的指標(.....)
        indicator.setIsFinish(isFinish);
        indicator.setViewPager(fragmentContainer);
        //設定ViewPage顯示頁面為"getWitch"
        fragmentContainer.setCurrentItem(((DailyExerciseActivity) activity).getWitch());
        //設定上一步按鈕"顯示"
        previousStepButton.setVisibility(View.VISIBLE);
        //設定下一步按鈕"顯示"
        nextStepButton.setVisibility(View.VISIBLE);
        //如果所在頁面為"第一頁"隱藏上一步按鈕
        if (((DailyExerciseActivity) activity).getWitch() == 0)
            previousStepButton.setVisibility(View.INVISIBLE);
        //如果所在頁面為"最後一頁"隱藏下一步按鈕
        else if (((DailyExerciseActivity) activity).getWitch() == topic.size() - 1)
            nextStepButton.setVisibility(View.INVISIBLE);
    }

    private void setFont() {
    }
    //下一頁監聽
    @OnClick(R.id.next_step_button)
    public void onNextButtonClick() {
        //切換到下一頁(後面的true是讓換頁時有個動畫)
        fragmentContainer.setCurrentItem(fragmentContainer.getCurrentItem() + 1, true);
    }
    //上一頁監聽
    @OnClick(R.id.previous_step_button)
    public void onPreviousButtonClick() {
        //切換到上一頁(後面的true是讓換頁時有個動畫)
        fragmentContainer.setCurrentItem(fragmentContainer.getCurrentItem() - 1, true);
    }

    @OnClick(R.id.start_button)
    public void onStartButtonClick() {
        //呼叫setWitch 放入所在的Topic
        ((DailyExerciseActivity) activity).setWitch(fragmentContainer.getCurrentItem());
        Bundle bundle = new Bundle();
        //把所在的Topic編號，讓入bundle
        bundle.putInt(KEY_INNER_FRAGMENT_INDEX, topic.get(fragmentContainer.getCurrentItem()));
        //動態加載HealthEducationActivity
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.activity_daily_exercise, null);
        //換到開始的Fragment
        FragmentLauncher.change(
                activity,
                view.findViewById(R.id.daily_exercise_container).getId(),
                bundle,
                new DailyExerciseInnerSelectFragment().getClass().getName()
        );
    }

    private boolean isComplete() {
        boolean complete = true;
        for (int i = 0; i < isFinish.length; i++) {
            complete = complete & isFinish[i];
        }
        return complete;
    }

    public void addToBackStack(String stackName) {
        activity.getFragmentManager().beginTransaction().addToBackStack(stackName).commit();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //如果所在頁面為"最後一頁"
        if (position == adapter.getCount() - 1) {
            //隱藏下一步按鈕
            nextStepButton.setVisibility(View.INVISIBLE);
            //顯示上一步按鈕
            previousStepButton.setVisibility(View.VISIBLE);
        //如果所在頁面為"第一頁"
        } else if (position == 0) {
            //顯示下一步按鈕
            nextStepButton.setVisibility(View.VISIBLE);
            //隱藏上一步按鈕
            previousStepButton.setVisibility(View.INVISIBLE);
        } else {
            //顯示下一步按鈕
            nextStepButton.setVisibility(View.VISIBLE);
            //顯示上一步按鈕
            previousStepButton.setVisibility(View.VISIBLE);
        }
    }
    //隱藏倒數元件
    public void hideCountDown() {
        RelativeLayout counterContainer = (RelativeLayout) activity.findViewById(R.id.counter_container);
        counterContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setNotFinishTopic() {
        for (int i = 0; i < isFinish.length; i++) {
            //判斷有沒有尚未做完的Topic
            if (!isFinish[i]) {
                //"有"設定ViewPage為該頁面
                fragmentContainer.setCurrentItem(i);
                break;
            }
        }
    }
}
