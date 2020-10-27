package com.imac.dr.voice_app.util.doctorsetting;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.view.doctorsetting.SpeedScoreListView;

/**
 * Created by isa on 2017/4/26.
 */

public class SpeedScoreListFragment extends Fragment {
    private SpeedScoreListView mSpeedScoreListView;
    public final static String BUNDLE_KEY_SPEEDDATA = "bundle_key_speeddata";


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speed_socure_list, container, false);
        mSpeedScoreListView = new SpeedScoreListView(getActivity(), view);
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
