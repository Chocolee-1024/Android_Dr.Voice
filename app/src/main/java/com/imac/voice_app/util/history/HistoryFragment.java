package com.imac.voice_app.util.history;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.voice_app.R;
import com.imac.voice_app.core.FragmentLauncher;
import com.imac.voice_app.view.history.HistoryFragmentView;

/**
 * Created by isa on 2016/10/24.
 */
public class HistoryFragment extends Fragment implements HistoryFragmentView.onClickEvent {
    private HistoryFragmentView historyFragmentView = null;
    public static final String KEY_IS_SHOW_SPINNER = "key_is_show_spinner";
    public boolean isShowTopicSpinner = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, null, false);
        historyFragmentView = new HistoryFragmentView(getActivity(), view, this);
        return view;
    }

    @Override
    public void onVoiceHistoryClick() {
        isShowTopicSpinner = false;
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_IS_SHOW_SPINNER, isShowTopicSpinner);
        FragmentLauncher.changeToBack(getActivity(), R.id.container, bundle, HistoryDetailFragment.class.getName());
    }

    @Override
    public void onVoiceDailyExerciseHistory() {
        isShowTopicSpinner = true;
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_IS_SHOW_SPINNER, isShowTopicSpinner);
        FragmentLauncher.changeToBack(getActivity(), R.id.container, bundle, HistoryDetailFragment.class.getName());

    }
}
