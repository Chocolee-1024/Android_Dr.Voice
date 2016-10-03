package com.imac.voice_app.view.dailyexercise;

import android.app.Activity;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.module.FontManager;

import java.util.ArrayList;

/**
 * Created by isa on 2016/9/20.
 */
public class DailySelectExerciseAdapter extends PagerAdapter {
    private Activity activity;
    private ArrayList<View> pageList;
    private ArrayList<Integer> topicList;

    public DailySelectExerciseAdapter(Activity activity, ArrayList<Integer> topicList) {
        this.activity = activity;
        this.topicList = topicList;
        pageList = new ArrayList<>();
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(activity);
        for (int i = 0; i < topicList.size(); i++) {
            View view = inflater.inflate(R.layout.layout_viewpager_item, null);
            pageList.add(view);
        }
    }

    @Override
    public int getCount() {
        return pageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(pageList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.practice_icon_array);

        TextView title = (TextView) pageList.get(position).findViewById(R.id.view_pager_title);
        ImageView image = (ImageView) pageList.get(position).findViewById(R.id.view_pager_image);
        title.setText(activity.getResources().getStringArray(R.array.daily_exercise_title_item)[topicList.get(position) - 1]);
        image.setImageResource(typedArray.getResourceId(topicList.get(position) - 1, -1));
        FontManager.setFont(activity,FontManager.MEDIUM,title);
        container.addView(pageList.get(position));
        return pageList.get(position);
    }

}
