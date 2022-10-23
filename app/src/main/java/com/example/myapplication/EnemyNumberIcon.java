package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EnemyNumberIcon extends LinearLayout {
    int enemyNumber;
    public static int allEnemyNumber;
    TextView enemyNumberText;
    public EnemyNumberIcon(Context context, int enemyNumber) {
        super(context);
        View v = LayoutInflater.from(context).inflate(R.layout.enemy_number_icon,EnemyNumberIcon.this);
        this.enemyNumber = enemyNumber;
        allEnemyNumber = enemyNumber;
        enemyNumberText = (TextView) v.findViewById(R.id.enemyNumberText);
        enemyNumberText.setText(enemyNumber + "/" + allEnemyNumber);
    }
    public void reduceEnemyNumber(){
        enemyNumber--;
        enemyNumberText.setText(enemyNumber+ "/" + allEnemyNumber);
    }
}
