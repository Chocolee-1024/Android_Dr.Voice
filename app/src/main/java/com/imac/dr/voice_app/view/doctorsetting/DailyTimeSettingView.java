package com.imac.dr.voice_app.view.doctorsetting;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.module.GroupHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isa on 2017/2/13.
 */

public class DailyTimeSettingView {
    @BindView(R.id.topic1_zero)
    TextView mTopic1Zero;
    @BindView(R.id.topic1_one)
    TextView mTopic1One;
    @BindView(R.id.topic1_three)
    TextView mTopic1Three;
    @BindView(R.id.topic1_five)
    TextView mTopic1Five;
    @BindView(R.id.topic2_zero)
    TextView mTopic2Zero;
    @BindView(R.id.topic2_one)
    TextView mTopic2One;
    @BindView(R.id.topic2_three)
    TextView mTopic2Three;
    @BindView(R.id.topic2_five)
    TextView mTopic2Five;
    @BindView(R.id.topic3_zero)
    TextView mTopic3Zero;
    @BindView(R.id.topic3_one)
    TextView mTopic3One;
    @BindView(R.id.topic3_three)
    TextView mTopic3Three;
    @BindView(R.id.topic3_five)
    TextView mTopic3Five;
    @BindView(R.id.topic4_zero)
    TextView mTopic4Zero;
    @BindView(R.id.topic4_one)
    TextView mTopic4One;
    @BindView(R.id.topic4_three)
    TextView mTopic4Three;
    @BindView(R.id.topic4_five)
    TextView mTopic4Five;
    @BindView(R.id.topic5_zero)
    TextView mTopic5Zero;
    @BindView(R.id.topic5_one)
    TextView mTopic5One;
    @BindView(R.id.topic5_three)
    TextView mTopic5Three;
    @BindView(R.id.topic5_five)
    TextView mTopic5Five;
    @BindView(R.id.topic6_zero)
    TextView mTopic6Zero;
    @BindView(R.id.topic6_one)
    TextView mTopic6One;
    @BindView(R.id.topic6_three)
    TextView mTopic6Three;
    @BindView(R.id.topic6_five)
    TextView mTopic6Five;
    @BindView(R.id.submit)
    Button submit;
    private CallBackEvent mCallBackEvent;
    private InitCallBackEvent mInitCallBackEvent;
    private GroupHelper topicOne;
    private GroupHelper topicTwo;
    private GroupHelper topicThree;
    private GroupHelper topicFour;
    private GroupHelper topicFive;
    private GroupHelper topicSix;
    private int topicOnePosition;
    private int topicTwoPosition;
    private int topicThreePosition;
    private int topicFourPosition;
    private int topicFivePosition;
    private int topicSixPosition;

    public DailyTimeSettingView(Activity activity, View view) {
        ButterKnife.bind(this, view);
        setGroup();
    }

    private void setGroup() {
        topicOne = new GroupHelper();
        topicOne.add(mTopic1Zero);
        topicOne.add(mTopic1One);
        topicOne.add(mTopic1Three);
        topicOne.add(mTopic1Five);
        topicOne.setCallBack(mGroupOneCallback);
        topicTwo = new GroupHelper();
        topicTwo.add(mTopic2Zero);
        topicTwo.add(mTopic2One);
        topicTwo.add(mTopic2Three);
        topicTwo.add(mTopic2Five);
        topicTwo.setCallBack(mGroupTwoCallback);
        topicThree = new GroupHelper();
        topicThree.add(mTopic3Zero);
        topicThree.add(mTopic3One);
        topicThree.add(mTopic3Three);
        topicThree.add(mTopic3Five);
        topicThree.setCallBack(mGroupThreeCallback);
        topicFour = new GroupHelper();
        topicFour.add(mTopic4Zero);
        topicFour.add(mTopic4One);
        topicFour.add(mTopic4Three);
        topicFour.add(mTopic4Five);
        topicFour.setCallBack(mGroupFourCallback);
        topicFive = new GroupHelper();
        topicFive.add(mTopic5Zero);
        topicFive.add(mTopic5One);
        topicFive.add(mTopic5Three);
        topicFive.add(mTopic5Five);
        topicFive.setCallBack(mGroupFiveCallback);
        topicSix = new GroupHelper();
        topicSix.add(mTopic6Zero);
        topicSix.add(mTopic6One);
        topicSix.add(mTopic6Three);
        topicSix.add(mTopic6Five);
        topicSix.setCallBack(mGroupSixCallback);

    }

    private GroupHelper.CallBack mGroupOneCallback = new GroupHelper.CallBack() {
        @Override
        public void onClick(int position, ArrayList<TextView> textViewArrayList) {
            topicOnePosition = position;
        }
    };

    private GroupHelper.CallBack mGroupTwoCallback = new GroupHelper.CallBack() {
        @Override
        public void onClick(int position, ArrayList<TextView> textViewArrayList) {
            topicTwoPosition = position;
        }
    };

    private GroupHelper.CallBack mGroupThreeCallback = new GroupHelper.CallBack() {
        @Override
        public void onClick(int position, ArrayList<TextView> textViewArrayList) {
            topicThreePosition = position;

        }
    };

    private GroupHelper.CallBack mGroupFourCallback = new GroupHelper.CallBack() {
        @Override
        public void onClick(int position, ArrayList<TextView> textViewArrayList) {
            topicFourPosition = position;
        }
    };

    private GroupHelper.CallBack mGroupFiveCallback = new GroupHelper.CallBack() {
        @Override
        public void onClick(int position, ArrayList<TextView> textViewArrayList) {
            topicFivePosition = position;
        }
    };

    private GroupHelper.CallBack mGroupSixCallback = new GroupHelper.CallBack() {
        @Override
        public void onClick(int position, ArrayList<TextView> textViewArrayList) {
            topicSixPosition = position;
        }
    };

    public void setCallBackEvent(CallBackEvent callBackEvent) {
        mCallBackEvent = callBackEvent;
    }

    public void setInitCallBackEvent(InitCallBackEvent initCallBackEvent) {
        mInitCallBackEvent = initCallBackEvent;
        if (null != mInitCallBackEvent)
            mInitCallBackEvent.onInit(topicOne, topicTwo, topicThree, topicFour, topicFive, topicSix);
    }

    @OnClick(R.id.submit)
    public void onSubmitClick() {
        if (null != mCallBackEvent)
            mCallBackEvent.onSubmitClick(topicOnePosition, topicTwoPosition, topicThreePosition, topicFourPosition, topicFivePosition, topicSixPosition);
    }

    public void setTopicOnePosition(int topicOnePosition) {
        this.topicOnePosition = topicOnePosition;
    }

    public void setTopicTwoPosition(int topicTwoPosition) {
        this.topicTwoPosition = topicTwoPosition;
    }

    public void setTopicThreePosition(int topicThreePosition) {
        this.topicThreePosition = topicThreePosition;
    }

    public void setTopicFourPosition(int topicFourPosition) {
        this.topicFourPosition = topicFourPosition;
    }

    public void setTopicFivePosition(int topicFivePosition) {
        this.topicFivePosition = topicFivePosition;
    }

    public void setTopicSixPosition(int topicSixPosition) {
        this.topicSixPosition = topicSixPosition;
    }

    public interface CallBackEvent {

        void onSubmitClick(int one, int two, int three, int four, int five, int six);
    }

    public interface InitCallBackEvent {
        void onInit(GroupHelper groupOne, GroupHelper groupTwo, GroupHelper groupThree
                , GroupHelper groupFour, GroupHelper groupFive, GroupHelper groupSix);
    }
}
