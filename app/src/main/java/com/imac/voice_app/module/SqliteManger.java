package com.imac.voice_app.module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by isa on 2016/10/6.
 */
public class SqliteManger {
    private SqliteHelper sqliteHelper;
    private Context context;

    public SqliteManger(Context context) {
        this.context = context;
        sqliteHelper = new SqliteHelper(context);
    }

    private SQLiteDatabase create() {
        SQLiteDatabase dataBase = sqliteHelper.getWritableDatabase();
        return dataBase;
    }

    public void writeSoundPoint(String[] value) {
        String[] key = {
                sqliteHelper.getAccount(),
                sqliteHelper.getSoundTopicPointKey(),
        };
        SQLiteDatabase dataBase = create();
        ContentValues values = new ContentValues();
        for (int i = 0; i < value.length; i++) {
            values.put(key[i], value[i]);
        }
        dataBase.insert(sqliteHelper.getSoundTableName(), null, values);
    }

    public void writeAssessmentPoint(String[] value) {
        String[] key = {
                sqliteHelper.getAccount(),
                sqliteHelper.getAssessmentTopicPointKey(),
        };
        SQLiteDatabase dataBase = create();
        ContentValues values = new ContentValues();
        for (int i = 0; i < value.length; i++) {
            values.put(key[i], value[i]);
        }
        dataBase.insert(sqliteHelper.getAssessmentTableName(), null, values);
    }

    public void selectSoundPoint() {
        SQLiteDatabase dataBase = create();
        String selectSql = "SELECT * FROM " + sqliteHelper.getSoundTableName();
        Cursor cursor = dataBase.rawQuery(selectSql, null);
        if (cursor.getCount()==0){
            return;
        }
        cursor.moveToFirst();
        do {
            Log.e("show soundpoint", cursor.getString(cursor.getColumnIndex(sqliteHelper.getAccount())));
            Log.e("show soundpoint", cursor.getString(cursor.getColumnIndex(sqliteHelper.getSoundTopicPointKey())));
            Log.e("show soundpoint", cursor.getString(cursor.getColumnIndex(sqliteHelper.getData())));
        } while (cursor.moveToNext());
        cursor.close();
        dataBase.close();
    }
    public void selectAssessmentPoint() {
        SQLiteDatabase dataBase = create();
        String selectSql = "SELECT * FROM " + sqliteHelper.getAssessmentTableName();
        Cursor cursor = dataBase.rawQuery(selectSql, null);
        if (cursor.getCount()==0){
            return;
        }
        cursor.moveToFirst();
        do {
            Log.e("show soundpoint", cursor.getString(cursor.getColumnIndex(sqliteHelper.getAccount())));
            Log.e("show soundpoint", cursor.getString(cursor.getColumnIndex(sqliteHelper.getAssessmentTopicPointKey())));
            Log.e("show soundpoint", cursor.getString(cursor.getColumnIndex(sqliteHelper.getData())));
        } while (cursor.moveToNext());
        cursor.close();
        dataBase.close();
    }
}
