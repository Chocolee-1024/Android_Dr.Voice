package com.imac.voice_app.util.dailyexercise;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.voice_app.R;
import com.imac.voice_app.view.dailyexercise.DailySelectExerciseView;

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
    public void onDestroy() {
        super.onDestroy();
    }
}
