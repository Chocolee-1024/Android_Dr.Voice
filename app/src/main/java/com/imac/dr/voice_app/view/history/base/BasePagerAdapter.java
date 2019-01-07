package com.imac.dr.voice_app.view.history.base;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.component.Histogram;
import com.imac.dr.voice_app.module.database.data.WeeklyDataStructure;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isa on 2016/10/26.
 */
public abstract class BasePagerAdapter extends PagerAdapter {
    @BindView(R.id.chart)
    Histogram chart;
    private Activity activity = null;
    private WeeklyDataStructure[] mWeeklyDataStructures = null;
    private String[] month = null;

    public BasePagerAdapter(Activity activity, String[] month, WeeklyDataStructure[] weeklyDataStructures) {
        this.activity = activity;
        this.mWeeklyDataStructures = weeklyDataStructures;
        this.month = month;
    }

    @Override
    public int getCount() {
        return month.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View histogramLayout = inflater.inflate(R.layout.layout_histogram, container, false);
        ButterKnife.bind(this, histogramLayout);
        chart.startDraw(
                dateCompare(position),
                calculate(position).getPointPercent(),
                calculate(position).getTotalScore()
        );
        container.addView(histogramLayout);
        return histogramLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    protected abstract String[] dateCompare(int position);

    protected abstract PointStruction calculate(int position);

    public class PointStruction {
        private float[] pointPercent;
        private String[] totalScore;

        public void setPointPercent(float[] pointPercent) {
            this.pointPercent = pointPercent;
        }

        public void setTotalScore(String[] totalScore) {
            this.totalScore = totalScore;
        }

        public float[] getPointPercent() {
            return pointPercent;
        }

        public String[] getTotalScore() {
            return totalScore;
        }
    }
}
