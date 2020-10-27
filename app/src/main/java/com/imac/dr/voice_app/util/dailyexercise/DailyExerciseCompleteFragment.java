package com.imac.dr.voice_app.util.dailyexercise;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.view.dailyexercise.DailyExerciseCompleteView;

/**
 * Created by isa on 2016/9/27.
 */
public class DailyExerciseCompleteFragment extends Fragment {

private DailyExerciseCompleteView dailyExerciseCompleteView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_exercise_finish, null);
         dailyExerciseCompleteView = new DailyExerciseCompleteView(getActivity(), view);
        return view;
    }
}
