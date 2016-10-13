package com.imac.voice_app.util.weeklyassessment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.voice_app.R;
import com.imac.voice_app.view.weeklyassessment.WeeklyAssessmentContainerView;

import butterknife.ButterKnife;

/**
 * Created by isa on 2016/10/4.
 */
public class WeeklyAssessmentContainerFragment extends Fragment {
    private WeeklyAssessmentContainerView weeklyAssessmentContainerView;
    private String status;
    private String soundTopic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getIntent();
        View view = inflater.inflate(R.layout.fragment_weekly_assessment_select_page_container, container, false);
        weeklyAssessmentContainerView = new WeeklyAssessmentContainerView(getActivity(), view, status);
        weeklyAssessmentContainerView.setSoundTopic(soundTopic);
        ButterKnife.bind(this, view);
        return view;
    }

    private void getIntent() {
        status = getArguments().getString(WeeklyAssessmentActivity.KEY_STATUS);
        if (status.equals(WeeklyAssessmentActivity.SELF_ASSESSMENT)) {
            soundTopic = getArguments().getString(WeeklyAssessmentActivity.KEY_SOUND_TOPIC);
        }
    }
}
