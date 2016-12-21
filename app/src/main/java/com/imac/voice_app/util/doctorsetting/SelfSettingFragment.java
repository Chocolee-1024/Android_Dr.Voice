package com.imac.voice_app.util.doctorsetting;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.voice_app.R;
import com.imac.voice_app.module.DataAppend;
import com.imac.voice_app.module.Preferences;
import com.imac.voice_app.view.doctorsetting.SelfSettingView;

import java.util.ArrayList;


/**
 * Created by isa on 2016/12/6.
 */

public class SelfSettingFragment extends Fragment {

    private SelfSettingView mSelfSettingView;
    private ArrayList<String> saveStatusList;
    private Preferences preferences;
    private DataAppend mDataAppend;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_self_setting, container, false);
        mSelfSettingView = new SelfSettingView(getActivity(), view);
        preferences=new Preferences(getActivity());
        mDataAppend =new DataAppend();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init(){
        mSelfSettingView.setCallbackEvent(callbackEvent);
    }

    private SelfSettingView.CallbackEvent callbackEvent =new SelfSettingView.CallbackEvent() {
        @Override
        public void onCallbackEvent(View view, int position) {
        }

        @Override
        public void onClickSave(Boolean[] status) {
            saveStatusList = new ArrayList<>(6);
            for (Boolean index : status) saveStatusList.add(String.valueOf(index));
            preferences.saveWeeklyDoctorSetting(mDataAppend.append(saveStatusList));
            getActivity().onBackPressed();
        }
    };
}
