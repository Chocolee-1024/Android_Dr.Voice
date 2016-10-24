package com.imac.voice_app.util.weeklyassessment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.voice_app.R;
import com.imac.voice_app.core.PreferencesHelper;
import com.imac.voice_app.module.FileWriter;
import com.imac.voice_app.module.SharePreferencesManager;
import com.imac.voice_app.module.net.FileUploader;
import com.imac.voice_app.util.login.LoginActivity;
import com.imac.voice_app.view.weeklyassessment.DataWriteEvent;
import com.imac.voice_app.view.weeklyassessment.WeeklyAssessmentContainerView;

import java.io.File;
import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by isa on 2016/10/4.
 */
public class WeeklyAssessmentContainerFragment extends Fragment implements DataWriteEvent, FileWriter.WriterCallBack {
    private WeeklyAssessmentContainerView weeklyAssessmentContainerView;
    private String status;
    private String soundTopic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getIntent();
        View view = inflater.inflate(R.layout.fragment_weekly_assessment_select_page_container, container, false);
        weeklyAssessmentContainerView = new WeeklyAssessmentContainerView(getActivity(), view, status);
        weeklyAssessmentContainerView.setDataWriteEvent(this);
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

    @Override
    public void onDataWrite(ArrayList<String> soundPoint, ArrayList<String> soundTopic, ArrayList<String> assessmentPoint) {
        FileWriter writer = new FileWriter(getActivity());
        writer.setWriterCallBack(this);
        writer.write(soundPoint, soundTopic, assessmentPoint);

    }

    @Override
    public void onWriteSuccessful(File file) {
        SharePreferencesManager sharePreferencesManager = SharePreferencesManager.getInstance(getActivity());
        String loginAccount = (String) sharePreferencesManager.get(LoginActivity.KEY_LOGIN_ACCOUNT, PreferencesHelper.Type.STRING);
        String name = (String) sharePreferencesManager.get(LoginActivity.KEY_LOGIN_NAME, PreferencesHelper.Type.STRING);
        FileUploader uploader = new FileUploader(getActivity(), name, loginAccount);
        uploader.connect(file);
    }

    @Override
    public void onWriteFail() {

    }
}
