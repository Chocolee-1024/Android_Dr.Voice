package com.imac.voice_app.view.doctorsetting;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.imac.voice_app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isa on 2016/12/6.
 */

public class InputPasswordView {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.input)
    EditText input;
    @BindView(R.id.submit)
    Button submit;

    private final static String DOCTOR_PASSWORD = "femh93265";

    private Activity mActivity;
    private CallbackEvent event;

    public InputPasswordView(Activity activity, View view) {
        this.mActivity = activity;
        ButterKnife.bind(this, view);
    }

    public void setCallbackEvent(CallbackEvent callbackEvent) {
        this.event = callbackEvent;
    }

    @OnClick(R.id.submit)
    public void onSubmitClick(View view) {
        if (null != event) {
            if (input.getText().toString().equals(DOCTOR_PASSWORD))
                event.onSubmitClick();
            else
                event.onSubmitError();
        }
    }

    public interface CallbackEvent {
        void onSubmitClick();

        void onSubmitError();
    }
}
