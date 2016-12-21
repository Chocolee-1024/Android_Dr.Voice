package com.imac.voice_app.util.doctorsetting;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.imac.voice_app.R;
import com.imac.voice_app.core.FragmentLauncher;
import com.imac.voice_app.view.doctorsetting.InputPasswordView;

/**
 * Created by isa on 2016/12/6.
 */

public class InputPasswordFragment extends Fragment {

    private InputPasswordView inputPasswordView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_password, container, false);
        inputPasswordView = new InputPasswordView(getActivity(), view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        inputPasswordView.setCallbackEvent(callbackEvent);
    }

    private InputPasswordView.CallbackEvent callbackEvent = new InputPasswordView.CallbackEvent() {
        @Override
        public void onSubmitClick() {
            FragmentLauncher.changeToBack(getActivity(),R.id.container, null, DoctorSettingMenuFragment.class.getName());
        }

        @Override
        public void onSubmitError() {
            Toast.makeText(getActivity(),"密碼錯誤，請重試",Toast.LENGTH_SHORT).show();
        }
    };
}

