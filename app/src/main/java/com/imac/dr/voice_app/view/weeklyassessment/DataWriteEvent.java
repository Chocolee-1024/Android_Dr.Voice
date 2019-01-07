package com.imac.dr.voice_app.view.weeklyassessment;

import java.util.ArrayList;

/**
 * Created by isa on 2016/10/19.
 */
public interface DataWriteEvent {
    void  onDataWrite(ArrayList<String> soundPoint, ArrayList<String> soundTopic, ArrayList<String> assessmentPoint);
}
