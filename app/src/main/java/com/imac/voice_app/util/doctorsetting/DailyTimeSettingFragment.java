package com.imac.voice_app.util.doctorsetting;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.voice_app.R;
import com.imac.voice_app.module.GroupHelper;
import com.imac.voice_app.module.Preferences;
import com.imac.voice_app.view.doctorsetting.DailyTimeSettingView;

/**
 * Created by isa on 2017/2/13.
 */

public class DailyTimeSettingFragment extends Fragment {

    private DailyTimeSettingView mDailyTimeSettingView;
    private Preferences mPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_doctory_daily_time_setting, container, false);
        mDailyTimeSettingView = new DailyTimeSettingView(getActivity(), view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mPreferences = new Preferences(getActivity());
        mDailyTimeSettingView.setCallBackEvent(mCallBackEvent);
        mDailyTimeSettingView.setInitCallBackEvent(mInitCallBackEvent);
    }

    private DailyTimeSettingView.CallBackEvent mCallBackEvent = new DailyTimeSettingView.CallBackEvent() {
        @Override
        public void onTopicOneClick(int position) {
            mPreferences.saveTopicOnePosition(position);
        }

        @Override
        public void onTopicTwoClick(int position) {
            mPreferences.saveTopicTwoPosition(position);
        }

        @Override
        public void onTopicThreeClick(int position) {
            mPreferences.saveTopicThreePosition(position);
        }

        @Override
        public void onTopicFourClick(int position) {
            mPreferences.saveTopicFourPosition(position);
        }

        @Override
        public void onTopicFiveClick(int position) {
            mPreferences.saveTopicFivePosition(position);
        }

        @Override
        public void onTopicSixClick(int position) {
            mPreferences.saveTopicSixPosition(position);
        }
    };
    private DailyTimeSettingView.InitCallBackEvent mInitCallBackEvent = new DailyTimeSettingView.InitCallBackEvent() {
        @Override
        public void onInit(GroupHelper groupOne, GroupHelper groupTwo, GroupHelper groupThree, GroupHelper groupFour, GroupHelper groupFive, GroupHelper groupSix) {
            groupOne.setInitClickStatus(mPreferences.getTopicOnePosition());
            groupTwo.setInitClickStatus(mPreferences.getTopicTwoPosition());
            groupThree.setInitClickStatus(mPreferences.getTopicThreePosition());
            groupFour.setInitClickStatus(mPreferences.getTopicFourPosition());
            groupFive.setInitClickStatus(mPreferences.getTopicFivePosition());
            groupSix.setInitClickStatus(mPreferences.getTopicSixPosition());
        }
    };

}
