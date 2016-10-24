package com.imac.voice_app.module.database;

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

    public void write(String[] value) {
        SQLiteDatabase dataBase = create();
        ContentValues values = new ContentValues();
        String[] key = {
                sqliteHelper.getAccount(),
                sqliteHelper.getSoundTopicPointKey(),
                sqliteHelper.getAssessmentTopicPointKey()
        };
        for (int i = 0; i < value.length; i++) {
            values.put(key[i], value[i]);
        }
        dataBase.insert(sqliteHelper.getTableName(), null, values);
    }

    public void select() {
        String selectSql;
        SQLiteDatabase dataBase = create();
        selectSql = "SELECT * FROM " + sqliteHelper.getTableName();
        Cursor cursor = dataBase.rawQuery(selectSql, null);
        if (cursor.getCount() == 0) {
            return;
        }
        cursor.moveToFirst();
        do {
            Log.e("getAccount", cursor.getString(cursor.getColumnIndex(sqliteHelper.getAccount())));
            Log.e("SoundTopicPointKey", cursor.getString(cursor.getColumnIndex(sqliteHelper.getSoundTopicPointKey())));
            Log.e("AssessmentTopicPointKey", cursor.getString(cursor.getColumnIndex(sqliteHelper.getAssessmentTopicPointKey())));
            Log.e("getData", cursor.getString(cursor.getColumnIndex(sqliteHelper.getData())));
        }
        while (cursor.moveToNext());
        cursor.close();
        dataBase.close();
    }
}
