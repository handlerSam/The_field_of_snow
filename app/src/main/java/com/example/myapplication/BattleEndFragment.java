package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class BattleEndFragment extends Fragment {
    String title;
    String subtitle;
    int star;
    ArrayList<Item> itemList;
    ImageView battleEndCharacter;
    TextView battleEndTitle;
    TextView battleEndSubtitle;
    TextView battleEndDial;
    ImageView battleEndStar1;
    ImageView battleEndStar2;
    ImageView battleEndStar3;
    LinearLayout battleEndStateColumn;
    LinearLayout battleEndItemColumn;
    Context MAINCONTEXT;
    LinearLayout battleEndTouchBoard;
    public BattleEndFragment(Operation operation) {
        super();
        this.title = operation.title;
        this.subtitle = operation.subTitle;
        this.star = operation.star;
        this.MAINCONTEXT = operation.context;
        itemList = operation.operation.getOutcome();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.battle_end_fragment,container,false);
        battleEndCharacter = (ImageView) v.findViewById(R.id.battleEndCharacter);
        battleEndTitle = (TextView) v.findViewById(R.id.battleEndTitle);
        battleEndSubtitle = (TextView) v.findViewById(R.id.battleEndSubtitle);
        battleEndStar1 = (ImageView) v.findViewById(R.id.battleEndStar1);
        battleEndStar2 = (ImageView) v.findViewById(R.id.battleEndStar2);
        battleEndStar3 = (ImageView) v.findViewById(R.id.battleEndStar3);
        battleEndStateColumn = (LinearLayout) v.findViewById(R.id.battleEndStateColumn);
        battleEndItemColumn = (LinearLayout) v.findViewById(R.id.battleEndItemColumn);
        battleEndDial = (TextView) v.findViewById(R.id.battleEndDial);
        battleEndTouchBoard = (LinearLayout) v.findViewById(R.id.battleEndTouchBoard);
        init();
        return v;
    }

    public void init(){
        if(((MainActivity)MAINCONTEXT).backgroundSound != null){
            ((MainActivity)MAINCONTEXT).backgroundSound.destroy();
            ((MainActivity)MAINCONTEXT).backgroundSound = null;
        }
        ((MainActivity)MAINCONTEXT).backgroundSound = new BackgroundSound(R.raw.victoryintro,R.raw.victoryloop,((MainActivity)MAINCONTEXT));
        battleEndTitle.setText(title);
        battleEndSubtitle.setText(subtitle);
        if(star >= 1) battleEndStar1.setImageResource(R.drawable.star_full); else battleEndStar1.setImageResource(R.drawable.star_hollow);
        if(star >= 2) battleEndStar2.setImageResource(R.drawable.star_full); else battleEndStar2.setImageResource(R.drawable.star_hollow);
        if(star >= 3) battleEndStar3.setImageResource(R.drawable.star_full); else battleEndStar3.setImageResource(R.drawable.star_hollow);
        battleEndStar1.setVisibility(View.INVISIBLE);
        battleEndStar2.setVisibility(View.INVISIBLE);
        battleEndStar3.setVisibility(View.INVISIBLE);
        Animation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                battleEndStar1.setVisibility(View.VISIBLE);
                Animation alphaAnimation = new AlphaAnimation(0,1);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        battleEndStar2.setVisibility(View.VISIBLE);
                        Animation alphaAnimation = new AlphaAnimation(0,1);
                        alphaAnimation.setDuration(500);
                        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                battleEndStar3.setVisibility(View.VISIBLE);
                                initItem();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        battleEndStar3.startAnimation(alphaAnimation);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                alphaAnimation.setDuration(500);
                battleEndStar2.startAnimation(alphaAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        alphaAnimation.setDuration(500);
        battleEndStar1.startAnimation(alphaAnimation);
    }

    public void initItem(){
        battleEndItemColumn.removeAllViews();
        battleEndItemColumn.setVisibility(View.INVISIBLE);
        SharedPreferences pref = MAINCONTEXT.getSharedPreferences("Item",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = MAINCONTEXT.getSharedPreferences("Item",Context.MODE_PRIVATE).edit();
        for(Item item : itemList){
            editor.putInt(""+item.model.id,pref.getInt(""+item.model.id,0)+item.number);
            battleEndItemColumn.addView(item);
        }
        editor.apply();
        Animation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                battleEndItemColumn.setVisibility(View.VISIBLE);
                initHurt();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        alphaAnimation.setDuration(500);
        battleEndItemColumn.startAnimation(alphaAnimation);
    }

    public void initHurt(){
        battleEndStateColumn.removeAllViews();
        battleEndStateColumn.setVisibility(View.INVISIBLE);
        int temp = 0;//统计有几个view被添加
        for(CharacterIconMessage iconMessage:MainActivity.mChooseList){
            if(iconMessage.status != iconMessage.status2){
                HurtStateIcon icon = new HurtStateIcon(MAINCONTEXT,iconMessage.name,iconMessage.status, iconMessage.status2);
                battleEndStateColumn.addView(icon);
                temp++;
            }
        }
        if(temp > 0){
            Animation alphaAnimation = new AlphaAnimation(0,1);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    battleEndStateColumn.setVisibility(View.VISIBLE);
                    battleEndTouchBoard.setSoundEffectsEnabled(false);
                    battleEndTouchBoard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity)MAINCONTEXT).setFragment(MainActivity.MAINMANU);
                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            alphaAnimation.setDuration(500);
            battleEndStateColumn.startAnimation(alphaAnimation);
        }else{
            battleEndStateColumn.setVisibility(View.VISIBLE);
            battleEndTouchBoard.setSoundEffectsEnabled(false);
            battleEndTouchBoard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)MAINCONTEXT).setFragment(MainActivity.MAINMANU);
                }
            });
        }

    }
}
