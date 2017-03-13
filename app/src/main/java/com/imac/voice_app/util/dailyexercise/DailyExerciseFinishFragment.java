package com.imac.voice_app.util.dailyexercise;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.voice_app.R;
import com.imac.voice_app.view.dailyexercise.DailyExerciseFinishView;
import com.imac.voice_app.view.dailyexercise.DailySelectInnerExerciseView;

import javax.annotation.Nullable;

import butterknife.ButterKnife;

/**
 * Created by isa on 2016/9/26.
 */
public class DailyExerciseFinishFragment extends Fragment{

    private DailyExerciseFinishView dailyExerciseFinishView;
    public static final String KEY_DAILY_COMPLETE = "key_daily_complete";
    public static final String KEY_DAILY_ENABLE_DATE = "key_daily_enable_date";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int topicIndex = getArguments().getInt(DailySelectInnerExerciseView.KEY_TOPIC_INDEX);
        View view = inflater.inflate(R.layout.fragment_daily_exercise_inner_finish, null);
        dailyExerciseFinishView = new DailyExerciseFinishView(getActivity(), view, topicIndex);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        dailyExerciseFinishView.getCounter().continueCount();
        super.onResume();
    }

    @Override
    public void onPause() {
        dailyExerciseFinishView.getCounter().pauseCount();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        dailyExerciseFinishView.getCounter().stopCount();
        super.onDestroy();
    }


}
