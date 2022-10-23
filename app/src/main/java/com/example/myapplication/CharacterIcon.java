package com.example.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import static com.example.myapplication.MainActivity.PHYSIC;

public class CharacterIcon extends ConstraintLayout {
    ImageView iconImage;
    ImageView iconElite;
    TextView iconLv;
    ImageView iconWeapon;
    TextView hurtState;
    Context context;
    ImageView unavailable;
    ImageView iconState;
    CharacterIconMessage message;
    LinearLayout layout;
    View v;
    int teamId;
    public CharacterIcon(Context context, CharacterIconMessage message, LinearLayout layout, View.OnClickListener listener, int teamId, final MainMenuFragment mainMenuFragment){
        super(context);
        this.context = context;
        this.message = message;
        this.layout = layout;
        this.teamId = teamId;
        v = LayoutInflater.from(context).inflate(R.layout.teammate_icon,layout,false);
        if(listener != null){
            v.setOnClickListener(listener);
        }else{
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mainMenuFragment.changingCharacter != null){
                        mainMenuFragment.changingCharacter.setState(false);
                        mainMenuFragment.hint1.setVisibility(INVISIBLE);
                        if(mainMenuFragment.changingCharacter == CharacterIcon.this){
                            mainMenuFragment.changingCharacter = null;
                            mainMenuFragment.changingCharacrerFormerTeam = null;
                        }else{
                            mainMenuFragment.hint1.setVisibility(VISIBLE);
                            CharacterIcon.this.setState(true);
                            mainMenuFragment.changingCharacter = CharacterIcon.this;
                            mainMenuFragment.changingCharacrerFormerTeam = MainActivity.teamList.get(CharacterIcon.this.teamId);
                        }
                    }else{
                        mainMenuFragment.hint1.setVisibility(VISIBLE);
                        CharacterIcon.this.setState(true);
                        mainMenuFragment.changingCharacter = CharacterIcon.this;
                        mainMenuFragment.changingCharacrerFormerTeam = MainActivity.teamList.get(CharacterIcon.this.teamId);
                    }
                    Log.d("Sam", "changingFormerTeam:"+mainMenuFragment.changingCharacrerFormerTeam);
                }
            });
        }
        iconElite = (ImageView) v.findViewById(R.id.iconElite);
        iconImage = (ImageView) v.findViewById(R.id.iconImage);
        iconLv = (TextView) v.findViewById(R.id.iconLv);
        iconWeapon = (ImageView) v.findViewById(R.id.iconWeapon);
        hurtState = (TextView) v.findViewById(R.id.hurtState);
        unavailable = (ImageView) v.findViewById(R.id.unavailable);
        iconState = (ImageView) v.findViewById(R.id.iconState);
        iconElite.setImageResource(getResourceByString("elite"+message.elite));
        iconImage.setImageResource(getResourceByString(message.name + "icon"));
        iconLv.setText("LV"+message.level);
        iconWeapon.setImageResource(getResourceByString(message.weapon == PHYSIC ? "physicbattleicon":"magicbattleicon"));
        String tmp="";
        switch(message.status){
            case -1:
                tmp = "死亡";
                hurtState.setBackground(context.getResources().getDrawable(R.drawable.redrect));
                break;
            case 0:
                tmp = "濒危";
                hurtState.setBackground(context.getResources().getDrawable(R.drawable.redrect));
                break;
            case 1:
                tmp = "重伤";
                hurtState.setBackground(context.getResources().getDrawable(R.drawable.orangerect));
                break;
            case 2:
                tmp = "受伤";
                hurtState.setBackground(context.getResources().getDrawable(R.drawable.pinkrect));
                break;
            case 3:
                tmp = "轻伤";
                hurtState.setBackground(context.getResources().getDrawable(R.drawable.yellowrect));
                break;
            case 4:
                tmp = "健康";
                hurtState.setBackground(context.getResources().getDrawable(R.drawable.greenrect));
                break;
        }
        hurtState.setText(tmp);
        setState(false);
        mainMenuFragment.hint1.setVisibility(INVISIBLE);
        layout.addView(v);
    }

    private int getResourceByString(String str){
        Resources res = context.getResources();
        int resourceId = res.getIdentifier(str,"drawable",context.getPackageName());
        return resourceId;
    }

    public void setState(boolean visibility){
        iconState.setVisibility(visibility? VISIBLE:INVISIBLE);
    }
}
