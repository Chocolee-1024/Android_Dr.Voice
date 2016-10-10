package com.imac.voice_app.module;

import android.app.Activity;

/**
 * Created by isa on 2016/9/26.
 */
public class MediaPlayer {
    private android.media.MediaPlayer player;
    private Activity activity;

    public enum Status {
        PLAY,
        PAUSE,
        STOP
    }

    private Status status;

    public MediaPlayer(Activity activity, int id) {
        this.activity = activity;
        player = android.media.MediaPlayer.create(activity, id);
    }

    public void startPlay() {
        if (player != null) {
            player.start();
            status = Status.PLAY;
        }
    }

    public void pausePlay() {
        if (player != null && status == Status.PLAY) {
            player.pause();
            status = Status.PAUSE;
        }
    }

    public void stopPlay() {
        if (player != null && status == Status.PAUSE) {
            player.stop();
            player.release();
            status = Status.STOP;
        }
    }

    public Status getStatus() {
        return status;
    }
}
