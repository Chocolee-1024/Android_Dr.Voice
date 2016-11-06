package com.imac.voice_app.view.history;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imac.voice_app.R;

import java.util.ArrayList;

/**
 * Created by isa on 2016/10/24.
 */
public class TopicSpinnerAdapter extends BaseAdapter {
    private ArrayList<String> weeklyTopicList = null;
    private Activity activity = null;
    private LayoutInflater layoutInflater = null;

    public TopicSpinnerAdapter(Activity activity, ArrayList<String> weeklyTopicList) {
        super();
        this.activity = activity;
        this.weeklyTopicList = weeklyTopicList;
        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return weeklyTopicList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View item = layoutInflater.inflate(R.layout.layout_item_spinner, null, false);
        TextView textView = (TextView) item.findViewById(R.id.item);
        textView.setText(weeklyTopicList.get(i));
        return textView;
    }
}
