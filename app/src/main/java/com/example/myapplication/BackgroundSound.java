package com.example.myapplication;

import android.animation.ValueAnimator;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.animation.LinearInterpolator;

public class BackgroundSound {
    int resId1;
    int resId2;
    int mediaOnPlay;
    float soundVolumn = 0.3f;
    MediaPlayer mediaPlayer1;
    MediaPlayer mediaPlayer2;
    MediaPlayer mediaPlayer3;
    MainActivity mainActivity;
    public BackgroundSound(int resId1, int resId2, MainActivity mainActivity){
        this.resId1 = resId1;
        this.resId2 = resId2;
        this.mainActivity = mainActivity;
        mediaPlayer1 = MediaPlayer.create(mainActivity,resId1);
        mediaPlayer1.setVolume(soundVolumn,soundVolumn);
        mediaPlayer2 = MediaPlayer.create(mainActivity,resId2);
        mediaPlayer2.setVolume(soundVolumn,soundVolumn);
        mediaPlayer3 = MediaPlayer.create(mainActivity,resId2);
        mediaPlayer3.setVolume(soundVolumn,soundVolumn);
        try {
            mediaPlayer1.setLooping(false);
            mediaPlayer2.setLooping(false);
            mediaPlayer3.setLooping(false);

            mediaPlayer1.setNextMediaPlayer(mediaPlayer2);
            mediaPlayer2.setNextMediaPlayer(mediaPlayer3);
            mediaPlayer1.start();
            mediaOnPlay = 1;
            Log.d("music","the first play");
            mediaPlayer1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaOnPlay = 2;
                    mediaPlayer1.reset();
                }
            });
            mediaPlayer2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaOnPlay = 3;
                    initMediaPlayer2();
                }
            });
            mediaPlayer3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaOnPlay = 2;
                    initMediaPlayer3();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy(){
        mediaPlayer1.release();
        mediaPlayer2.release();
        mediaPlayer3.release();
    }

    public void reset(){
        mediaPlayer1.reset();
        mediaPlayer2.reset();
        mediaPlayer3.reset();
    }

    public void endMusic(int duration){
        setFade(soundVolumn,0, duration);
    }

    private void setFade(float from, int to, int duration){
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(duration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float volumn = (float) animation.getAnimatedValue();
                if(((float)animation.getAnimatedValue()) <= 0.1f){
                    (mediaOnPlay == 1 ? mediaPlayer1: mediaOnPlay == 2? mediaPlayer2:mediaPlayer3).pause();
                }
                try{//mediaOnplay记录是哪个mediaplayer
                    (mediaOnPlay == 1 ? mediaPlayer1: mediaOnPlay == 2? mediaPlayer2:mediaPlayer3).setVolume(volumn,volumn);
                }catch(Exception e){
                    animation.cancel();
                }
            }
        });
        animator.start();
    }

    private void initMediaPlayer2(){
        mediaPlayer2.reset();
        mediaPlayer2 = MediaPlayer.create(mainActivity,resId2);
        mediaPlayer2.setVolume(soundVolumn,soundVolumn);
        mediaPlayer2.setLooping(false);
        mediaPlayer2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer3.setNextMediaPlayer(mediaPlayer2);
            }
        });
        mediaPlayer2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaOnPlay = 3;
                initMediaPlayer2();
            }
        });
        Log.d("music","the second has play");
    }

    private void initMediaPlayer3(){
        mediaPlayer3.reset();
        mediaPlayer3 = MediaPlayer.create(mainActivity,resId2);
        mediaPlayer3.setLooping(false);
        mediaPlayer3.setVolume(soundVolumn,soundVolumn);
        mediaPlayer3.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer2.setNextMediaPlayer(mediaPlayer3);
            }
        });
        mediaPlayer3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaOnPlay = 2;
                initMediaPlayer3();
            }
        });
        Log.d("music","the third has play");
    }

}
