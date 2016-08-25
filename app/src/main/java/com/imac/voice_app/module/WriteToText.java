package com.imac.voice_app.module;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.imac.voice_app.util.speakSpeed.SpeakSpeedActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by user on 2016/6/29.
 */
public class WriteToText {
    private Context context;
    private ArrayList<Integer> textNumArrayList;
    private int index = 0;

    public WriteToText(Context context, ArrayList<String> textArrayList) {
        super();
        this.context = context;
        textNumArrayList = new ArrayList<>();
        for (int i = 0; i < textArrayList.size(); i++) {
            textNumArrayList.add(textArrayList.get(i).length());
        }
    }

    /**
     * 寫入資料到儲存空間內
     * 以日期命名
     */
    public void write() {
        String path = Environment.getExternalStorageDirectory().getPath()
                + "/" + context.getPackageName();
        File sdFile = new File(path);
        sdFile.mkdir();
        Log.e("path", path);
        try {
            int temp = 0;
            Date date = new Date();
            SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat minFormat = new SimpleDateFormat("hh:mm");
            String dayFormatDate = dayFormat.format(date);
            String minFormatDate = minFormat.format(date);
            File file;
            //如果資料夾沒檔案
            if (sdFile.listFiles().length == 0) {
                Log.e("file exit", "檔案不存在");
                //TODO  檔名可能會修改
                file = new File(sdFile, dayFormatDate + "Name" + ((SpeakSpeedActivity) context).getName() + ".csv");
            }
            //如果資料夾有檔案
            //把資料寫入第一個檔案中
            else {
                Log.e("file exit", "檔案存在");
                file = sdFile.listFiles()[0];
            }
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, true));
            //TODO 寫入的項目可能會再變
            printWriter.append("," + dayFormatDate);
            printWriter.append("," + minFormatDate);
//            printWriter.write("," + ((MainActivity) context).getName());
            for (int i = 0; i < textNumArrayList.size(); i++) {
                Log.e("temp", "字數累加 :  " + temp);
                //字數累加
                temp += textNumArrayList.get(i);
            }
            printWriter.append("," + Integer.toString(temp));
            printWriter.append("," + ((SpeakSpeedActivity) context).getSex() + ",");
            printWriter.append("\n");
            printWriter.flush();
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("FileNotFoundException", e.toString());
        }
    }
}
