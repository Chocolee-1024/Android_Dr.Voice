package com.imac.voice_app.view.weeklyassessment;

import android.app.Activity;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.util.weeklyassessment.WeeklyAssessmentActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isa on 2016/10/4.
 */
public class WeeklyAssessmentViewPagerAdapter extends PagerAdapter implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.weekly_assessment_topic_number)
    TextView weeklyAssessmentTopicNumber;
    @BindView(R.id.weekly_assessment_date)
    TextView weeklyAssessmentDate;
    @BindView(R.id.weekly_assessment_topic_content)
    TextView weeklyAssessmentTopicContent;
    @BindView(R.id.weekly_assessment_container)
    PercentRelativeLayout weeklyAssessmentContainer;
    @BindView(R.id.option_one)
    RadioButton optionOne;
    @BindView(R.id.option_two)
    RadioButton optionTwo;
    @BindView(R.id.option_three)
    RadioButton optionThree;
    @BindView(R.id.option_four)
    RadioButton optionFour;
    @BindView(R.id.option_five)
    RadioButton optionFive;
    @BindView(R.id.bottom_line)
    View bottomLine;
    @BindView(R.id.weekly_assessment_select_container)
    RadioGroup weeklyAssessmentSelectContainer;
    private Activity activity;
    private String status;
    private ArrayList<String> topic;
    private int position;

    public WeeklyAssessmentViewPagerAdapter(Activity activity, String status) {
        this.activity = activity;
        this.status = status;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return topic.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View view = layoutInflater.inflate(R.layout.fragment_weekly_assessment_self_assessment, null);
        ButterKnife.bind(this, view);
        ArrayList<String> weeklyTopic = ((WeeklyAssessmentActivity) activity).getWeeklyTopic();

        if (status.equals(WeeklyAssessmentActivity.SOUND_RECORDING)) {
            optionFive.setVisibility(View.GONE);
            bottomLine.setVisibility(View.GONE);
        } else if (status.equals(WeeklyAssessmentActivity.SELF_ASSESSMENT)) {
            optionFive.setVisibility(View.VISIBLE);
            bottomLine.setVisibility(View.VISIBLE);
        } else {
            throw new RuntimeException("Status must SOUND_RECORDING or SELF_ASSESSMENT");
        }
        String[] optionArray;
        String[] contentArray;
        String[] topicNumberArray = activity.getResources().getStringArray(R.array.weekly_assessment_topic_number);
        if (status.equals(WeeklyAssessmentActivity.SOUND_RECORDING)) {
            int weeklySoundTopicIndex = Integer.valueOf(weeklyTopic.get(position));
            optionArray = activity.getResources().getStringArray(getSoundOptionId(weeklySoundTopicIndex));
            contentArray = activity.getResources().getStringArray(R.array.weekly_assessment_sound_topic);
            weeklyAssessmentTopicContent.setText(contentArray[weeklySoundTopicIndex]);

        } else if (status.equals(WeeklyAssessmentActivity.SELF_ASSESSMENT)) {
            optionArray = activity.getResources().getStringArray(R.array.weekly_assessment_self_assessment_topic_option);
            optionFive.setText(optionArray[4]);
            contentArray = activity.getResources().getStringArray(R.array.weekly_assessment_self_assessment_topic);
            weeklyAssessmentTopicContent.setText(contentArray[position]);
        } else {
            throw new RuntimeException("Status must SOUND_RECORDING or SELF_ASSESSMENT");
        }
        weeklyAssessmentTopicNumber.setText(topicNumberArray[position]);
        weeklyAssessmentDate.setText(formatDateText());
        optionOne.setText(optionArray[0]);
        optionTwo.setText(optionArray[1]);
        optionThree.setText(optionArray[2]);
        optionFour.setText(optionArray[3]);
        setRadioButtonStatus(position);
        weeklyAssessmentSelectContainer.setOnCheckedChangeListener(this);
        container.addView(view);
        return view;
    }

    private int getSoundOptionId(int position) {
        int arrayId;
        switch (position) {
            case 0:
                arrayId = R.array.weekly_assessment_sound_topic_option_1;
                break;
            case 1:
                arrayId = R.array.weekly_assessment_sound_topic_option_2;
                break;
            case 2:
                arrayId = R.array.weekly_assessment_sound_topic_option_3;
                break;
            case 3:
                arrayId = R.array.weekly_assessment_sound_topic_option_4;
                break;
            case 4:
                arrayId = R.array.weekly_assessment_sound_topic_option_5;
                break;
            case 5:
                arrayId = R.array.weekly_assessment_sound_topic_option_6;
                break;
            case 6:
                arrayId = R.array.weekly_assessment_sound_topic_option_7;
                break;
            default:
                arrayId = R.array.weekly_assessment_sound_topic_option_1;
                break;
        }
        return arrayId;
    }

    private String formatDateText() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(date);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (status.equals(WeeklyAssessmentActivity.SOUND_RECORDING)) {
            if (i == R.id.option_one) {
                topic.set(position, "0");
            } else if (i == R.id.option_two) {
                topic.set(position, "1");
            } else if (i == R.id.option_three) {
                topic.set(position, "2");
            } else if (i == R.id.option_four) {
                topic.set(position, "3");
            }
        } else {
            if (i == R.id.option_one) {
                topic.set(position, "0");
            } else if (i == R.id.option_two) {
                topic.set(position, "1");
            } else if (i == R.id.option_three) {
                topic.set(position, "2");
            } else if (i == R.id.option_four) {
                topic.set(position, "3");
            } else if (i == R.id.option_five) {
                topic.set(position, "4");
            }
        }
    }

    private void setRadioButtonStatus(int position) {
        int topicInt = Integer.valueOf(topic.get(position));
        if (status.equals(WeeklyAssessmentActivity.SOUND_RECORDING))
            switch (topicInt) {
                case 0:
                    optionOne.setChecked(true);
                    break;
                case 1:
                    optionTwo.setChecked(true);
                    break;
                case 2:
                    optionThree.setChecked(true);
                    break;
                case 3:
                    optionFour.setChecked(true);
                    break;
            }
        else
            switch (topicInt) {
                case 0:
                    optionOne.setChecked(true);
                    break;
                case 1:
                    optionTwo.setChecked(true);
                    break;
                case 2:
                    optionThree.setChecked(true);
                    break;
                case 3:
                    optionFour.setChecked(true);
                    break;
                case 4:
                    optionFive.setChecked(true);
                    break;
            }

    }

    public void setRadioButtonArray(ArrayList<String> topic) {
        this.topic = topic;
    }

    public ArrayList<String> getRadioButtonArray() {
        return topic;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
