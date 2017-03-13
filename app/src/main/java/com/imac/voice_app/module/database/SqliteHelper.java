package com.imac.voice_app.module.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by isa on 2016/10/6.
 */
public class SqliteHelper extends SQLiteOpenHelper {
    private final String TABLE_NAME = "sound_table";
    private final String ASSESSMENT_TABLE_NAME = "assessment_table";
    private final static String DATABASE_NAME = "history.db";
    private final static int DATABASE_VERSION = 1;
    private final String account = "Account";
    private final String soundTopicPoint = "SoundTopicPoint";
    private final String assessmentTopicPoint = "AssessmentTopicPoint";
    private final String date = "Data";

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_SQL =
                "create table " + TABLE_NAME + " ("
                        + account + " TEXT ,"
                        + soundTopicPoint + " TEXT,"
                        + assessmentTopicPoint + " TEXT,"
                        + date + " DEFAULT (datetime('now','localtime'))"
                        + ");";
        sqLiteDatabase.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String DROP_SOUND_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_SOUND_TABLE);
        onCreate(sqLiteDatabase);
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getAccount() {
        return account;
    }

    public String getSoundTopicPointKey() {
        return soundTopicPoint;
    }

    public String getAssessmentTopicPointKey() {
        return assessmentTopicPoint;
    }

    public String getDate() {
        return date;
    }
}
