package com.imac.dr.voice_app.util.healtheducation;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.view.healtheducation.HealthEducationContainerView;

public class HealthEducationContainerFragment extends Fragment {
    private HealthEducationContainerView HealthEducationContainerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_health_edication_container,null);
        HealthEducationContainerView =new HealthEducationContainerView(getActivity(),view);
        return view;
    }
}
