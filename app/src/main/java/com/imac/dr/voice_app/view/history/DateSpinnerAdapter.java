package com.imac.dr.voice_app.view.history;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imac.dr.voice_app.R;

/**
 * Created by isa on 2016/10/25.
 */
public class DateSpinnerAdapter extends BaseAdapter {
    private Activity activity = null;
    private String[] dateArray = null;
    private LayoutInflater layoutInflater = null;

    public DateSpinnerAdapter(Activity activity, String[] dateArray) {
        super();
        this.activity = activity;
        this.dateArray = dateArray;
        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return dateArray.length;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View item = layoutInflater.inflate(R.layout.layout_item_spinner, null, false);
        TextView textView= (TextView) item.findViewById(R.id.item);
        textView.setText(dateArray[i]);
        return textView;
    }
}
