package com.imac.dr.voice_app.component;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imac.dr.voice_app.R;

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
    //對外接口
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
        ButterKnife.bind(this, view);
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
    //iv_back_left_arrow和iv_menu監聽
    @OnClick({R.id.iv_back_left_arrow, R.id.iv_menu})
    public void onClick(ImageView iv) {
        //判斷是按下哪一個
        switch (iv.getId()) {
            //如果是iv_back_left_arrow呼叫backButtonListener
            case R.id.iv_back_left_arrow:
                if (listener != null) {
                    listener.backButtonListener();
                }
                break;
            //    如果是iv_menu呼叫menuButtonListener
            case R.id.iv_menu:
                if (listener != null) {
                    listener.menuButtonListener();
                }
                break;
        }
    }
    //傳入listener
    public void setToolbarButtonCallBack(toolbarCallBack listener) {
        this.listener = listener;
    }
    //傳入tv_title要的值
    public void setTitleTextViewText(String text){
        this.titleTextView.setText(text);
    }

    public String getTitleTextViewText (){
        return (String) this.titleTextView.getText();
    }

    public void setBackButtonVisibility(boolean visibility){
        if (visibility) {
            backButton.setVisibility(VISIBLE);
        } else {
            backButton.setVisibility(INVISIBLE);
        }
    }

    public boolean getBackButtonVisibility () {
        return backButton.getVisibility() == VISIBLE;
    }
}
