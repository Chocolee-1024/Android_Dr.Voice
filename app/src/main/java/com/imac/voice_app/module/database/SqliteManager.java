package com.imac.voice_app.module.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.imac.voice_app.module.Preferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;

/**
 * Created by isa on 2016/10/6.
 */
public class SqliteManager {
    private SqliteHelper sqliteHelper;
    private static SqliteManager manger = null;
    private Context context;
    private Preferences preferences;

    private SqliteManager(Context context) {
        this.context = context;
        sqliteHelper = new SqliteHelper(context);
        preferences = new Preferences(context);
    }

    public static SqliteManager getIntence(Context context) {
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
        SQLiteDatabase dataBase = sqliteHelper.getWritableDatabase();
        return dataBase;
    }

    public void write(String[] value) {
        SQLiteDatabase dataBase = create();
        ContentValues values = new ContentValues();
        String[] key = {
                sqliteHelper.getSoundTopicPointKey(),
                sqliteHelper.getAssessmentTopicPointKey()
        };
        for (int i = 0; i < value.length; i++) {
            values.put(key[i], value[i]);
        }
        dataBase.insert(sqliteHelper.getTableName(), null, values);
    }

    public DataStructure[] getALlSqlData() {
        String selectSql;
        DataStructure dataStructure[] = null;
        ArrayList<DataStructure> DataObj = new ArrayList<>();
        SQLiteDatabase dataBase = create();
        selectSql = "SELECT * FROM " + sqliteHelper.getTableName();
        Cursor cursor = dataBase.rawQuery(selectSql, null);
        if (cursor.getCount() == 0) {
            return dataStructure = new DataStructure[0];
        }
        cursor.moveToFirst();
        do {
            DataStructure structure = new DataStructure();
            structure.setSoundTopicPoint(cursor.getString(cursor.getColumnIndex(sqliteHelper.getSoundTopicPointKey())));
            structure.setWeeklyTopicPoint(cursor.getString(cursor.getColumnIndex(sqliteHelper.getAssessmentTopicPointKey())));
            structure.setDate(cursor.getString(cursor.getColumnIndex(sqliteHelper.getDate())));
            DataObj.add(structure);

            Log.e("SoundTopicPointKey", "" + cursor.getString(cursor.getColumnIndex(sqliteHelper.getSoundTopicPointKey())));
            Log.e("AssessmentTopicPointKey", cursor.getString(cursor.getColumnIndex(sqliteHelper.getAssessmentTopicPointKey())));
            Log.e("getDate", cursor.getString(cursor.getColumnIndex(sqliteHelper.getDate())));
        }
        while (cursor.moveToNext());
        cursor.close();
        dataBase.close();
        dataStructure = new DataStructure[DataObj.size()];
        for (int i = 0; i < DataObj.size(); i++) {
            dataStructure[i] = DataObj.get(i);
        }
        return dataStructure;
    }

    public String[] selectMonth() {
        LinkedHashSet<String> linkHashSet = new LinkedHashSet<>();
        String[] result = null;
        SQLiteDatabase dataBase = create();
        String selectSql = "SELECT " + sqliteHelper.getDate() + " FROM " + sqliteHelper.getTableName() + " ORDER BY " + sqliteHelper.getDate() + " DESC";
        Cursor cursor = dataBase.rawQuery(selectSql, null);
        SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");
        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if (cursor.getCount() == 0) {
            return result = new String[0];
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
        }
        while (cursor.moveToNext());
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

    public class DataStructure {
        private String date = null;
        private String soundTopicPoint = "0";
        private String weeklyTopicPoint = null;

        public void setDate(String date) {
            this.date = date;
        }

        public void setSoundTopicPoint(String soundTopicPoint) {
            if (null != soundTopicPoint) this.soundTopicPoint = soundTopicPoint;
        }

        public void setWeeklyTopicPoint(String weeklyTopicPoint) {
            this.weeklyTopicPoint = weeklyTopicPoint;
        }

        public String getDate() {
            return date;
        }

        public String getSoundTopicPoint() {
            return soundTopicPoint;
        }

        public String getWeeklyTopicPoint() {
            return weeklyTopicPoint;
        }
    }
}
