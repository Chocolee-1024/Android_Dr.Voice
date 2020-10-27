package com.imac.dr.voice_app.util.weeklyassessment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.view.weeklyassessment.WeeklyAssessmentStartView;

/**
 * Created by isa on 2016/10/3.
 */
public class WeeklyAssessmentStartFragment extends Fragment {
    private WeeklyAssessmentStartView weeklyAssessmentStartView;
    private String status;
    private String soundTopic;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getIntent();
        View view = inflater.inflate(R.layout.fragment_weekly_assessment_start_page, container, false);
        weeklyAssessmentStartView = new WeeklyAssessmentStartView(getActivity(), view, status);
        weeklyAssessmentStartView.setSoundTopic(soundTopic);
        return view;
    }

    private void getIntent() {
        status = getArguments().getString(WeeklyAssessmentActivity.KEY_STATUS);
        if (status.equals(WeeklyAssessmentActivity.SELF_ASSESSMENT)){
             soundTopic = getArguments().getString(WeeklyAssessmentActivity.KEY_SOUND_TOPIC);
        }
    }
}
