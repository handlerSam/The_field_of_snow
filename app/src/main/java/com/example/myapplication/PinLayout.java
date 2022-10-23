package com.example.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class PinLayout extends LinearLayout {
    public TextView teamText;
    public TextView placeNameView;
    public ImageView pinIcon;
    public LinearLayout father;
    String placeName;
    String color;
    public PinLayout(Context context, String placeName, String color) {
        super(context);
        this.placeName = placeName;
        this.color = color;
        init(context);
    }
    public void init(Context context){
        View c = LayoutInflater.from(context).inflate(R.layout.pin_on_map,PinLayout.this);
        teamText = (TextView) c.findViewById(R.id.teamText);
        placeNameView = (TextView) c.findViewById(R.id.placeName);
        pinIcon = (ImageView) c.findViewById(R.id.pinIcon);
        father = (LinearLayout) c.findViewById(R.id.place);
        placeNameView.setText(placeName);
        Resources res = ((MainActivity)(context)).getResources();
        int resourceId = res.getIdentifier("pin_"+color,"drawable",((MainActivity)(context)).getPackageName());
        pinIcon.setImageResource(resourceId);
        refreshTeamText();
    }

    public void refreshTeamText(){
        boolean flag = false;
        for(int i = 0; i < MainActivity.teamList.size(); i++){
            if(MainActivity.teamList.get(i).site.equals(placeName)){
                teamText.setText(teamText.getText().toString()+(i+1));
                flag = true;
            }
        }
        teamText.setText("编队:"+ teamText.getText().toString());
        teamText.setVisibility(flag? VISIBLE:GONE);
    }
}
