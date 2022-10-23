package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CostIcon extends LinearLayout {
    TextView costBar;
    TextView costNumber;
    public static int cost;
    public int progress = 0;//max:100
    public int speed;
    public int costMax;
    BattleFragment battleFragment;
    public CostIcon(Context context, int cost, int speed, int costMax, BattleFragment battleFragment) {
        super(context);
        View v = LayoutInflater.from(context).inflate(R.layout.costicon,CostIcon.this);
        costBar = (TextView) v.findViewById(R.id.costProgressBar);
        costNumber = (TextView) v.findViewById(R.id.costNumber);
        this.cost = cost;
        this.speed = speed;
        this.costMax = costMax;
        this.battleFragment = battleFragment;
        updateCostNumber();

    }

    public void updateCostNumber(){
        costNumber.setText(""+cost);
        battleFragment.onCostChange();
    }

    public void updateCostBar(){
        LayoutParams params = (LinearLayout.LayoutParams)costBar.getLayoutParams();
        params.width = (int)(1.0*progress/100*160);
        costBar.setLayoutParams(params);
    }

    public void costBarAdd(){
        if(cost >= costMax){
            if(progress != 0){
                progress = 0;
                updateCostBar();
            }
            return;
        }
        if(progress + speed >= 100){
            progress = progress + speed - 100;
            cost++;
            if(cost >= costMax){
                progress = 0;
                updateCostBar();
            }
            updateCostNumber();
        }else{
            progress += speed;
        }
        updateCostBar();
    }

    public void addCost(int number){
        if(cost + number <= costMax){
            cost += number;
        }else{
            cost = costMax;
        }
        updateCostNumber();
    }
}
