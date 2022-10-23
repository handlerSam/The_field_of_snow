package com.example.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HurtStateIcon extends LinearLayout {
    ImageView hurtStateIcon;
    TextView hurtStateBefore;
    TextView hurtStateAfter;
    Context context;
    public HurtStateIcon(Context context, String charName, int stateBefore, int stateAfter){
        super(context);
        this.context = context;
        View v= LayoutInflater.from(context).inflate(R.layout.hurt_state_icon,HurtStateIcon.this);
        hurtStateIcon = (ImageView) v.findViewById(R.id.hurtStateIcon);
        hurtStateBefore = (TextView) v.findViewById(R.id.hurtStateBefore);
        hurtStateAfter = (TextView) v.findViewById(R.id.hurtStateAfter);
        hurtStateIcon.setImageResource(getResourceByString(charName+"icon"));
        String tmp="";
        switch(stateBefore){
            case -1:
                tmp = "死亡";
                hurtStateBefore.setBackground(context.getResources().getDrawable(R.drawable.redrect));
                break;
            case 0:
                tmp = "濒危";
                hurtStateBefore.setBackground(context.getResources().getDrawable(R.drawable.redrect));
                break;
            case 1:
                tmp = "重伤";
                hurtStateBefore.setBackground(context.getResources().getDrawable(R.drawable.orangerect));
                break;
            case 2:
                tmp = "受伤";
                hurtStateBefore.setBackground(context.getResources().getDrawable(R.drawable.pinkrect));
                break;
            case 3:
                tmp = "轻伤";
                hurtStateBefore.setBackground(context.getResources().getDrawable(R.drawable.yellowrect));
                break;
            case 4:
                tmp = "健康";
                hurtStateBefore.setBackground(context.getResources().getDrawable(R.drawable.greenrect));
                break;
        }
        hurtStateBefore.setText(tmp);
        switch(stateAfter){
            case -1:
                tmp = "死亡";
                hurtStateAfter.setBackground(context.getResources().getDrawable(R.drawable.redrect));
                break;
            case 0:
                tmp = "濒危";
                hurtStateAfter.setBackground(context.getResources().getDrawable(R.drawable.redrect));
                break;
            case 1:
                tmp = "重伤";
                hurtStateAfter.setBackground(context.getResources().getDrawable(R.drawable.orangerect));
                break;
            case 2:
                tmp = "受伤";
                hurtStateAfter.setBackground(context.getResources().getDrawable(R.drawable.pinkrect));
                break;
            case 3:
                tmp = "轻伤";
                hurtStateAfter.setBackground(context.getResources().getDrawable(R.drawable.yellowrect));
                break;
            case 4:
                tmp = "健康";
                hurtStateAfter.setBackground(context.getResources().getDrawable(R.drawable.greenrect));
                break;
        }
        hurtStateAfter.setText(tmp);
    }
    private int getResourceByString(String str){
        Resources res = context.getResources();
        int resourceId = res.getIdentifier(str,"drawable",context.getPackageName());
        return resourceId;
    }
}
