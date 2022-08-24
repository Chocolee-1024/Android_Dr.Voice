package com.imac.dr.voice_app.module;

import android.util.Log;

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
        //把字串分開，如果遇到"_,_"就分段
        String[] splitString = data.split(SPLIT);
        ArrayList<String> result = new ArrayList<>();
        //分段後一個一個放入，List
        for (String index : splitString)
            if (!"".equals(index))
                result.add(index);
        return result;
    }

    public ArrayList<Boolean> formatBoolean(String data) {
        //如果遇到"_,_"，就把傳入的資料分段放入。
        String[] splitString = data.split(SPLIT);
        ArrayList<Boolean> result = new ArrayList<>();
        //並把值轉為Boolean，放入List。
        for (String index : splitString)
            if (!"".equals(index))
                result.add(Boolean.valueOf(index));
        return result;
    }

}
