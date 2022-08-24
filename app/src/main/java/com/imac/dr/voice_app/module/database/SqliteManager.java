package com.imac.dr.voice_app.module.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.imac.dr.voice_app.module.Preferences;
import com.imac.dr.voice_app.module.database.data.SpeedDataStricture;
import com.imac.dr.voice_app.module.database.data.WeeklyDataStructure;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;

/**
 * Created by isa on 2016/10/6.
 */
public class SqliteManager {
    private SqliteHelper sqliteHelper;
    private static SqliteManager manger = null;
    private Preferences preferences;

    private SqliteManager(Context context) {
        WeakReference<Context> weakReference = new WeakReference<Context>(context);
        //建立SqliteHelper
        sqliteHelper = new SqliteHelper(weakReference.get());
        //建立Preferences
        preferences = new Preferences(weakReference.get());
    }

    public static SqliteManager getInstence(Context context) {
        //判斷有沒有建立過"manger"
        if (manger == null) {
            /** 當另一個地方再使用synchronized所包起來的物件時
                必須等正在使用的人，使用完才能用。*/
            synchronized (SqliteManager.class) {
                if (manger == null) {
                    manger = new SqliteManager(context);
                }
            }
        }
        return manger;
    }

    private SQLiteDatabase create() {
        return sqliteHelper.getWritableDatabase();
    }

    public void writeWeeklyData(String[] value) {
        SQLiteDatabase dataBase = create();
        ContentValues values = new ContentValues();
        String[] key = {
                sqliteHelper.getSoundTopicKey(),
                sqliteHelper.getSoundTopicPointKey(),
                sqliteHelper.getAssessmentTopicPointKey()
        };
        for (int i = 0; i < value.length; i++) {
            values.put(key[i], value[i]);
        }
        dataBase.insert(sqliteHelper.getWeeklyTableName(), null, values);
    }

    public void writeSpeedData(String[] value) {
        SQLiteDatabase dataBase = create();
        ContentValues values = new ContentValues();
        String[] key = {
                sqliteHelper.getStartTimeKey(),
                sqliteHelper.getEndTimeKey(),
                sqliteHelper.getSpeedCountKey(),
                sqliteHelper.getSpeedKey(),
                sqliteHelper.getRecordTimeKey()
        };
        for (int i = 0; i < value.length; i++) {
            values.put(key[i], value[i]);
        }
        dataBase.insert(sqliteHelper.getSpeedTableName(), null, values);
    }

    public WeeklyDataStructure[] getWeeklyTableALlSqlData() {
        String selectSql;
        //每周練習的資料表
        WeeklyDataStructure weeklyDataStructure[];
        ArrayList<WeeklyDataStructure> dataObj = new ArrayList<>();
        //建立dataBase
        SQLiteDatabase dataBase = create();
        //拿取每周練習SQL的表名稱
        selectSql = "SELECT * FROM " + sqliteHelper.getWeeklyTableName();
        //創建指標-----------------------放入你要使用的資料
        Cursor cursor = dataBase.rawQuery(selectSql, null);
        //判斷有沒有資料(getCount為返回行數)
        if (cursor.getCount() == 0) {
            //新增一行WeeklyDataStructure
            return new WeeklyDataStructure[0];
        }
        //使cursor正確指向資料表第一個位置
        cursor.moveToFirst();
        //(因為已經指向資料表第一個位置，所以從第一個開始抓)
        do {
            WeeklyDataStructure structure = new WeeklyDataStructure();
            //拿取資料庫裡的SoundTopic
            structure.setSoundTopic(cursor.getString(cursor.getColumnIndex(sqliteHelper.getSoundTopicKey())));
            //拿取資料庫裡的SoundTopicPoint
            structure.setSoundTopicPoint(cursor.getString(cursor.getColumnIndex(sqliteHelper.getSoundTopicPointKey())));
            //拿取資料庫裡的WeeklyTopicPoint
            structure.setWeeklyTopicPoint(cursor.getString(cursor.getColumnIndex(sqliteHelper.getAssessmentTopicPointKey())));
            //拿取資料庫裡的Date
            structure.setDate(cursor.getString(cursor.getColumnIndex(sqliteHelper.getDate())));
            //放入List
            dataObj.add(structure);

            Log.e("SoundTopicKey", "" + cursor.getString(cursor.getColumnIndex(sqliteHelper.getSoundTopicKey())));
            Log.e("SoundTopicPointKey", "" + cursor.getString(cursor.getColumnIndex(sqliteHelper.getSoundTopicPointKey())));
            Log.e("AssessmentTopicPointKey", cursor.getString(cursor.getColumnIndex(sqliteHelper.getAssessmentTopicPointKey())));
            Log.e("getDate", cursor.getString(cursor.getColumnIndex(sqliteHelper.getDate())));
          //直到沒有下一行
        } while (cursor.moveToNext());
        //關掉cursor和dataBase
        cursor.close();
        dataBase.close();
        weeklyDataStructure = new WeeklyDataStructure[dataObj.size()];
        for (int i = 0; i < dataObj.size(); i++) {
            weeklyDataStructure[i] = dataObj.get(i);
        }
        return weeklyDataStructure;
    }

    public SpeedDataStricture[] getSpeedTableALlSqlData() {
        String selectSql;
        SpeedDataStricture speedDataStrictures[];
        ArrayList<SpeedDataStricture> dataObj = new ArrayList<>();
        SQLiteDatabase dataBase = create();
        selectSql = "SELECT * FROM " + sqliteHelper.getSpeedTableName();
        Cursor cursor = dataBase.rawQuery(selectSql, null);
        if (cursor.getCount() == 0) {
            return new SpeedDataStricture[0];
        }
        cursor.moveToFirst();
        do {
            SpeedDataStricture structure = new SpeedDataStricture();
            structure.setStartTime(cursor.getString(cursor.getColumnIndex(sqliteHelper.getStartTimeKey())));
            structure.setEndTime(cursor.getString(cursor.getColumnIndex(sqliteHelper.getEndTimeKey())));
            structure.setSpeedCount(cursor.getString(cursor.getColumnIndex(sqliteHelper.getSpeedCountKey())));
            structure.setSpeed(cursor.getString(cursor.getColumnIndex(sqliteHelper.getSpeedKey())));
            structure.setRecord(cursor.getString(cursor.getColumnIndex(sqliteHelper.getRecordTimeKey())));
            dataObj.add(structure);

            Log.e(sqliteHelper.getStartTimeKey(), "" + cursor.getString(cursor.getColumnIndex(sqliteHelper.getStartTimeKey())));
            Log.e(sqliteHelper.getEndTimeKey(), "" + cursor.getString(cursor.getColumnIndex(sqliteHelper.getEndTimeKey())));
            Log.e(sqliteHelper.getSpeedCountKey(), "" + cursor.getString(cursor.getColumnIndex(sqliteHelper.getSpeedCountKey())));
            Log.e(sqliteHelper.getSpeedKey(), "" + cursor.getString(cursor.getColumnIndex(sqliteHelper.getSpeedKey())));
            Log.e(sqliteHelper.getRecordTimeKey(), "" + cursor.getString(cursor.getColumnIndex(sqliteHelper.getRecordTimeKey())));

        } while (cursor.moveToNext());
        cursor.close();
        dataBase.close();
        speedDataStrictures = new SpeedDataStricture[dataObj.size()];
        for (int i = 0; i < dataObj.size(); i++) {
            speedDataStrictures[i] = dataObj.get(i);
        }
        return speedDataStrictures;
    }

    public String[] selectWeeklyTableMonth() {
        LinkedHashSet<String> linkHashSet = new LinkedHashSet<>();
        String[] result;
        SQLiteDatabase dataBase = create();
        String selectSql = "SELECT " + sqliteHelper.getDate() + " FROM " + sqliteHelper.getWeeklyTableName() + " ORDER BY " + sqliteHelper.getDate() + " DESC";
        Cursor cursor = dataBase.rawQuery(selectSql, null);
        SimpleDateFormat format = new SimpleDateFormat("MM/yyyy", Locale.TAIWAN);
        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.TAIWAN);
        if (cursor.getCount() == 0) {
            return new String[0];
        }
        cursor.moveToFirst();
        do {
            String sqlDate = cursor.getString(cursor.getColumnIndex(sqliteHelper.getDate()));
            Log.e("sqlDate", selectSql);
            Date date = null;
            try {
                date = parse.parse(sqlDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formatDate = format.format(date);
            Log.e("formatDate", formatDate);
            linkHashSet.add(formatDate);
        } while (cursor.moveToNext());
        cursor.close();
        dataBase.close();
        result = toArray(linkHashSet);
        return result;
    }

    private String[] toArray(Collection<String> input) {
        String[] result = new String[input.size()];
        for (int i = 0; i < input.size(); i++) {
            result[i] = (String) input.toArray()[i];
        }
        return result;
    }
}
