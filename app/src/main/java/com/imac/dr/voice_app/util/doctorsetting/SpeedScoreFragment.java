package com.imac.dr.voice_app.util.doctorsetting;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.module.database.data.SpeedDataStricture;
import com.imac.dr.voice_app.view.doctorsetting.SpeedScoreView;

/**
 * Created by isa on 2017/4/26.
 */

public class SpeedScoreFragment extends Fragment {

    private SpeedScoreView mSpeedScoreView;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speed_socure, container, false);
        mSpeedScoreView = new SpeedScoreView(getActivity(), view);
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        checkBundle();
    }

    private void checkBundle() {
        if (null == getArguments()) return;
        else if (!getArguments().containsKey(SpeedScoreListFragment.BUNDLE_KEY_SPEEDDATA)) return;
        else
            mSpeedScoreView.setData((SpeedDataStricture) getArguments().getParcelable(SpeedScoreListFragment.BUNDLE_KEY_SPEEDDATA));
    }
}
