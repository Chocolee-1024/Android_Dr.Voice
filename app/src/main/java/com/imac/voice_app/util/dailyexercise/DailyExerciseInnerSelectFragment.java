package com.imac.voice_app.util.dailyexercise;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.voice_app.R;
import com.imac.voice_app.module.MediaPlayer;
import com.imac.voice_app.view.dailyexercise.DailySelectExerciseView;
import com.imac.voice_app.view.dailyexercise.DailySelectInnerExerciseView;

import butterknife.ButterKnife;

/**
 * Created by isa on 2016/9/21.
 */
public class DailyExerciseInnerSelectFragment extends Fragment {
    private DailySelectInnerExerciseView dailySelectInnerExerciseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int topicIndex = getArguments().getInt(DailySelectExerciseView.KEY_INNER_FRAGMENT_INDEX);
        View view = inflater.inflate(R.layout.fragment_select_page_inner, null);
        dailySelectInnerExerciseView = new DailySelectInnerExerciseView(
                getActivity(),
                view,
                topicIndex
        );
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onPause() {
        dailySelectInnerExerciseView.getCounter().pauseCount();
        dailySelectInnerExerciseView.getPlayer().pausePlay();
        super.onPause();
    }

    @Override
    public void onResume() {
        if (!dailySelectInnerExerciseView.isFiveSecCountDown() &&
                (dailySelectInnerExerciseView.getPlayer().getStatus() == MediaPlayer.Status.PAUSE)) {
            dailySelectInnerExerciseView.getCounter().continueCount();
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        dailySelectInnerExerciseView.getPlayer().stopPlay();
        dailySelectInnerExerciseView.getCounter().stopCount();
        dailySelectInnerExerciseView.getCounterFiveSec().stopCount();
        super.onDestroy();
    }
}
