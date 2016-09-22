package com.imac.voice_app.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.core.ActivityLauncher;
import com.imac.voice_app.util.mainmenu.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Toolbar view，custom attr in xml for：
 * backButtonVisibility format="boolean"
 * titleText format="string"
 * Created by flowmaHuang on 2016/9/13.
 */
public class ToolbarView extends RelativeLayout {
    @BindView(R.id.iv_back_left_arrow)
    ImageView backButton;
    @BindView(R.id.iv_menu)
    ImageView menuButton;
    @BindView(R.id.tv_title)
    TextView titleTextView;

    public interface toolbarCallBack {
        void backButtonListener();

        void menuButtonListener();
    }

    private toolbarCallBack listener = null;
    private Context mContext;
    private Activity mActivity;
    private View view;

    public ToolbarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.layout_toolbar, null, false);
        ButterKnife.bind(this,view);
        addView(view);

        loadStateFromAttrs(attributeSet);

    }

    private void loadStateFromAttrs(AttributeSet attributeSet) {
        if (attributeSet == null) {
            return; // quick exit
        }

        TypedArray a = null;
        try {
            a = getContext().obtainStyledAttributes(attributeSet, R.styleable.ToolbarView);
            if (!a.getBoolean(R.styleable.ToolbarView_backButtonVisibility, false)) {
                backButton.setVisibility(INVISIBLE);
            }
            titleTextView.setText(a.getText(R.styleable.ToolbarView_titleText));
        } finally {
            if (a != null) {
                a.recycle(); // ensure this is always called
            }
        }
    }

    @OnClick({R.id.iv_back_left_arrow, R.id.iv_menu})
    public void onClick(ImageView iv) {
        switch (iv.getId()) {
            case R.id.iv_back_left_arrow:
                if (listener != null) {
                    listener.backButtonListener();
                }
                break;
            case R.id.iv_menu:
                if (listener != null) {
                    listener.menuButtonListener();
                }
                break;
        }
    }

    public void setToolbarButtonCallBack(toolbarCallBack listener){
        this.listener = listener;
    }
}
