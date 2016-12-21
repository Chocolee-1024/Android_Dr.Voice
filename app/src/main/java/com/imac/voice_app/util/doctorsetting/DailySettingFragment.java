package com.imac.voice_app.util.doctorsetting;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.voice_app.R;
import com.imac.voice_app.module.DataAppend;
import com.imac.voice_app.module.Preferences;
import com.imac.voice_app.view.doctorsetting.DailySettingView;

import java.util.ArrayList;

/**
 * Created by isa on 2016/12/6.
 */

public class DailySettingFragment extends Fragment {

    private DailySettingView mDailySettingView;
    private DataAppend mDataAppend;
    private ArrayList<String> saveStatusList;
    private Preferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_daily_settting, container, false);
        preferences = new Preferences(getActivity());
        mDailySettingView = new DailySettingView(getActivity(), view);
        mDataAppend = new DataAppend();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mDailySettingView.setCallbackEvent(callbackEvent);
    }

    private DailySettingView.CallbackEvent callbackEvent = new DailySettingView.CallbackEvent() {
        @Override
        public void onCallbackEvent(View view, int position) {

        }

        @Override
        public void onClickSave(Boolean[] status) {
            saveStatusList = new ArrayList<>(6);
            for (Boolean index : status) saveStatusList.add(String.valueOf(index));
            preferences.saveDailyDoctorSetting(mDataAppend.append(saveStatusList));
            getActivity().onBackPressed();
        }
    };
}
