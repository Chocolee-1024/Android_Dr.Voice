package com.imac.dr.voice_app.view.dailyexercise;

import android.app.Activity;
import android.content.res.TypedArray;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imac.dr.voice_app.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isa on 2016/9/20.
 */
public class DailySelectExerciseAdapter extends PagerAdapter {
    @BindView(R.id.view_pager_title)
    TextView viewPagerTitle;
    @BindView(R.id.view_pager_image)
    ImageView viewPagerImage;
    private Activity activity;
    private ArrayList<Integer> topicList;

    public DailySelectExerciseAdapter(Activity activity, ArrayList<Integer> topicList) {
        this.activity = activity;
        this.topicList = topicList;
    }

    @Override
    public int getCount() {
        return topicList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.layout_viewpager_item, null);
        ButterKnife.bind(this, view);

        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.practice_icon_array);
        viewPagerTitle.setText(activity.getResources().getStringArray(R.array.daily_exercise_title_item)[topicList.get(position)]);
        viewPagerImage.setImageResource(typedArray.getResourceId(topicList.get(position), -1));
        container.addView(view);
        return view;
    }
}
