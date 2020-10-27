package com.imac.dr.voice_app.util.doctorsetting;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.view.doctorsetting.SpeedSettingView;

/**
 * Created by isa on 2016/12/20.
 */

public class SpeedSettingFragment extends Fragment {
    private SpeedSettingView speedSettingView;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speed_setting, container, false);
        speedSettingView=new SpeedSettingView(getActivity(),view);
        return view;
    }
}
