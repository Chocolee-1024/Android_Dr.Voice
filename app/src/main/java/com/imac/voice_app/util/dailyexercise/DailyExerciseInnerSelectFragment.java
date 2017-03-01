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

/**
 * Created by isa on 2016/9/21.
 */
public class DailyExerciseInnerSelectFragment extends Fragment {
    private DailySelectInnerExerciseView dailySelectInnerExerciseView;
    private int topicIndex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        topicIndex = getArguments().getInt(DailySelectExerciseView.KEY_INNER_FRAGMENT_INDEX);
        View view = inflater.inflate(R.layout.fragment_select_page_inner, null);
        dailySelectInnerExerciseView = new DailySelectInnerExerciseView(
                getActivity(),
                view,
                topicIndex
        );
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onPause() {
        if (null != dailySelectInnerExerciseView.getPlayer()) {
            dailySelectInnerExerciseView.getPlayer().pausePlay();
        }
        dailySelectInnerExerciseView.getCounter().pauseCount();
        dailySelectInnerExerciseView.getCounterFiveSec().pauseCount();
        if (null != dailySelectInnerExerciseView.getPictureRunnable())
            dailySelectInnerExerciseView.getPictureRunnable().pause();
        if (null != dailySelectInnerExerciseView.getTextRunnable())
            dailySelectInnerExerciseView.getTextRunnable().pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        if (null != dailySelectInnerExerciseView.getPlayer() && MediaPlayer.Status.PAUSE.equals(dailySelectInnerExerciseView.getPlayer().getStatus())) {
            dailySelectInnerExerciseView.getPlayer().startPlay();
        }
        if (!dailySelectInnerExerciseView.isFiveSecCountDown()) {
            dailySelectInnerExerciseView.getCounter().continueCount();
        } else dailySelectInnerExerciseView.getCounterFiveSec().continueCount();
        if (null != dailySelectInnerExerciseView.getPictureRunnable())
            dailySelectInnerExerciseView.getPictureRunnable().restart();
        if (null != dailySelectInnerExerciseView.getTextRunnable())
            dailySelectInnerExerciseView.getTextRunnable().restart();

        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (null != dailySelectInnerExerciseView.getPlayer()) {
            dailySelectInnerExerciseView.getPlayer().stopPlay();
        }
        dailySelectInnerExerciseView.getCounter().stopCount();
        dailySelectInnerExerciseView.getCounterFiveSec().stopCount();
        super.onDestroy();
    }
}
