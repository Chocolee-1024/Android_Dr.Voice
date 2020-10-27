package com.imac.dr.voice_app.util.doctorsetting;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.view.doctorsetting.WeeklyScoreView;

/**
 * Created by isa on 2017/4/24.
 */

public class WeeklyScoreFragment extends Fragment {
    public static final String BUNDLE_KEY_SOUNDTOPIC = "bundle_key_soundtopic";
    public static final String BUNDLE_KEY_SOUNDDATA = "bundle_key_sounddata";
    public static final String BUNDLE_KEY_SELFASSESSMENTDATA = "bundle_key_selfAssessmentData";

    private WeeklyScoreView mWeeklyScoreView;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly_socure, container, false);
        mWeeklyScoreView = new WeeklyScoreView(getActivity(), view);
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mWeeklyScoreView.setBundleData(getArguments());
    }

}
