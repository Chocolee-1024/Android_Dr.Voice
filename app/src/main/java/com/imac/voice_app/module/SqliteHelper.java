package com.imac.voice_app.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by isa on 2016/10/6.
 */
public class SqliteHelper extends SQLiteOpenHelper {
    private final String SOUND_TABLE_NAME = "sound_table";
    private final String ASSESSMENT_TABLE_NAME = "assessment_table";
    private final static String DATABASE_NAME = "history.db";
    private final static int DATABASE_VERSION = 1;
    private final String account = "Account";
    private final String soundTopicPoint = "SoundTopicPoint";
    private final String assessmentTopicPoint = "AssessmentTopicPoint";
    private final String data = "Data";

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SOUND_DATABASE_SQL =
                "create table " + SOUND_TABLE_NAME + " ("
                        + account + " TEXT PRIMARY KEY,"
                        + soundTopicPoint + " TEXT,"
                        + data + " DEFAULT (datetime('now','localtime'))"
                        + ");";
        String ASSESSMENT_DATABASE_SQL =
                "create table " + ASSESSMENT_TABLE_NAME + " ("
                        + account + " TEXT PRIMARY KEY,"
                        + assessmentTopicPoint + " TEXT,"
                        + data + " DEFAULT (datetime('now','localtime'))"
                        + ");";
        sqLiteDatabase.execSQL(SOUND_DATABASE_SQL);
        sqLiteDatabase.execSQL(ASSESSMENT_DATABASE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String DROP_SOUND_TABLE = "DROP TABLE IF EXISTS " + SOUND_TABLE_NAME;
        final String DROP_ASSESSMENT_TABLE = "DROP TABLE IF EXISTS " + ASSESSMENT_TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_SOUND_TABLE);
        sqLiteDatabase.execSQL(DROP_ASSESSMENT_TABLE);
        onCreate(sqLiteDatabase);
    }

    public String getSoundTableName() {
        return SOUND_TABLE_NAME;
    }
    public String getAssessmentTableName() {
        return ASSESSMENT_TABLE_NAME;
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

    public String getData() {
        return data;
    }
}
