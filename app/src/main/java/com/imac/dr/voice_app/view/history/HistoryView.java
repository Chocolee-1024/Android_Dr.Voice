package com.imac.dr.voice_app.view.history;

import android.app.Activity;
import android.widget.FrameLayout;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.component.ToolbarView;
import com.imac.dr.voice_app.core.FragmentLauncher;
import com.imac.dr.voice_app.util.history.HistoryDetailFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isa on 2016/10/24.
 */
public class HistoryView {
    @BindView(R.id.toolbar)
    ToolbarView toolbar;
    @BindView(R.id.container)
    FrameLayout container;
    private Activity activity = null;

    public HistoryView(Activity activity) {
        ButterKnife.bind(this, activity);
        this.activity = activity;
        toolbar.setToolbarButtonCallBack(setCallBackEvent());
        change();
    }

    private ToolbarView.toolbarCallBack setCallBackEvent() {
        return new ToolbarView.toolbarCallBack() {
            @Override
            public void backButtonListener() {

            }

            @Override
            public void menuButtonListener() {
                activity.finish();
            }
        };
    }

    private void change() {
        FragmentLauncher.change(activity, R.id.container, null, HistoryDetailFragment.class.getName());
    }
}
