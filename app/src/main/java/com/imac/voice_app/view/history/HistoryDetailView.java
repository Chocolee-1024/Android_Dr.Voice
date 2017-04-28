package com.imac.voice_app.view.history;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.module.database.SqliteManager;
import com.imac.voice_app.module.database.data.WeeklyDataStructure;
import com.imac.voice_app.util.history.HistoryActivity;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * Created by isa on 2016/10/24.
 */
public class HistoryDetailView implements ViewPager.OnPageChangeListener {
    @BindView(R.id.history_detail_date_spinner)
    Spinner historyDetailDateSpinner;
    //    @BindView(R.id.history_detail_topic_spinner)
//    Spinner historyDetailTopicSpinner;
    @BindView(R.id.history_detail_good_icon)
    ImageView historyDetailGoodIcon;
    @BindView(R.id.history_detail_good_text)
    TextView historyDetailGoodText;
    @BindView(R.id.history_detail_bad_text)
    TextView historyDetailBadText;
    @BindView(R.id.history_detail_bad_icon)
    ImageView historyDetailBadIcon;
    @BindView(R.id.history_detail_chart)
    ViewPager historyDetailChart;
    @BindView(R.id.history_detail_line)
    View historyDetailLine;
    @BindView(R.id.history_detail_category_sound)
    TextView historyDetailCategorySound;
    @BindView(R.id.history_detail_category_switch)
    Switch historyDetailCategorySwitch;
    @BindView(R.id.history_detail_category_exercise)
    TextView historyDetailCategoryExercise;
    @BindView(R.id.history_detail_category_container)
    PercentRelativeLayout historyDetailCategoryContainer;
    @BindColor(R.color.history_text)
    int selectColor;
    @BindColor(R.color.black)
    int noSelectColor;
    private Activity activity = null;
    private WeeklyPagerAdapter weeklyPagerAdapter = null;
    private VoicePagerAdapter voicePagerAdapter = null;
    //    private TopicSpinnerAdapter topicTopicSpinnerAdapter = null;
    private DateSpinnerAdapter dateTopicSpinnerAdapter = null;
    private ArrayList<String> weeklyTpicList = null;
    private String[] month = null;
    private WeeklyDataStructure[] mWeeklyDataStructures = null;
    private SqliteManager manger = null;
    private int dataItemIndex = 0;

    public HistoryDetailView(Activity activity, View view) {
        this.activity = activity;
        weeklyTpicList = ((HistoryActivity) activity).getWeeklyTopicList();
        ButterKnife.bind(this, view);
        manger = SqliteManager.getInstence(activity);
        month = manger.selectWeeklyTableMonth();
        mWeeklyDataStructures = manger.getWeeklyTableALlSqlData();
        weeklyPagerAdapter = new WeeklyPagerAdapter(activity, month, mWeeklyDataStructures);
        voicePagerAdapter = new VoicePagerAdapter(activity, month, mWeeklyDataStructures);
//        topicTopicSpinnerAdapter = new TopicSpinnerAdapter(activity, weeklyTpicList);
//        historyDetailTopicSpinner.setAdapter(topicTopicSpinnerAdapter);
        dateTopicSpinnerAdapter = new DateSpinnerAdapter(activity, month);
        historyDetailDateSpinner.setAdapter(dateTopicSpinnerAdapter);
//        historyDetailTopicSpinner.getBackground().setColorFilter(selectColor, PorterDuff.Mode.SRC_ATOP);
        historyDetailDateSpinner.getBackground().setColorFilter(selectColor, PorterDuff.Mode.SRC_ATOP);

        setListener();
    }

    private void setListener() {
        historyDetailChart.addOnPageChangeListener(this);
    }

    public void setSwitch(boolean isSwitch) {
        historyDetailCategorySwitch.setChecked(isSwitch);
        if (isSwitch) {
            historyDetailCategorySound.setTextColor(noSelectColor);
            historyDetailCategoryExercise.setTextColor(selectColor);
            historyDetailChart.setAdapter(weeklyPagerAdapter);
        } else {
            historyDetailCategorySound.setTextColor(selectColor);
            historyDetailCategoryExercise.setTextColor(noSelectColor);
            historyDetailChart.setAdapter(voicePagerAdapter);
        }
    }

    @OnCheckedChanged(R.id.history_detail_category_switch)
    public void SwitchCheck(CompoundButton compoundButton, boolean b) {
        historyDetailChart.invalidate();
//        historyDetailTopicSpinner.setVisibility(b ? View.GONE : View.VISIBLE);
        if (b) {
            historyDetailCategorySound.setTextColor(noSelectColor);
            historyDetailCategoryExercise.setTextColor(selectColor);
            historyDetailChart.setAdapter(weeklyPagerAdapter);
        } else {
            historyDetailCategorySound.setTextColor(selectColor);
            historyDetailCategoryExercise.setTextColor(noSelectColor);
            historyDetailChart.setAdapter(voicePagerAdapter);
        }
        historyDetailChart.setCurrentItem(dataItemIndex);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        historyDetailDateSpinner.setSelection(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @OnItemSelected(R.id.history_detail_date_spinner)
    public void dateSpinnerListener(AdapterView<?> adapterView, View view, int i, long l) {
        historyDetailChart.setCurrentItem(i);
        dataItemIndex = i;
    }

    @OnClick(R.id.history_detail_category_sound)
    public void onHistoryDetailCategorySoundClick() {
        historyDetailCategorySwitch.setChecked(false);
    }

    @OnClick(R.id.history_detail_category_exercise)
    public void onhistoryDetailCategoryExerciseClick() {
        historyDetailCategorySwitch.setChecked(true);
    }
//    @OnItemSelected(R.id.history_detail_topic_spinner)
//    public void topicSpinnerListener(AdapterView<?> adapterView, View view, int i, long l) {
//        historyDetailChart.setCurrentItem(dataItemIndex);
//        voicePagerAdapter.setSelectTopic(i);
//        voicePagerAdapter.notifyDataSetChanged();
//    }

//    @OnTouch(R.id.history_detail_chart)
//    public boolean viewPagerOnTouch() {
//        return true;
//    }
}
