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
        sqliteHelper = new SqliteHelper(weakReference.get());
        preferences = new Preferences(weakReference.get());
    }

    public static SqliteManager getInstence(Context context) {
        if (manger == null) {
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
        WeeklyDataStructure weeklyDataStructure[];
        ArrayList<WeeklyDataStructure> dataObj = new ArrayList<>();
        SQLiteDatabase dataBase = create();
        selectSql = "SELECT * FROM " + sqliteHelper.getWeeklyTableName();
        Cursor cursor = dataBase.rawQuery(selectSql, null);
        if (cursor.getCount() == 0) {
            return new WeeklyDataStructure[0];
        }
        cursor.moveToFirst();
        do {
            WeeklyDataStructure structure = new WeeklyDataStructure();
            structure.setSoundTopic(cursor.getString(cursor.getColumnIndex(sqliteHelper.getSoundTopicKey())));
            structure.setSoundTopicPoint(cursor.getString(cursor.getColumnIndex(sqliteHelper.getSoundTopicPointKey())));
            structure.setWeeklyTopicPoint(cursor.getString(cursor.getColumnIndex(sqliteHelper.getAssessmentTopicPointKey())));
            structure.setDate(cursor.getString(cursor.getColumnIndex(sqliteHelper.getDate())));
            dataObj.add(structure);

            Log.e("SoundTopicKey", "" + cursor.getString(cursor.getColumnIndex(sqliteHelper.getSoundTopicKey())));
            Log.e("SoundTopicPointKey", "" + cursor.getString(cursor.getColumnIndex(sqliteHelper.getSoundTopicPointKey())));
            Log.e("AssessmentTopicPointKey", cursor.getString(cursor.getColumnIndex(sqliteHelper.getAssessmentTopicPointKey())));
            Log.e("getDate", cursor.getString(cursor.getColumnIndex(sqliteHelper.getDate())));
        } while (cursor.moveToNext());
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
