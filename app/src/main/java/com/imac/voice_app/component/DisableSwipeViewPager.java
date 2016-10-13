package com.imac.voice_app.component;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by isa on 2016/10/5.
 */
public class DisableSwipeViewPager extends ViewPager {
    private boolean enable=false;
    public DisableSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.enable;
    }
}
