package com.imac.voice_app.view.doctorsetting;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.imac.voice_app.R;
import com.imac.voice_app.core.FragmentLauncher;
import com.imac.voice_app.module.adapter.SpeedListAdapter;
import com.imac.voice_app.module.database.SqliteManager;
import com.imac.voice_app.module.database.data.SpeedDataStricture;
import com.imac.voice_app.util.doctorsetting.DoctorSettingActivity;
import com.imac.voice_app.util.doctorsetting.SpeedScoreFragment;
import com.imac.voice_app.util.doctorsetting.SpeedScoreListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isa on 2017/4/26.
 */

public class SpeedScoreListView implements SpeedListAdapter.OnRecyclerViewEvent {
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    private Activity mActivity;
    private SqliteManager mSqliteManager;

    public SpeedScoreListView(Activity activity, View view) {
        mActivity = activity;
        ButterKnife.bind(this, view);
        init();
    }

    private void init() {
        ((DoctorSettingActivity) mActivity).getToolBarView().setTitleTextViewText("語速監控");
        mSqliteManager = SqliteManager.getInstence(mActivity);
        setRecyclerViewAdapter(mRecyclerView);
    }

    private void setRecyclerViewAdapter(RecyclerView recyclerViewAdapter) {
        SpeedListAdapter speedListAdapter = new SpeedListAdapter(mActivity);
        speedListAdapter.setData(mSqliteManager.getSpeedTableALlSqlData());
        speedListAdapter.setOnRecyclerViewEvent(this);
        recyclerViewAdapter.setAdapter(speedListAdapter);
        recyclerViewAdapter.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    @Override
    public void onItemClick(SpeedDataStricture speedData) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SpeedScoreListFragment.BUNDLE_KEY_SPEEDDATA, speedData);
        FragmentLauncher.changeToBack(mActivity, R.id.container, bundle, SpeedScoreFragment.class.getName());
    }
}
