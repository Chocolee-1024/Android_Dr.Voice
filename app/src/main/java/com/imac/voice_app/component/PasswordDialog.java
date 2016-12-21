package com.imac.voice_app.component;

import android.app.Dialog;
import android.content.Context;

import com.imac.voice_app.R;

/**
 * Created by isa on 2016/12/6.
 */

public class PasswordDialog extends Dialog {
    public PasswordDialog(Context context) {
        super(context);
        setContentView(R.layout.fragment_input_password);
    }
}
