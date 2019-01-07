package com.imac.dr.voice_app.module;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;

import java.io.IOException;

/**
 * Created by isa on 2016/9/26.
 */
public class MediaPlayer {
    private android.media.MediaPlayer mMediaPlayer;
    private Activity activity;

    public enum Status {
        PLAY,
        PAUSE,
        STOP
    }

    private Status status;

    public MediaPlayer(Activity activity, int id) {
        this.activity = activity;
        mMediaPlayer = android.media.MediaPlayer.create(activity, id);
        mMediaPlayer.setLooping(false);
    }

    public void setCompleteListener(android.media.MediaPlayer.OnCompletionListener listener) {
        mMediaPlayer.setOnCompletionListener(listener);
    }


    public void startPlay() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            status = Status.PLAY;
        }
    }

    public void pausePlay() {
        if (mMediaPlayer != null && status == Status.PLAY) {
            mMediaPlayer.pause();
            status = Status.PAUSE;
        }
    }

    public void stopPlay() {
        if (mMediaPlayer != null && status == Status.PAUSE) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            status = Status.STOP;
        }
    }

    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    public Status getStatus() {
        return status;
    }

    public void setDataSource(int resid) {
        AssetFileDescriptor assetFileDescriptor = activity.getResources().openRawResourceFd(resid);
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getDeclaredLength());
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
