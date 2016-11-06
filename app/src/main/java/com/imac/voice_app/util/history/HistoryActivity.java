package com.imac.voice_app.util.history;

import android.app.Activity;
import android.os.Bundle;

import com.imac.voice_app.R;
import com.imac.voice_app.util.login.LoginActivity;
import com.imac.voice_app.view.history.HistoryView;

import java.util.ArrayList;

/**
 * Created by isa on 2016/10/24.
 */
public class HistoryActivity extends Activity {

    private HistoryView historyView = null;
    private ArrayList<String> weeklyTopicList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getBundle();
        historyView = new HistoryView(this);
    }

    private void getBundle() {
        weeklyTopicList = (ArrayList<String>) getIntent().getExtras().getSerializable(LoginActivity.KEY_WEEKLY_EXERCISE);
    }

    public ArrayList<String> getWeeklyTopicList() {
        return weeklyTopicList;
    }
}
