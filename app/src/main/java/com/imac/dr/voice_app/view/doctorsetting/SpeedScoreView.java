package com.imac.dr.voice_app.view.doctorsetting;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.module.database.data.SpeedDataStricture;
import com.imac.dr.voice_app.util.doctorsetting.DoctorSettingActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isa on 2017/4/26.
 */

public class SpeedScoreView {
    @BindView(R.id.start_time_value)
    TextView mStartTimeValue;
    @BindView(R.id.end_time_value)
    TextView mEndTimeValue;
    @BindView(R.id.speed_count_value)
    TextView mSpeedCountValue;
    @BindView(R.id.speed_value)
    TextView mSpeedValue;
    @BindView(R.id.record_value)
    TextView mRecordValue;
    private Activity mActivity;
    private SpeedDataStricture mSpeedDataStricture;

    public SpeedScoreView(Activity activity, View view) {
        mActivity = activity;
        ButterKnife.bind(this, view);
    }

    public void setData(SpeedDataStricture speedDataStricture) {
        ((DoctorSettingActivity) mActivity).getToolBarView().setTitleTextViewText("語速監控成績");
        mSpeedDataStricture = speedDataStricture;
        mStartTimeValue.setText(dateFormat(mSpeedDataStricture.getStartTime()));
        mEndTimeValue.setText(dateFormat(mSpeedDataStricture.getEndTime()));
        mSpeedCountValue.setText(mSpeedDataStricture.getSpeedCount());
        mSpeedValue.setText(mSpeedDataStricture.getSpeed());
        mRecordValue.setText(mSpeedDataStricture.getRecord());
    }

    private String dateFormat(String time) {
        Long millionTime = Long.valueOf(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm", Locale.TAIWAN);
        return simpleDateFormat.format(new Date(millionTime));
    }
}
