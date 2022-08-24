package com.imac.dr.voice_app.component;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
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
        //把這一個LinearLayout設為水平的
        setOrientation(HORIZONTAL);
    }

    private void init(int itemCount) {
        //new出ruler
        Ruler ruler=new Ruler(context);
        for (int i = 0; i < itemCount; i++) {
            views[i] = new View(context);
            //設置指標(....)的背景
            views[i].setBackgroundResource(R.drawable.drawable_indicator_no_select);
            //第一個參數為寬，第二個參數為高。
            LayoutParams params = new LayoutParams(
                    ruler.getW(2),
                    ruler.getW(2)
            );
            //控制靠右邊距
            params.rightMargin=ruler.getW(5);
            //設定權重
            params.weight = 1;
            //set設定值
            views[i].setLayoutParams(params);
            //add view。
            addView(views[i]);
        }
        //設定setBackgroundResource(如果true，綠色，false，白色)
        if (isFinish[0]) {
            views[0].setBackgroundResource(R.drawable.drawable_indicator_finish_select);
        } else {
//            views[0].setBackgroundResource(R.drawable.drawable_indicator_select);
            views[0].setBackgroundResource(R.drawable.drawable_indicator_finish_select);
        }
        for (int i = 1; i < views.length; i++) {
            if (isFinish[i]) {
                views[i].setBackgroundResource(R.drawable.drawable_indicator_finish_no_select);
            } else {
//                views[i].setBackgroundResource(R.drawable.drawable_indicator_no_select);
                views[i].setBackgroundResource(R.drawable.drawable_indicator_select);
            }
        }
    }

    public void setViewPager(ViewPager viewPager) {
        //拿取viewPager設定的Adapter裡的Count(有幾頁)
        int itemCount = viewPager.getAdapter().getCount();
        //新增陣列
        views = new View[itemCount];
        init(itemCount);
        //viewPager監聽
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int index = 0;
                //設定setBackgroundResource(如果true，綠色，false，白色)
                for (View temp : views) {
                    if (isFinish[index]) {
                        temp.setBackgroundResource(R.drawable.drawable_indicator_finish_no_select);
                    } else {
//                        temp.setBackgroundResource(R.drawable.drawable_indicator_no_select);
                        views[index].setBackgroundResource(R.drawable.drawable_indicator_select);
                        Log.e("note", String.valueOf(index));
                    }
                    index++;
                }
                //設定position的view setBackgroundResource(如果true，綠色黑線，false，白色黑線)
                if (isFinish[position]) {
                    views[position].setBackgroundResource(R.drawable.drawable_indicator_finish_select);
                } else {
//                    views[position].setBackgroundResource(R.drawable.drawable_indicator_select);
                    views[position].setBackgroundResource(R.drawable.drawable_indicator_finish_select);
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
