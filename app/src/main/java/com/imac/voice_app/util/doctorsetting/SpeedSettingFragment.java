package com.imac.voice_app.util.doctorsetting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.voice_app.R;
import com.imac.voice_app.view.doctorsetting.SpeedSettingView;

/**
 * Created by isa on 2016/12/20.
 */

public class SpeedSettingFragment extends Fragment {
    private SpeedSettingView speedSettingView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speed_setting, container, false);
        speedSettingView=new SpeedSettingView(getActivity(),view);
        return view;
    }
}
