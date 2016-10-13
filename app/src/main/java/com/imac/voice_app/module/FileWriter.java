package com.imac.voice_app.module;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

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
public class FileWriter {
    private Context context;
    private ArrayList<Integer> textNumArrayList;
    private int index = 0;
    private File file;
    private Date startDate;

    public FileWriter(Context context) {
        super();
        this.context = context;
        textNumArrayList = new ArrayList<>();
    }

    /**
     * 寫入資料到儲存空間內
     * 以日期命名
     */
    public void write(String name, ArrayList<String> textArrayList) {
        for (int i = 0; i < textArrayList.size(); i++) {
            textNumArrayList.add(textArrayList.get(i).length());
        }
        if (textNumArrayList.size() == 0) {
            return;
        }
        String path = Environment.getExternalStorageDirectory().getPath()
                + "/" + context.getPackageName();
        File sdFile = new File(path);
        sdFile.mkdir();
        Log.e("path", path);
        try {
            int temp = 0;
            Date date = new Date();
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat dayFormat = new SimpleDateFormat("MM-dd");
            SimpleDateFormat minFormat = new SimpleDateFormat("hh:mm");
            SimpleDateFormat secFormat = new SimpleDateFormat("hh:mm:ss");

            String yearFormatDate = yearFormat.format(date);
            String dayFormatDate = dayFormat.format(date);
            String minFormatDate = minFormat.format(date);
            String startMinFormatDate = secFormat.format(startDate);
            String secFormatDate = secFormat.format(date);
            //如果資料夾沒檔案
            if (sdFile.listFiles().length == 0) {
                Log.e("file exit", "檔案不存在");
                file = new File(sdFile, dayFormatDate + " " + minFormatDate + ".csv");
            }
            //如果資料夾有檔案
            //把資料寫入第一個檔案中
            else {
                Log.e("file exit", "檔案存在");
                file = sdFile.listFiles()[0];
            }
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, true));
            printWriter.write("," + name);
            printWriter.append("," + yearFormatDate);
            printWriter.append("," + startMinFormatDate);
            printWriter.append("," + secFormatDate);
            for (int i = 0; i < textNumArrayList.size(); i++) {
                Log.e("temp", "字數累加 :  " + temp);
                //字數累加
                temp += textNumArrayList.get(i);
            }
            printWriter.append("," + Integer.toString(temp));
            printWriter.append("," + Integer.toString(temp / textNumArrayList.size() * 2) + ",");
            printWriter.append("\n");
            printWriter.flush();
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("FileNotFoundException", e.toString());
        }
    }

    public void setStartTime(Date date) {
        this.startDate = date;
    }

    public File getFile() {
        return file;
    }
}
