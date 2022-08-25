package com.imac.dr.voice_app.view.dailyexercise;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.core.FragmentLauncher;
import com.imac.dr.voice_app.module.CountSecond;
import com.imac.dr.voice_app.module.MediaPlayer;
import com.imac.dr.voice_app.module.Preferences;
import com.imac.dr.voice_app.util.dailyexercise.DailyExerciseFinishFragment;
import com.imac.dr.voice_app.util.dailyexercise.DailyExerciseSelectFragment;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isa on 2016/9/21.
 */
public class DailySelectInnerExerciseView implements android.media.MediaPlayer.OnCompletionListener {
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
    @BindArray(R.array.mu_raw_array)
    TypedArray muRawArray;
    @BindArray(R.array.her_raw_array)
    TypedArray herRawArray;
    @BindArray(R.array.relax_array)
    TypedArray relaxArray;

    private Activity activity;
    private int index;
    private Handler handler;
    private CountSecond countSecond;
    private CountSecond countFiveSecond;
    private MediaPlayer player;
    private TurnDataThread pictureRunnable;
    public static final String KEY_TOPIC_INDEX = "key_topic_index";
    private boolean isFiveSecCountDown = true;
    private int mCountTime = 60;
    private RelativeLayout counterContainer;
    private TurnDataThread textRunnable;
    private Preferences mPreferences;
    private GifDrawable mGifDrawable;
    private boolean isFirstIn = true;
    private boolean isComplete = false;
//    private int[] delaySec = {2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4};
        private int[] delaySec = {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};

    public DailySelectInnerExerciseView(Activity activity, View view, int index) {
        this.activity = activity;
        //存取你要做的Topic
        this.index = index;
        handler = new Handler();
        //倒數 監聽
        countSecond = new CountSecond(countEvent());
        //倒數 五秒監聽
        countFiveSecond = new CountSecond(countFiveSecondCallBack());
        mPreferences = new Preferences(activity);
        counterContainer = (RelativeLayout) activity.findViewById(R.id.counter_container);
        ButterKnife.bind(this, view);
        init();
        //5sec
        startCountDown();
    }

    private void init() {
        //判斷為第幾個Topic
        if (index == 0) {
            //mCountTime = 60*醫生設定的秒數
            mCountTime *= positionToTime(mPreferences.getTopicOnePosition());
            //這個Topic要做的事，顯示TextView
            dailyExerciseSelectedDescription.setVisibility(View.VISIBLE);
            //並載入該Topic的圖片
            Glide.with(activity)
                    .load(R.drawable.practice4_action_a_icon)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(dailyExerciseSelectedImage);
            //並撥放該Topic的提示音
            player = new MediaPlayer(activity, R.raw.practice_4);
        } else if (index == 1) {
            mCountTime *= mPreferences.getTopicTwoPosition();
            Glide.with(activity)
                    .load(R.drawable.breathing)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(mGifDrawableRequestListener)
                    .into(dailyExerciseSelectedImage);
            player = new MediaPlayer(activity, R.raw.practice_1);
        } else if (index == 2) {
            mCountTime *= positionToTime(mPreferences.getTopicThreePosition());
            Glide.with(activity)
                    .load(R.drawable.sing_in_water)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(mGifDrawableRequestListener)
                    .into(dailyExerciseSelectedImage);
            player = new MediaPlayer(activity, R.raw.practice_2);
        } else if (index == 3) {
            mCountTime *= positionToTime(mPreferences.getTopicFourPosition());
            Glide.with(activity)
                    .load(R.drawable.musicnotes)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(mGifDrawableRequestListener)
                    .into(dailyExerciseSelectedImage);
            player = new MediaPlayer(activity, R.raw.practice_3);
        } else if (index == 4) {
            //note mCountTime = 60*醫生設定的秒數
            mCountTime *= positionToTime(mPreferences.getTopicFivePosition());
            dailyExerciseSelectedImage.setVisibility(View.VISIBLE);
            Glide.with(activity)
                    .load(R.drawable.cat)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(dailyExerciseSelectedImage);
            //播放事前提醒
            player = new MediaPlayer(activity, R.raw.practice_5);
        } else {
            mCountTime *= positionToTime(mPreferences.getTopicSixPosition());
            dailyExerciseSelectedImage.setVisibility(View.VISIBLE);
            Glide.with(activity)
                    .load(R.drawable.breath)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(dailyExerciseSelectedImage);
            player = new MediaPlayer(activity, R.raw.practice_6);
        }
        //note 秒轉 分:秒
        dailyExerciseSelectedTime.setText(SecToMin(mCountTime));
        setFont();
        Log.e("note","333333333" );
    }

    private void setFont() {
    }

    private int positionToTime(int time) {
        switch (time) {
            case 1:
                return 1;
            case 2:
                return 3;
            case 3:
                return 5;
            default:
                return 1;
        }
    }

    private RequestListener<Integer, GifDrawable> mGifDrawableRequestListener = new RequestListener<Integer, GifDrawable>() {
        @Override
        public boolean onException(Exception e, Integer model, Target<GifDrawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(GifDrawable resource, Integer model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            mGifDrawable = resource;
            return false;
        }
    };

    public void startCount() {
        if (index == 0) pictureTurnPlay(); //note ->4 recycle
        else if (index == 4) textTurnPlay(index); //note ->12 recycle
        else if (index == 5) textTurnPlay(index); //note ->12 recycle

        countSecond.startCountWithCountDown(mCountTime);
    }

    private void pictureTurnPlay() {
        pictureRunnable = new TurnDataThread(
                activity
                , 4
        );
        pictureRunnable.setDataChangeEvent(onChangeEvent());
        pictureRunnable.setDelaySec(15);
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
                        if (4 == index) {
                            dailyExerciseSelectedImage.setImageResource(muImageArray.getResourceId(witchData, 0));
                            player.setDataSource(muRawArray.getResourceId(witchData, 0));
                        } else {
                            dailyExerciseSelectedImage.setImageResource(herArray.getResourceId(witchData, 0));
                            player.setDataSource(herRawArray.getResourceId(witchData, 0));
                        }
                        Log.d("note", "setDelaySec "+delaySec[witchData] + player.getDuration() / 1000);
                        textRunnable.setDelaySec(delaySec[witchData] + player.getDuration() / 1000);
                        player.startPlay();
                        isComplete = false;
                    }
                });
            }
        };
    }

    @OnClick(R.id.daily_exercise_selected_stop_button)
    public void onStopClick() {
        if (null != pictureRunnable) pictureRunnable.pause();
        if (null != textRunnable) textRunnable.pause();
        player.pausePlay();
        countSecond.pauseCount();
        if (null != mGifDrawable) mGifDrawable.stop();
        dailyExerciseSelectedPlayButton.setVisibility(View.VISIBLE);
        dailyExerciseSelectedStopButton.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.daily_exercise_selected_close_button)
    public void onCloseClick() {
        player.stopPlay();
        countSecond.stopCount();
        if (null != textRunnable) textRunnable.finish();
        if (null != pictureRunnable) pictureRunnable.finish();
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
        if (null != pictureRunnable) pictureRunnable.restart();
        if (null != textRunnable) textRunnable.restart();
        if (!isComplete) player.startPlay();
        countSecond.continueCount();
        if (null != mGifDrawable && !isFirstIn)
            mGifDrawable.start();
        dailyExerciseSelectedPlayButton.setVisibility(View.INVISIBLE);
        dailyExerciseSelectedStopButton.setVisibility(View.VISIBLE);
    }

    private CountSecond.countSecondCallBack countEvent() {
        return new CountSecond.countSecondCallBack() {
            @Override
            public void countPerSecond(int sec) {
                dailyExerciseSelectedTime.setText(SecToMin(sec));
                dailyExerciseSelectedTime.invalidate();
            }

            @Override
            public void finishCount() {
                if (null != pictureRunnable) pictureRunnable.pause();
                if (null != textRunnable) textRunnable.pause();
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
                //note 數完
                isFiveSecCountDown = false;
                dailyExerciseSelectedImage.setVisibility(View.VISIBLE);
                counterContainer.setVisibility(View.INVISIBLE);
                dailyExerciseSelectedDescription.setVisibility(View.INVISIBLE);
                //換畫面
                player.setCompleteListener(DailySelectInnerExerciseView.this);
                player.startPlay();
                isComplete = false;
                //note 播放事前音檔
                Toast.makeText(activity, activity.getString(R.string.daily_exercise_playing), Toast.LENGTH_LONG).show();
                if (null != mGifDrawable) mGifDrawable.stop();
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
                                player.setDataSource(relaxArray.getResourceId(witchData, 0));
                                break;
                            case 1:
                                dailyExerciseSelectedImage.setImageResource(R.drawable.practice4_action_b_icon);
                                dailyExerciseSelectedDescription.setText(R.string.daily_exercise_practice4_action_b);
                                player.setDataSource(relaxArray.getResourceId(witchData, 0));
                                break;
                            case 2:
                                dailyExerciseSelectedImage.setImageResource(R.drawable.practice4_action_c_icon);
                                dailyExerciseSelectedDescription.setText(R.string.daily_exercise_practice4_action_c);
                                player.setDataSource(relaxArray.getResourceId(witchData, 0));
                                break;
                            case 3:
                                dailyExerciseSelectedImage.setImageResource(R.drawable.practice4_action_d_icon);
                                dailyExerciseSelectedDescription.setText(R.string.daily_exercise_practice4_action_d);
                                player.setDataSource(relaxArray.getResourceId(witchData, 0));
                                break;
                        }
                        player.startPlay();
                        isComplete = false;
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

    public TurnDataThread getPictureRunnable() {
        return pictureRunnable;
    }

    public TurnDataThread getTextRunnable() {
        return textRunnable;
    }

    @Override
    public void onCompletion(android.media.MediaPlayer mp) {
        if (isFirstIn) {
            startCount();
            if (null != mGifDrawable)
                mGifDrawable.start();
            isFirstIn = false;
        }
        isComplete = true;
    }
}
