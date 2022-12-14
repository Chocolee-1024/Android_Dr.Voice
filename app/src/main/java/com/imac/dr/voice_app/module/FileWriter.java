package com.imac.dr.voice_app.module;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by user on 2016/6/29.
 */
public class FileWriter {
    private Context context;
    private int index = 0;
    private File file;
    private Date startDate;
    private WriterCallBack writerCallBack = null;

    public FileWriter(Context context) {
        this.context = context;
    }

    /**
     * {@link com.imac.dr.voice_app.util.speakSpeed.SpeakSpeedActivity} 寫入資料到儲存空間內 以日期命名
     */
    public void write( ArrayList<String> textArrayList) {
        ArrayList<Integer> textNumArrayList = new ArrayList<>();
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
            File file = new File(sdFile, "speed"  + ".csv");
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, true));
            if (isFileEmpty(file)) {
                printWriter.append("日期,開始時間,結束時間,總字數,總錄製時間,語速\n");
            }
            printWriter.append(yearFormatDate);
            printWriter.append("," + startMinFormatDate);
            printWriter.append("," + secFormatDate);
            for (int i = 0; i < textNumArrayList.size(); i++) {
                Log.e("temp", "字數累加 :  " + temp);
                //字數累加
                temp += textNumArrayList.get(i);
            }
            printWriter.append("," + Integer.toString(temp));
            printWriter.append("," + (date.getTime()-startDate.getTime())/1000);
            printWriter.append("," + Integer.toString(temp / textNumArrayList.size() * 2) + ",");
            printWriter.append("\n");
            printWriter.flush();
            printWriter.close();
            writerCallBack.onWriteSuccessful(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("FileNotFoundException", e.toString());
            writerCallBack.onWriteFail();
        }
    }

    public void write(ArrayList<String> soundPoint, ArrayList<String> soundTopic, ArrayList<String> assessmentPoint) {
        ArrayList<String> assessmentTopic = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            assessmentTopic.add(String.valueOf(i));
        }
        if (soundPoint.size() == 0 || assessmentPoint.size() == 0) {
            return;
        }
        String path = Environment.getExternalStorageDirectory().getPath()
                + "/" + context.getPackageName();
        File sdFile = new File(path);
        sdFile.mkdir();
        try {
            Date date = new Date();
            SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy/MM/dd");
            String dayFormatDate = dayFormat.format(date);
            File file = new File(sdFile, "record"  + ".csv");
            ArrayList<String> soundResult = pointAddTopic(soundPoint, soundTopic, true);
            ArrayList<String> assessmentResult = pointAddTopic(assessmentPoint, assessmentTopic, false);

            PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, true));
            if (isFileEmpty(file)) {
                printWriter.append("日期,用聲1,用聲2,用聲3,用聲4,用聲5,用聲6,用聲7,每週用聲總分,自我評估總分\n");
            }
            printWriter.append(dayFormatDate);
            ArrayList<String> arrayList = new ArrayList();
            for (int i = 0; i < 7; i++) {
                arrayList.add(i, "無");
            }
            for (int j = 0; j < soundTopic.size(); j++) {
                arrayList.set(Integer.parseInt(soundTopic.get(j)), soundPoint.get(j));
            }
            for (String member : arrayList)
                printWriter.append("," + member);
            int soundAllPoint = 0;
            int soundAllTopicPoint = 0;
            for (String menber : soundPoint) soundAllPoint += Integer.valueOf(menber);
            for (String menber : soundTopic) soundAllTopicPoint += Integer.valueOf(menber);
            try {
                printWriter.append("," + soundAllPoint + "(" + Math.round(soundAllPoint * 100 / soundAllTopicPoint) + "%)");
            } catch (ArithmeticException e) {
                printWriter.append("," + soundAllPoint + "(" + 0 + "%)");
            }
            int assessmentAllPoint = 0;
//            String assementTopicList = "";
//            for (String member : assessmentResult) assementTopicList += member;
            for (String menber : assessmentPoint) assessmentAllPoint += Integer.valueOf(menber);
            try {
                printWriter.append("," + assessmentAllPoint + "(" + Math.round(assessmentAllPoint * 100 / 40) + "%)");
            } catch (ArithmeticException e) {
                printWriter.append("," + assessmentAllPoint + "(" + 0 + "%)");
            }
            printWriter.append("\n");


//            printWriter.append(dayFormatDate);
//
//            printWriter.append("," + assementTopicList);
//            for (String menber : assessmentPoint) assessmentAllPoint += Integer.valueOf(menber);
//            try {
//                printWriter.append("," + assessmentAllPoint + "(" + Math.round(assessmentAllPoint * 100 / 40) + "%)");
//            } catch (ArithmeticException e) {
//                printWriter.append("," + assessmentAllPoint + "(" + 0 + "%)");
//            }
//            printWriter.append("\n");
            printWriter.flush();
            printWriter.close();
            writerCallBack.onWriteSuccessful(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("FileNotFoundException", e.toString());
            writerCallBack.onWriteFail();
        }
    }

    private ArrayList<String> pointAddTopic(ArrayList<String> point, ArrayList<String> topic, boolean isSoundResult) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < topic.size(); i++) {
            int topicNum = Integer.valueOf(topic.get(i)) + (isSoundResult ? 1 : 0);
            String temp = topicNum + ":" + point.get(i) + "。";
            result.add(temp);
        }
        return result;
    }

    public void setStartTime(Date date) {
        this.startDate = date;
    }

    public File getFile() {
        return file;
    }

    public void setWriterCallBack(WriterCallBack event) {
        this.writerCallBack = event;
    }

    private boolean isFileEmpty(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            return null==reader.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public interface WriterCallBack {
        void onWriteSuccessful(File file);

        void onWriteFail();
    }
}
