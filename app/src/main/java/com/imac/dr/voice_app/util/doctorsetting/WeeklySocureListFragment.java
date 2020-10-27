package com.imac.dr.voice_app.util.doctorsetting;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.view.doctorsetting.WeeklyScoreListView;

/**
 * Created by isa on 2017/4/24.
 */

public class WeeklySocureListFragment extends Fragment {

    private WeeklyScoreListView mWeeklyScoreListView;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly_socure_list, container, false);
        mWeeklyScoreListView = new WeeklyScoreListView(getActivity(), view);
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
