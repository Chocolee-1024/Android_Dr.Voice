package com.imac.voice_app.util.weeklyassessment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.imac.voice_app.R;
import com.imac.voice_app.module.FileWriter;
import com.imac.voice_app.module.Preferences;
import com.imac.voice_app.module.net.DriveFile;
import com.imac.voice_app.module.net.FileUploader;
import com.imac.voice_app.view.weeklyassessment.DataWriteEvent;
import com.imac.voice_app.view.weeklyassessment.WeeklyAssessmentContainerView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by isa on 2016/10/4.
 */
public class WeeklyAssessmentContainerFragment extends Fragment implements DataWriteEvent, FileWriter.WriterCallBack {
    private WeeklyAssessmentContainerView weeklyAssessmentContainerView;
    private String status;
    private String soundTopic;
    private Activity activity;
private   Preferences preferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getIntent();
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_weekly_assessment_select_page_container, container, false);
        weeklyAssessmentContainerView = new WeeklyAssessmentContainerView(activity, view, status);
        weeklyAssessmentContainerView.setDataWriteEvent(this);
        weeklyAssessmentContainerView.setSoundTopic(soundTopic);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
         preferences = new Preferences(activity);

    }

    private void getIntent() {
        status = getArguments().getString(WeeklyAssessmentActivity.KEY_STATUS);
        if (status.equals(WeeklyAssessmentActivity.SELF_ASSESSMENT)) {
            soundTopic = getArguments().getString(WeeklyAssessmentActivity.KEY_SOUND_TOPIC);
        }
    }

    @Override
    public void onDataWrite(ArrayList<String> soundPoint, ArrayList<String> soundTopic, ArrayList<String> assessmentPoint) {
        DriveFile driveFile = new DriveFile(activity, callbackEvent(soundPoint, soundTopic, assessmentPoint), FileUploader.FILE_WEEKLY_SOUND,preferences.getName());
        driveFile.execute();
    }

    @Override
    public void onWriteSuccessful(File file) {
        String loginAccount = preferences.getAccounnt();
        String name = preferences.getName();
        FileUploader uploader = new FileUploader(activity, name, loginAccount, FileUploader.FILE_WEEKLY_SOUND);
        uploader.connect(file);
    }

    @Override
    public void onWriteFail() {

    }

    private DriveFile.CallbackEvent callbackEvent(final ArrayList<String> soundPoint, final ArrayList<String> soundTopic, final ArrayList<String> assessmentPoint) {
        return new DriveFile.CallbackEvent() {
            @Override
            public void onCallback() {
                FileWriter writer = new FileWriter(activity);
                writer.setWriterCallBack(WeeklyAssessmentContainerFragment.this);
                writer.write(soundPoint, soundTopic, assessmentPoint,preferences.getName());
            }

            @Override
            public void onFailCallback() {
                Toast.makeText(activity, "存取失敗，請檢查網路", Toast.LENGTH_LONG).show();
            }
        };
    }
}
