package com.imac.dr.voice_app.util.dailyexercise;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.view.dailyexercise.DailySelectExerciseView;


/**
 * Created by isa on 2016/9/19.
 */
public class DailyExerciseSelectFragment extends Fragment {
    private DailySelectExerciseView dailySelectExerciseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_page, null);
        dailySelectExerciseView = new DailySelectExerciseView(getActivity(), view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        ((DailyExerciseActivity) getActivity()).setCountTextVisible(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        dailySelectExerciseView.setNotFinishTopic();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}