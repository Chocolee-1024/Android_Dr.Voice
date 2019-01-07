package com.imac.dr.voice_app.view.doctorsetting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.module.DataAppend;
import com.imac.dr.voice_app.util.doctorsetting.DoctorSettingActivity;
import com.imac.dr.voice_app.util.doctorsetting.WeeklyScoreFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isa on 2017/4/24.
 */

public class WeeklyScoreView {
    @BindView(R.id.topic_one)
    TextView mTopicOne;
    @BindView(R.id.topic_two)
    TextView mTopicTwo;
    @BindView(R.id.topic_three)
    TextView mTopicThree;
    @BindView(R.id.topic_four)
    TextView mTopicFour;
    @BindView(R.id.topic_five)
    TextView mTopicFive;
    @BindView(R.id.topic_six)
    TextView mTopicSix;
    @BindView(R.id.topic_seven)
    TextView mTopicSeven;
    @BindView(R.id.weekly_score)
    TextView mWeeklyScore;
    @BindView(R.id.self_score)
    TextView mSelfScore;
    private Activity mActivity;
    private ArrayList<String> soundTopic;
    private ArrayList<String> soundData;
    private ArrayList<String> selfAssessment;
    private ArrayList<TextView> mViewArrayList;

    public WeeklyScoreView(Activity activity, View view) {
        mActivity = activity;
        ButterKnife.bind(this, view);
        init();
    }

    private void init() {
        ((DoctorSettingActivity) mActivity).getToolBarView().setTitleTextViewText("每週評量成績");
        mViewArrayList = new ArrayList<>(7);
        mViewArrayList.add(mTopicOne);
        mViewArrayList.add(mTopicTwo);
        mViewArrayList.add(mTopicThree);
        mViewArrayList.add(mTopicFour);
        mViewArrayList.add(mTopicFive);
        mViewArrayList.add(mTopicSix);
        mViewArrayList.add(mTopicSeven);
    }

    public void setBundleData(Bundle args) {
        DataAppend dataAppend = new DataAppend();
        soundTopic = dataAppend.formatString(args.getString(WeeklyScoreFragment.BUNDLE_KEY_SOUNDTOPIC));
        soundData = dataAppend.formatString(args.getString(WeeklyScoreFragment.BUNDLE_KEY_SOUNDDATA));
        selfAssessment = dataAppend.formatString(args.getString(WeeklyScoreFragment.BUNDLE_KEY_SELFASSESSMENTDATA));
        setViewData();
    }

    private void setViewData() {
        int soundScore = 0;
        int selfScore = 0;
        for (int i = 0; i < soundTopic.size(); i++) {
            int topicNum = Integer.valueOf(soundTopic.get(i));
            soundScore += Integer.valueOf(soundData.get(i));
            mViewArrayList.get(topicNum).setText(soundData.get(i));
        }
        for (String data :selfAssessment){
            selfScore+=Integer.valueOf(data);
        }
        mWeeklyScore.setText(String.valueOf(soundScore));
        mSelfScore.setText(String.valueOf(selfScore));
    }
}
