package com.imac.dr.voice_app.util.history;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.view.history.HistoryDetailView;

/**
 * Created by isa on 2016/10/24.
 */
public class HistoryDetailFragment extends Fragment {
    private HistoryDetailView historyDetailView = null;
    private boolean isShow = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getBundle();
        View view = inflater.inflate(R.layout.fragment_history_detail, null, false);
        historyDetailView = new HistoryDetailView(getActivity(), view);
        historyDetailView.setSwitch(isShow);
        return view;
    }

    private void getBundle() {
//        isShow = getArguments().getBoolean(HistoryFragment.KEY_IS_SHOW_SPINNER);
    }
}
