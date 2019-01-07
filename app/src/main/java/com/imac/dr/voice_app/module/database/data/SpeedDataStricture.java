package com.imac.dr.voice_app.module.database.data;

import android.os.Parcel;
import android.os.Parcelable;

public class SpeedDataStricture implements Parcelable {
    private String startTime;
    private String endTime;
    private String speedCount;
    private String speed;
    private String record;

    public SpeedDataStricture() {
    }

    public SpeedDataStricture(Parcel in) {
        startTime = in.readString();
        endTime = in.readString();
        speedCount = in.readString();
        speed = in.readString();
        record = in.readString();
    }

    public static final Creator<SpeedDataStricture> CREATOR = new Creator<SpeedDataStricture>() {
        @Override
        public SpeedDataStricture createFromParcel(Parcel in) {
            return new SpeedDataStricture(in);
        }

        @Override
        public SpeedDataStricture[] newArray(int size) {
            return new SpeedDataStricture[size];
        }
    };

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getSpeedCount() {
        return speedCount;
    }

    public String getSpeed() {
        return speed;
    }

    public String getRecord() {
        return record;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setSpeedCount(String speedCount) {
        this.speedCount = speedCount;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(speedCount);
        dest.writeString(speed);
        dest.writeString(record);
    }
}