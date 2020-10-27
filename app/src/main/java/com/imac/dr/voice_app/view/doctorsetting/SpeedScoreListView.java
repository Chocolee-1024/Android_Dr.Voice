package com.imac.dr.voice_app.view.doctorsetting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.core.FragmentLauncher;
import com.imac.dr.voice_app.module.adapter.SpeedListAdapter;
import com.imac.dr.voice_app.module.database.SqliteManager;
import com.imac.dr.voice_app.module.database.data.SpeedDataStricture;
import com.imac.dr.voice_app.util.doctorsetting.DoctorSettingActivity;
import com.imac.dr.voice_app.util.doctorsetting.SpeedScoreFragment;
import com.imac.dr.voice_app.util.doctorsetting.SpeedScoreListFragment;

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
        recyclerViewAdapter.setLayoutManager(new LinearLayoutManager(this.mActivity));
    }

    @Override
    public void onItemClick(SpeedDataStricture speedData) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SpeedScoreListFragment.BUNDLE_KEY_SPEEDDATA, speedData);
        FragmentLauncher.changeToBack(mActivity, R.id.container, bundle, SpeedScoreFragment.class.getName());
    }
}
