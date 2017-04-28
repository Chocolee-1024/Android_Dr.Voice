package com.imac.voice_app.module.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by isa on 2016/10/6.
 */
public class SqliteHelper extends SQLiteOpenHelper {
    private final String TABLE_NAME = "sound_table";
    private final String SPEED_TABLE_NAME = "speed_table";
    private final String ASSESSMENT_TABLE_NAME = "assessment_table";
    private final static String DATABASE_NAME = "history.db";
    private final static int DATABASE_VERSION = 1;
    private final String account = "Account";
    private final String soundTopicPoint = "SoundTopicPoint";
    private final String soundTopic= "SoundTopic";
    private final String assessmentTopicPoint = "AssessmentTopicPoint";
    private final String date = "Data";
    private final String startTime = "StartTime";
    private final String endTime = "EndTime";
    private final String speedCount = "SpeedCount";
    private final String speed = "Speed";
    private final String recordTime = "RecordTime";

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_SQL =
                "create table " + TABLE_NAME + " ("
                        + soundTopic + " TEXT,"
                        + soundTopicPoint + " TEXT,"
                        + assessmentTopicPoint + " TEXT,"
                        + date + " DEFAULT (datetime('now','localtime'))"
                        + ");";

        String CREATE_SPEED_TABLE_SQL =
                "create table " + SPEED_TABLE_NAME + " ("
                        + startTime + " TEXT, "
                        + endTime + " TEXT, "
                        + speedCount + " TEXT,"
                        + speed + " TEXT,"
                        + recordTime + " TEXT "
                        + ");";

        sqLiteDatabase.execSQL(CREATE_TABLE_SQL);

        sqLiteDatabase.execSQL(CREATE_SPEED_TABLE_SQL);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String DROP_SOUND_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_SOUND_TABLE);
        onCreate(sqLiteDatabase);
    }

    public String getWeeklyTableName() {
        return TABLE_NAME;
    }

    public String getSpeedTableName() {
        return SPEED_TABLE_NAME;
    }

    public String getAccount() {
        return account;
    }

    public String getSoundTopicKey() {
        return soundTopic;
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

    public String getStartTimeKey() {
        return startTime;
    }

    public String getEndTimeKey() {
        return endTime;
    }

    public String getSpeedCountKey() {
        return speedCount;
    }

    public String getSpeedKey() {
        return speed;
    }

    public String getRecordTimeKey() {
        return recordTime;
    }
}
