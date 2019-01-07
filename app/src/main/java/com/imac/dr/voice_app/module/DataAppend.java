package com.imac.dr.voice_app.module;

import java.util.ArrayList;

/**
 * Created by isa on 2016/10/6.
 */
public class DataAppend {
    private final String SPLIT = "_,_";

    public DataAppend() {
    }

    public String append(ArrayList<String> arrayList) {
        String result = "";
        for (String data : arrayList) {
            result = result + SPLIT + data;
        }
        return result;
    }

    public ArrayList<String> formatString(String data) {
        String[] splitString = data.split(SPLIT);
        ArrayList<String> result = new ArrayList<>();
        for (String index : splitString) {
            if (!"".equals(index))
                result.add(index);
        }
        return result;
    }

    public ArrayList<Boolean> formatBoolean(String data) {
        String[] splitString = data.split(SPLIT);
        ArrayList<Boolean> result = new ArrayList<>();
        for (String index : splitString) {
            if (!"".equals(index))
                result.add(Boolean.valueOf(index));
        }
        return result;
    }

}
