package com.imac.dr.voice_app.component;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.core.Ruler;

/**
 * Created by isa on 2016/9/21.
 */
public class CustomIndicator extends LinearLayout {
    private Context context;
    private View[] views;
    private boolean[] isFinish;

    public CustomIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        setOrientation(HORIZONTAL);
    }

    private void init(int itemCount) {
        Ruler ruler=new Ruler(context);
        for (int i = 0; i < itemCount; i++) {
            views[i] = new View(context);
            views[i].setBackgroundResource(R.drawable.drawable_indicator_no_select);
            LayoutParams params = new LayoutParams(
                    ruler.getW(2),
                    ruler.getW(2)
            );
            params.rightMargin=ruler.getW(5);
            params.weight = 1;
            views[i].setLayoutParams(params);
            addView(views[i]);
        }

        if (isFinish[0]) {
            views[0].setBackgroundResource(R.drawable.drawable_indicator_finish_select);
        } else {
            views[0].setBackgroundResource(R.drawable.drawable_indicator_select);
        }
        for (int i = 1; i < views.length; i++) {
            if (isFinish[i]) {
                views[i].setBackgroundResource(R.drawable.drawable_indicator_finish_no_select);
            } else {
                views[i].setBackgroundResource(R.drawable.drawable_indicator_no_select);
            }
        }
    }

    public void setViewPager(ViewPager viewPager) {
        int itemCount = viewPager.getAdapter().getCount();
        views = new View[itemCount];
        init(itemCount);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int index = 0;
                for (View temp : views) {
                    if (isFinish[index]) {
                        temp.setBackgroundResource(R.drawable.drawable_indicator_finish_no_select);
                    } else {
                        temp.setBackgroundResource(R.drawable.drawable_indicator_no_select);
                    }
                    index++;
                }
                if (isFinish[position]) {
                    views[position].setBackgroundResource(R.drawable.drawable_indicator_finish_select);
                } else {
                    views[position].setBackgroundResource(R.drawable.drawable_indicator_select);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setIsFinish(boolean[] isFinish) {
        this.isFinish = isFinish;
    }

}
