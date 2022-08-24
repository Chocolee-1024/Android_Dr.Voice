package com.imac.dr.voice_app.view.healtheducation;

import android.app.Activity;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.component.CustomIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HealthEducationContainerView {
    private  Activity mActivity;
    private View mView;
    @BindView(R.id.health_education_viewpager)
    ViewPager viewPager;
    @BindView(R.id.health_education＿indicator)
    CustomIndicator customIndicator;
    private  HealthEducationAdapter healthEducationAdapter;
    //傳入建構值
    public HealthEducationContainerView(Activity activity , View view) {
        this.mActivity=activity;
        this.mView=view;
        ButterKnife.bind(this,view);
        init();
    }

    private  void init(){
        //建立healthEducationAdapter
        healthEducationAdapter=new HealthEducationAdapter(mActivity);
        //設定viewPager的Adapter
        viewPager.setAdapter(healthEducationAdapter);
        boolean [] booleans =new boolean[8];
        //用來建立頁面底下的指標(.....)
        customIndicator.setIsFinish(booleans);
        customIndicator.setViewPager(viewPager);
    }
}
