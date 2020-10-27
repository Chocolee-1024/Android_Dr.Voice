package com.imac.dr.voice_app.util.doctorsetting;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.module.GroupHelper;
import com.imac.dr.voice_app.module.Preferences;
import com.imac.dr.voice_app.view.doctorsetting.DailyTimeSettingView;

/**
 * Created by isa on 2017/2/13.
 */

public class DailyTimeSettingFragment extends Fragment {

    private DailyTimeSettingView mDailyTimeSettingView;
    private Preferences mPreferences;


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
        mDailyTimeSettingView.setTopicOnePosition(mPreferences.getTopicOnePosition());
        mDailyTimeSettingView.setTopicTwoPosition(mPreferences.getTopicTwoPosition());
        mDailyTimeSettingView.setTopicThreePosition(mPreferences.getTopicThreePosition());
        mDailyTimeSettingView.setTopicFourPosition(mPreferences.getTopicFourPosition());
        mDailyTimeSettingView.setTopicFivePosition(mPreferences.getTopicFivePosition());
        mDailyTimeSettingView.setTopicSixPosition(mPreferences.getTopicSixPosition());
        mDailyTimeSettingView.setCallBackEvent(mCallBackEvent);
        mDailyTimeSettingView.setInitCallBackEvent(mInitCallBackEvent);
    }

    private DailyTimeSettingView.CallBackEvent mCallBackEvent = new DailyTimeSettingView.CallBackEvent() {
        @Override
        public void onSubmitClick(int one, int two, int three, int four, int five, int six) {
            mPreferences.saveTopicOnePosition(one);
            mPreferences.saveTopicTwoPosition(two);
            mPreferences.saveTopicThreePosition(three);
            mPreferences.saveTopicFourPosition(four);
            mPreferences.saveTopicFivePosition(five);
            mPreferences.saveTopicSixPosition(six);
            getActivity().onBackPressed();

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
