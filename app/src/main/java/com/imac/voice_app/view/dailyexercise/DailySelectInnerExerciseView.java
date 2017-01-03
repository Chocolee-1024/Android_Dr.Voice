package com.imac.voice_app.view.dailyexercise;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.imac.voice_app.R;
import com.imac.voice_app.core.FragmentLauncher;
import com.imac.voice_app.module.CountSecond;
import com.imac.voice_app.module.MediaPlayer;
import com.imac.voice_app.util.dailyexercise.DailyExerciseFinishFragment;
import com.imac.voice_app.util.dailyexercise.DailyExerciseSelectFragment;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isa on 2016/9/21.
 */
public class DailySelectInnerExerciseView {
    @BindView(R.id.daily_exercise_selected_time)
    TextView dailyExerciseSelectedTime;
    @BindView(R.id.daily_exercise_selected_image)
    ImageView dailyExerciseSelectedImage;
    @BindView(R.id.daily_exercise_selected_close_button)
    ImageView dailyExerciseSelectedCloseButton;
    @BindView(R.id.daily_exercise_selected_play_button)
    ImageView dailyExerciseSelectedPlayButton;
    @BindView(R.id.daily_exercise_selected_stop_button)
    ImageView dailyExerciseSelectedStopButton;
    @BindView(R.id.daily_exercise_selected_description)
    TextView dailyExerciseSelectedDescription;
    @BindArray(R.array.soft_attack)
    String[] softAttack;
    @BindArray(R.array.pre_resonance)
    String[] preResonance;
    @BindArray(R.array.mu_image_array)
    TypedArray muImageArray;
    @BindArray(R.array.her_array)
    TypedArray herArray;
    private Activity activity;
    private int index;
    private Handler handler;
    private CountSecond countSecond;
    private CountSecond countFiveSecond;
    private MediaPlayer player;
    private TurnDataThread pictureRunnable;
    public static final String KEY_TOPIC_INDEX = "key_topic_index";
    private boolean isFiveSecCountDown = true;
    private static final int COUNT_TIME = 120;
    private RelativeLayout counterContainer;
    private TurnDataThread textRunnable;

    public DailySelectInnerExerciseView(Activity activity, View view, int index) {
        this.activity = activity;
        this.index = index;
        handler = new Handler();
        countSecond = new CountSecond(countEvent());
        countFiveSecond = new CountSecond(countFiveSecondCallBack());
        counterContainer = (RelativeLayout) activity.findViewById(R.id.counter_container);
        ButterKnife.bind(this, view);
        init();
        startCountDown();
    }

    private void init() {
        if (index == 0) {
            dailyExerciseSelectedDescription.setVisibility(View.VISIBLE);
            player = new MediaPlayer(activity, R.raw.practice_4);
        } else if (index == 1) {
            Glide.with(activity)
                    .load(R.drawable.breathing)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(dailyExerciseSelectedImage);
            player = new MediaPlayer(activity, R.raw.practice_1);
        } else if (index == 2) {
            Glide.with(activity)
                    .load(R.drawable.sing_in_water)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(dailyExerciseSelectedImage);
            player = new MediaPlayer(activity, R.raw.practice_2);

        } else if (index == 3) {
            Glide.with(activity)
                    .load(R.drawable.musicnotes)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(dailyExerciseSelectedImage);
            player = new MediaPlayer(activity, R.raw.practice_3);
        } else if (index == 4) {
            dailyExerciseSelectedImage.setVisibility(View.VISIBLE);
            player = new MediaPlayer(activity, R.raw.practice_5);
        } else {
            dailyExerciseSelectedImage.setVisibility(View.VISIBLE);
            player = new MediaPlayer(activity, R.raw.practice_6);
        }
        setFont();
    }

    private void setFont() {
    }

    public void startCount() {
        if (index == 0) pictureTurnPlay();
        else if (index == 4) textTurnPlay(index);
        else if (index == 5) textTurnPlay(index);

        countSecond.startCountWithCountDown(DailySelectInnerExerciseView.COUNT_TIME);
    }

    private void pictureTurnPlay() {
        pictureRunnable = new TurnDataThread(
                activity
                , 4
        );
        pictureRunnable.setDataChangeEvent(onChangeEvent());
        pictureRunnable.start();
    }

    private void textTurnPlay(int index) {
        textRunnable = new TurnDataThread(
                activity
                , 12
        );
        textRunnable.setDataChangeEvent(onTextChange(index));
        textRunnable.start();
    }

    private TurnDataThread.DataChangeEvent onTextChange(final int index) {
        return new TurnDataThread.DataChangeEvent() {
            @Override
            public void onDataChangeEvent(final int witchData) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (4 == index)
                            dailyExerciseSelectedImage.setImageResource(muImageArray.getResourceId(witchData, 0));
                        else
                            dailyExerciseSelectedImage.setImageResource(herArray.getResourceId(witchData, 0));
                    }
                });
            }
        };
    }

    @OnClick(R.id.daily_exercise_selected_stop_button)
    public void onStopClick() {
        player.pausePlay();
        countSecond.pauseCount();
        dailyExerciseSelectedPlayButton.setVisibility(View.VISIBLE);
        dailyExerciseSelectedStopButton.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.daily_exercise_selected_close_button)
    public void onCloseClick() {
        player.stopPlay();
        countSecond.stopCount();
        change();
    }

    private String SecToMin(int inputSec) {
        String min = String.valueOf(inputSec / 60);
        String sec = String.valueOf(inputSec % 60);
        if (inputSec % 60 < 10) {
            sec = "0" + sec;
        }
        return "0" + min + ":" + sec;

    }

    @OnClick(R.id.daily_exercise_selected_play_button)
    public void onPlayClick() {
        player.startPlay();
        countSecond.continueCount();
        dailyExerciseSelectedPlayButton.setVisibility(View.INVISIBLE);
        dailyExerciseSelectedStopButton.setVisibility(View.VISIBLE);
    }

    private CountSecond.countSecondCallBack countEvent() {
        return new CountSecond.countSecondCallBack() {
            @Override
            public void countPerSecond(int sec) {
                player.startPlay();
                dailyExerciseSelectedTime.setText(SecToMin(sec));
                dailyExerciseSelectedTime.invalidate();
            }

            @Override
            public void finishCount() {
                Bundle bundle = new Bundle();
                bundle.putInt(KEY_TOPIC_INDEX, index);
                LayoutInflater inflater = LayoutInflater.from(activity);
                View view = inflater.inflate(R.layout.activity_daily_exercise, null);
                FragmentLauncher.change(
                        activity,
                        view.findViewById(R.id.daily_exercise_container).getId(),
                        bundle,
                        new DailyExerciseFinishFragment().getClass().getName()
                );
            }
        };
    }

    private CountSecond.countSecondCallBack countFiveSecondCallBack() {
        return new CountSecond.countSecondCallBack() {
            @Override
            public void countPerSecond(int sec) {
                TextView counter = (TextView) activity.findViewById(R.id.counter);
                counter.setText(String.valueOf(sec));
                counter.invalidate();
            }

            @Override
            public void finishCount() {
                isFiveSecCountDown = false;
                dailyExerciseSelectedImage.setVisibility(View.VISIBLE);
//        dailyExerciseSelectedText.setVisibility(View.INVISIBLE);
                counterContainer.setVisibility(View.INVISIBLE);
                dailyExerciseSelectedDescription.setVisibility(View.INVISIBLE);
                startCount();
            }
        };
    }


    public void startCountDown() {
        counterContainer.setVisibility(View.VISIBLE);
        countFiveSecond.startCountWithCountDown(5);
    }

    private TurnDataThread.DataChangeEvent onChangeEvent() {
        return new TurnDataThread.DataChangeEvent() {
            @Override
            public void onDataChangeEvent(final int witchData) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        switch (witchData) {
                            case 0:
                                dailyExerciseSelectedImage.setImageResource(R.drawable.practice4_action_a_icon);
                                dailyExerciseSelectedDescription.setText(R.string.daily_exercise_practice4_action_a);
                                break;
                            case 1:
                                dailyExerciseSelectedImage.setImageResource(R.drawable.practice4_action_b_icon);
                                dailyExerciseSelectedDescription.setText(R.string.daily_exercise_practice4_action_b);
                                break;
                            case 2:
                                dailyExerciseSelectedImage.setImageResource(R.drawable.practice4_action_c_icon);
                                dailyExerciseSelectedDescription.setText(R.string.daily_exercise_practice4_action_c);
                                break;
                            case 3:
                                dailyExerciseSelectedImage.setImageResource(R.drawable.practice4_action_d_icon);
                                dailyExerciseSelectedDescription.setText(R.string.daily_exercise_practice4_action_d);
                                break;
                        }
                    }
                });
            }
        };
    }

    private void change() {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.activity_daily_exercise, null);
        FragmentLauncher.change(
                activity,
                view.findViewById(R.id.daily_exercise_container).getId(),
                null,
                new DailyExerciseSelectFragment().getClass().getName()
        );
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public CountSecond getCounter() {
        return countSecond;
    }

    public CountSecond getCounterFiveSec() {
        return countFiveSecond;
    }

    public boolean isFiveSecCountDown() {
        return isFiveSecCountDown;
    }
}
