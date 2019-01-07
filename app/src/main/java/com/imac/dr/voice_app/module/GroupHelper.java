package com.imac.dr.voice_app.module;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by isa on 2017/2/13.
 */

public class GroupHelper {
    private ArrayList<TextView> mArrayList;
    private CallBack mCallBack;

    public GroupHelper() {
        mArrayList = new ArrayList<>();
    }

    private void setDefaultBackGround() {
        for (int i = 0; i < mArrayList.size(); i++) {
            mArrayList.get(i).setBackgroundColor(Color.parseColor("#eff0eb"));
        }
    }

    private void setListener() {
        if (mArrayList.size() == 0) return;
        for (int i = 0; i < mArrayList.size(); i++) {
            mArrayList.get(i).setOnClickListener(onClickListener(i));
        }
    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCallBack) {
                    setDefaultBackGround();
                    v.setBackgroundColor(Color.parseColor("#a36978"));
                    mCallBack.onClick(position, mArrayList);
                }
            }
        };
    }

    public void add(TextView textView) {
        mArrayList.add(textView);
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
        setListener();
    }

    public void setInitClickStatus(int position) {
        setDefaultBackGround();
        mArrayList.get(position).setBackgroundColor(Color.parseColor("#a36978"));
    }

    public interface CallBack {
        void onClick(int position, ArrayList<TextView> textViewArrayList);
    }
}
