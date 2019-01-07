package com.imac.dr.voice_app.util.doctorsetting;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.core.FragmentLauncher;
import com.imac.dr.voice_app.view.doctorsetting.DoctorSettingMenuView;

/**
 * Created by isa on 2016/12/6.
 */

public class DoctorSettingMenuFragment extends Fragment {

    private DoctorSettingMenuView mDoctorSettingMenuView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_setting_menu, container, false);
        mDoctorSettingMenuView = new DoctorSettingMenuView(getActivity(), view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mDoctorSettingMenuView.setCallbackEvent(callbackEvent);
    }

    private DoctorSettingMenuView.CallbackEvent callbackEvent = new DoctorSettingMenuView.CallbackEvent() {
        @Override
        public void onDailyClick() {
//            FragmentLauncher.changeToBack(getActivity(), R.id.container, null, DailySettingFragment.class.getName());
            FragmentLauncher.changeToBack(getActivity(), R.id.container, null, DailyTimeSettingFragment.class.getName());
        }

        @Override
        public void onSelfClick() {
            FragmentLauncher.changeToBack(getActivity(), R.id.container, null, SelfSettingFragment.class.getName());
        }

        @Override
        public void onSpeedClick() {
            FragmentLauncher.changeToBack(getActivity(), R.id.container, null, SpeedSettingFragment.class.getName());
        }

        @Override
        public void onDailyTimeClick() {
            FragmentLauncher.changeToBack(getActivity(), R.id.container, null, DailyTimeSettingFragment.class.getName());
        }

        @Override
        public void onWeeklyScoreClick() {
            FragmentLauncher.changeToBack(getActivity(), R.id.container, null, WeeklySocureListFragment.class.getName());
        }

        @Override
        public void onSpeedScoreClick() {
            FragmentLauncher.changeToBack(getActivity(), R.id.container, null, SpeedScoreListFragment.class.getName());
        }
    };
}
