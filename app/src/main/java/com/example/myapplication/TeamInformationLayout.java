package com.example.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



public class TeamInformationLayout extends LinearLayout {
    ImageView teamNumber;
    LinearLayout teamCharacterColumn;
    TextView teamSite;
    TextView teamFood;
    TextView teamWine;
    Context context;
    View v;
    public TeamInformationLayout(Context context, Team team, LinearLayout layout, OnClickListener onClickListener, MainMenuFragment mainMenuFragment){
        super(context);
        this.context = context;
        v = LayoutInflater.from(context).inflate(R.layout.team_information,TeamInformationLayout.this,false);
        if(onClickListener != null)v.setOnClickListener(onClickListener);
        initFind(v);
        teamNumber.setImageResource(getResourceByString("number_"+team.teamNumber));
        for(int i = 0; i < team.characterList.size(); i++){
            CharacterIcon icon = new CharacterIcon(context, team.characterList.get(i), teamCharacterColumn, null, team.teamNumber-1, mainMenuFragment);
        }
        teamSite.setText("地点：" + team.site);
        teamFood.setText("补给"+ team.food);
        teamWine.setText("酒"+team.wine);
        layout.addView(v);
    }
    public void setOnClickListener(OnClickListener onClickListener){
        v.setOnClickListener(onClickListener);
    }

    public void initFind(View v){
        teamNumber = (ImageView) v.findViewById(R.id.teamNumber);
        teamCharacterColumn = (LinearLayout) v.findViewById(R.id.teamCharacterColumn);
        teamSite = (TextView) v.findViewById(R.id.teamSite);
        teamFood = (TextView) v.findViewById(R.id.teamFood);
        teamWine = (TextView) v.findViewById(R.id.teamWine);
    }

    private int getResourceByString(String str){
        Resources res = context.getResources();
        int resourceId = res.getIdentifier(str,"drawable",context.getPackageName());
        return resourceId;
    }
}

