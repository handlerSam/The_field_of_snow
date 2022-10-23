package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GalFragment extends Fragment {
    ArrayList<Dialogue> dialogues;
    ArrayList<ImageView> imageViewList = new ArrayList<>();
    ImageView galBackground;
    TextView galMainText;
    TextView galCharacterText;
    TextView galSkip;
    TextView galAuto;
    ImageView galTouch;
    Timer timer;
    Context MAINCONTEXT;
    public int dialogueIndex = 0;

    public static final int TEXTQUICK = 70;
    public static final int TEXTMODERATE = 100;
    public static final int TEXTSLOW = 120;


    public static final int IMAGEQUICK = 300;
    public static final int IMAGEMODERATE = 900;
    public static final int IMAGESLOW = 1400;
    public static final int IMAGEVERYSLOW = 3000;

    boolean[] imageViewOnShow = new boolean[3];
    boolean backgroundOnShow = false;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case 1:
                    if(tempNumber == (tempString.length()-1) && timer != null){
                        timer.cancel();
                        timer = null;
                    }else if(tempNumber < (tempString.length()-1)){
                        tempNumber++;
                    }
                    galMainText.setText(tempString.substring(0,tempNumber+1));
                    break;
                default:
            }
        }
    };

    String tempString = "";//记录string,用于handler和timer之间交换数据；
    int tempNumber = 0;//记录当前显示到第几个字符
    public GalFragment(Context context, ArrayList<Dialogue> dialogues){
        this.dialogues = dialogues;
        this.MAINCONTEXT = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.gal_layout,container,false);
        initFind(v);
        initSet();
        return v;
    }

    private void initFind(View v){
        galBackground = (ImageView) v.findViewById(R.id.galBackground);
        galMainText = (TextView) v.findViewById(R.id.galMainText);
        galCharacterText = (TextView) v.findViewById(R.id.galCharacterText);
        galSkip = (TextView) v.findViewById(R.id.galSkip);
        galAuto = (TextView) v.findViewById(R.id.galAuto);
        galTouch = (ImageView) v.findViewById(R.id.galTouch);
        ImageView i = (ImageView) v.findViewById(R.id.galCharacterImageLeft);
        imageViewList.add(i);
        i = (ImageView) v.findViewById(R.id.galCharacterImageMiddle);
        imageViewList.add(i);
        i = (ImageView) v.findViewById(R.id.galCharacterImageRight);
        imageViewList.add(i);
    }

    private void initSet(){
        dialogueIndex = 0;
        for(int i = 0; i < 3; i++){
            imageViewOnShow[i] = false;
        }
        nextText();
        galTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tempNumber != (tempString.length()-1)){
                    //情况1：在文字没有播放完的时候点击
                    tempNumber = tempString.length()-1;
                    Message m = new Message();
                    m.what = 1;
                    handler.sendMessage(m);
                }else{
                    //情况2：在文字显示完的时候点击
                    if(dialogues.size() - dialogueIndex >= 1){
                        nextText();
                    }else{
                        //结束这个gal场景
                        if(((MainActivity)MAINCONTEXT).backgroundSound != null){
                            ((MainActivity)MAINCONTEXT).backgroundSound.endMusic(1500);
                        }
                        ((MainActivity)MAINCONTEXT).setFragment(MainActivity.MAINMANU);
                    }
                }
            }
        });
    }

    private void nextText(){
        Dialogue d = dialogues.get(dialogueIndex);
        dialogueIndex++;
        galCharacterText.setText(d.character);
        setText(d.text,d.textSpeed);
        //调整背景音乐
        if(d.musicChange){
            if(!d.isMusicNull){
                if(((MainActivity)MAINCONTEXT).backgroundSound != null){
                    ((MainActivity)MAINCONTEXT).backgroundSound.destroy();
                    ((MainActivity)MAINCONTEXT).backgroundSound = null;
                }
                ((MainActivity)MAINCONTEXT).backgroundSound = new BackgroundSound(d.musicIntro,d.musicLoop,((MainActivity)MAINCONTEXT));
            }else{
                if(((MainActivity)MAINCONTEXT).backgroundSound != null){
                    ((MainActivity)MAINCONTEXT).backgroundSound.endMusic(500);
                    ((MainActivity)MAINCONTEXT).backgroundSound = null;
                }
            }
        }

        //替换角色立绘
        for(int i = 0; i < 3; i++){
            if(d.characterImageChange[i]){
                if(imageViewOnShow[i]){
                    if(d.isCharacterImageNull[i]){
                        final int j = i;
                        Animation.AnimationListener listener = new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                imageViewList.get(j).setVisibility(View.INVISIBLE);
                            }
                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        };
                        imageViewOnShow[j] = false;
                        startAnimation(imageViewList.get(i),1, listener, d.imageSpeed);
                    }else{
                        imageViewList.get(i).setImageResource(d.characterImage[i]);
                    }
                }else{
                    if(!d.isCharacterImageNull[i]){
                        final int j = i;
                        imageViewList.get(j).setVisibility(View.VISIBLE);
                        imageViewList.get(i).setImageResource(d.characterImage[i]);
                        imageViewOnShow[j] = true;
                        startAnimation(imageViewList.get(i),0, null , d.imageSpeed);
                    }
                }

            }
        }

        //替换背景
        if(d.isBackgroundChange){
            if(backgroundOnShow){
                if(d.isBackgroundNull){
                    Animation.AnimationListener listener = new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            galBackground.setVisibility(View.INVISIBLE);
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    };
                    backgroundOnShow = false;
                    startAnimation(galBackground,1, listener, d.imageSpeed);
                }else{
                    galBackground.setBackgroundResource(d.backgroundImage);
                }
            }else{
                if(!d.isBackgroundNull){
                    galBackground.setVisibility(View.VISIBLE);
                    backgroundOnShow = true;
                    galBackground.setBackgroundResource(d.backgroundImage);
                    startAnimation(galBackground,0, null, d.imageSpeed);
                }
            }
        }
        setText(d.text,d.textSpeed);
    }

    public void startAnimation(View v, int start, Animation.AnimationListener listener, int speed){
        Animation alphaAnimation = new AlphaAnimation(start, 1-start);
        alphaAnimation.setDuration(speed);
        if(listener != null) alphaAnimation.setAnimationListener(listener);
        v.startAnimation(alphaAnimation);
    }

    public void setText(String text, int speed){
        tempString = text;
        tempNumber = 0;
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Message m = new Message();
                m.what = 1;
                handler.sendMessage(m);
            }
        };
        timer.schedule(task,0,speed);
    }
}

class Dialogue{
    String text;
    String character;
    int textSpeed = GalFragment.TEXTSLOW;
    int imageSpeed = GalFragment.IMAGESLOW;
    int[] characterImage = new int[3];
    boolean[] isCharacterImageNull = new boolean[3];
    boolean[] characterImageChange = new boolean[3];
    boolean musicChange = false;
    boolean isMusicNull = true;
    int musicIntro;
    int musicLoop;

    boolean isBackgroundChange = false;
    boolean isBackgroundNull = true;
    int backgroundImage;

    public Dialogue(String text, String character){
        this.text = text;
        this.character = character;
        for(int i = 0; i < 3; i++){
            characterImageChange[i] = false;
            isCharacterImageNull[i] = true;
        }
    }

    public void setCharacterImage(int position, int resourceId){
        characterImage[position] = resourceId;
        characterImageChange[position] = true;
        isCharacterImageNull[position] = false;
    }

    public void setCharacterImageNull(int position){
        characterImageChange[position] = true;
        isCharacterImageNull[position] = true;
    }

    public void setMusic(int startResourceId, int loopResourceId){
        musicIntro = startResourceId;
        musicLoop = loopResourceId;
        musicChange = true;
    }

    public void setMusicNull(){
        musicChange = true;
        isMusicNull = true;
    }

    public void setBackgroundChange(int resourceId){
        isBackgroundChange = true;
        isBackgroundNull = false;
        backgroundImage = resourceId;
    }

    public void setBackgroundNull(){
        isBackgroundChange = true;
        isBackgroundNull = true;
    }
}


