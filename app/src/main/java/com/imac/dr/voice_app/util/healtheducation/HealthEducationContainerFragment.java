package com.imac.dr.voice_app.util.healtheducation;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.view.healtheducation.HealthEducationContainerView;

public class HealthEducationContainerFragment extends Fragment {
    private HealthEducationContainerView HealthEducationContainerView;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        //把fragment_health_edication_container這個layour放入
        View view =inflater.inflate(R.layout.fragment_health_edication_container,null);
        HealthEducationContainerView =new HealthEducationContainerView(getActivity(),view);
        return view;
    }
}
