package com.imac.voice_app.view.homepage;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.module.FontManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isa on 2016/9/8.
 */
public class HomePage {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.start)
    Button start;
    @BindView(R.id.developer)
    TextView developer;
    private OnClickEvent event;
    private Activity activity;
    public HomePage(Activity activity,OnClickEvent event) {
        ButterKnife.bind(this, activity);
        this.event=event;
        this.activity=activity;
        setFontType();
    }
    private void setFontType(){
        FontManager.setFont(activity,FontManager.LIGHT,title,developer);
        FontManager.setFont(activity,FontManager.MEDIUM,start);
    }
    @OnClick(R.id.start)
    public void clickStart() {
        event.onClick();
    }

    public interface OnClickEvent {
        void onClick();
    }
}
