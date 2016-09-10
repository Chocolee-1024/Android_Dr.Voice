package com.imac.voice_app.view.mainmenu;

import android.app.Activity;
import android.content.res.TypedArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.module.FontManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isa on 2016/9/8.
 */
public class MainMenu {
    private Activity activity;
    @BindView(R.id.container)
    LinearLayout container;
    private OnClickEvent clickEvent;

    public MainMenu(Activity activity, OnClickEvent clickEvent) {
        this.activity = activity;
        ButterKnife.bind(this, activity);
        this.clickEvent = clickEvent;
        createMenu();
    }

    private void setFontType(TextView view){
        FontManager.setFont(activity,FontManager.NORMAL,view);
    }
    private void createMenu() {
        TypedArray imgArray = activity.getResources().obtainTypedArray(R.array.menu_icon_array);
        LayoutInflater inflater = LayoutInflater.from(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.weight = 1;
        params.gravity = Gravity.CENTER_HORIZONTAL;
        for (int i = 0; i < 5; i++) {
            View view = inflater.inflate(R.layout.menu_item, null);
            TextView text = (TextView) view.findViewById(R.id.menu_text);
            ImageView icon = (ImageView) view.findViewById(R.id.menu_icon);
            setFontType(text);
            text.setText(activity.getResources().getStringArray(R.array.menu_text_array)[i]);
            icon.setImageResource(imgArray.getResourceId(i, -1));
            view.setLayoutParams(params);
            View line = new View(activity);
            LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1
            );
            line.setBackgroundColor(activity.getResources().getColor(R.color.line_color));
            line.setLayoutParams(lineParams);
            final int position = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickEvent.onClick(view,position);
                }
            });
            container.addView(view);
            container.addView(line);
        }
    }

    public interface OnClickEvent {
        void onClick(View view,int position);
    }
}
