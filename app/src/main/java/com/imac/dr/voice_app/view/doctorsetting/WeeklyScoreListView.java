package com.imac.dr.voice_app.view.doctorsetting;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.core.FragmentLauncher;
import com.imac.dr.voice_app.module.adapter.WeeklyListAdapter;
import com.imac.dr.voice_app.module.database.SqliteManager;
import com.imac.dr.voice_app.util.doctorsetting.DoctorSettingActivity;
import com.imac.dr.voice_app.util.doctorsetting.WeeklyScoreFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isa on 2017/4/24.
 */

public class WeeklyScoreListView implements WeeklyListAdapter.OnRecyclerViewTItemClick {
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    private SqliteManager mSqliteManager;
    private Activity mActivity;

    public WeeklyScoreListView(Activity activity, View view) {
        mActivity = activity;
        ButterKnife.bind(this, view);
        mSqliteManager = SqliteManager.getInstence(mActivity);
        init();
    }

    private void init() {
        ((DoctorSettingActivity) mActivity).getToolBarView().setTitleTextViewText("每週評量");
        setRecyclerView(mRecyclerView);
    }

    private void setRecyclerView(RecyclerView recyclerView) {
        WeeklyListAdapter weeklyListAdapter = new WeeklyListAdapter(mActivity);
        weeklyListAdapter.setData(mSqliteManager.getWeeklyTableALlSqlData());
        weeklyListAdapter.setOnRecyclerViewTItemClick(this);
        recyclerView.setAdapter(weeklyListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    @Override
    public void onItemClick(String soundData, String selfAssessmentData, String Date,String soundTopic) {
        Bundle bundle =new Bundle();
        bundle.putString(WeeklyScoreFragment.BUNDLE_KEY_SOUNDTOPIC,soundTopic);
        bundle.putString(WeeklyScoreFragment.BUNDLE_KEY_SOUNDDATA,soundData);
        bundle.putString(WeeklyScoreFragment.BUNDLE_KEY_SELFASSESSMENTDATA,selfAssessmentData);
        FragmentLauncher.changeToBack(mActivity, R.id.container, bundle, WeeklyScoreFragment.class.getName());
    }
}
