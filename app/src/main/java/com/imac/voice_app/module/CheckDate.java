package com.imac.voice_app.module;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2016/7/5.
 * 用來確認檔案日期
 * 會取其中一個檔案判斷日期，如果日期為昨天
 * 會將全部的檔案上傳
 */
public class CheckDate {
    private Context context;

    public CheckDate(Context context) {
        super();
        this.context = context;

    }

    public void check() {
        Date date = new Date();
        String path = Environment.getExternalStorageDirectory().getPath()
                + "/" + context.getPackageName();

        File file = new File(path);
        file.mkdir();
        final File[] fileList = file.listFiles();
        if (fileList.length == 0) {
            return;
        }
        for (int i = 0; i < fileList.length; i++) {
            File uploadFile = fileList[i];
            String fileName = fileList[i].getName();
            String dateString = fileName.split("N")[0];
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date fileDate = format.parse(dateString);
                Log.e("date", String.valueOf((date.getTime() - fileDate.getTime()) / (1000 * 60 * 60 * 24)));
                if (((date.getTime() - fileDate.getTime()) / (1000 * 60 * 60 * 24)) > 0) {
                    DriveConnect connect = new DriveConnect(context, uploadFile);
                    connect.connect();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

}
