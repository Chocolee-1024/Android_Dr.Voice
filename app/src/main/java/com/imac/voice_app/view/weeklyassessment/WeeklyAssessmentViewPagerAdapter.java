package com.imac.voice_app.view.weeklyassessment;

import android.app.Activity;
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

/**
 * Created by isa on 2016/10/4.
 */
public class WeeklyAssessmentViewPagerAdapter extends PagerAdapter implements RadioGroup.OnCheckedChangeListener {
    private Activity activity;
    private ArrayList<View> views;
    private String status;
    private String optionPoint = "0";

    public WeeklyAssessmentViewPagerAdapter(Activity activity, String status) {
        this.activity = activity;
        this.status = status;
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        views = new ArrayList<>();
        if (status.equals(WeeklyAssessmentActivity.SOUND_RECORDING)) {
            for (int i = 0; i < 7; i++) {
                View view = layoutInflater.inflate(R.layout.fragment_weekly_assessment_self_assessment, null);
                view.findViewById(R.id.option_five).setVisibility(View.GONE);
                view.findViewById(R.id.bottom_line).setVisibility(View.GONE);
                views.add(view);
            }
        } else if (status.equals(WeeklyAssessmentActivity.SELF_ASSESSMENT)) {
            for (int i = 0; i < 10; i++) {
                View view = layoutInflater.inflate(R.layout.fragment_weekly_assessment_self_assessment, null);
                views.add(view);
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView topicNumText = (TextView) views.get(position).findViewById(R.id.weekly_assessment_topic_number);
        TextView dateText = (TextView) views.get(position).findViewById(R.id.weekly_assessment_date);
        TextView contentText = (TextView) views.get(position).findViewById(R.id.weekly_assessment_topic_content);
        RadioGroup optionGroup = (RadioGroup) views.get(position).findViewById(R.id.weekly_assessment_select_container);
        RadioButton option1 = (RadioButton) views.get(position).findViewById(R.id.option_one);
        RadioButton option2 = (RadioButton) views.get(position).findViewById(R.id.option_two);
        RadioButton option3 = (RadioButton) views.get(position).findViewById(R.id.option_three);
        RadioButton option4 = (RadioButton) views.get(position).findViewById(R.id.option_four);
        RadioButton option5 = (RadioButton) views.get(position).findViewById(R.id.option_five);
        optionGroup.setOnCheckedChangeListener(this);


        String[] optionArray;
        String[] contentArray;
        String[] topicNumberArray = activity.getResources().getStringArray(R.array.weekly_assessment_topic_number);
        if (status.equals(WeeklyAssessmentActivity.SOUND_RECORDING)) {
            optionArray = activity.getResources().getStringArray(getSoundOptionId(position));
            contentArray = activity.getResources().getStringArray(R.array.weekly_assessment_sound_topic);
        } else if (status.equals(WeeklyAssessmentActivity.SELF_ASSESSMENT)) {
            optionArray = activity.getResources().getStringArray(R.array.weekly_assessment_self_assessment_topic_option);
            option5.setText(optionArray[4]);
            contentArray = activity.getResources().getStringArray(R.array.weekly_assessment_self_assessment_topic);
        } else {
            throw new RuntimeException("Status must SOUND_RECORDING or SELF_ASSESSMENT");
        }
        topicNumText.setText(topicNumberArray[position]);
        dateText.setText(formatDateText());
        contentText.setText(contentArray[position]);
        option1.setText(optionArray[0]);
        option2.setText(optionArray[1]);
        option3.setText(optionArray[2]);
        option4.setText(optionArray[3]);

        container.addView(views.get(position));
        return views.get(position);
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
        if (i == R.id.option_one) {
            optionPoint = "0";
        } else if (i == R.id.option_two) {
            optionPoint = "1";
        } else if (i == R.id.option_three) {
            optionPoint = "2";
        } else if (i == R.id.option_four) {
            optionPoint = "3";
        } else if (i == R.id.option_five) {
            optionPoint = "4";
        }
    }

    public String getOptionPoint() {
        return optionPoint;
    }

    public void setOptionPoint(String input) {
        this.optionPoint = input;
    }
}
