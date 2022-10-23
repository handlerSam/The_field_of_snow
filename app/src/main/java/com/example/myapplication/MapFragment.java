package com.example.myapplication;

import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import java.util.List;

public class MapFragment extends Fragment {
    LinearLayout backButton;
    MainActivity m;
    ConstraintLayout father;
    TextView teamTextView;
    ConstraintLayout mapPlaceDetailLayout;
    TextView mapPlaceCategoryTextView;
    TextView mapPlaceNameTextView;
    TextView mapPlaceIllustrationTextView;
    TextView mapPlaceDetailTextView;
    ImageView mapPlaceUpColumn;
    ImageView mapPlaceMiddleColumn;
    ImageView mapPlaceDownColumn;
    ImageView mapPlaceStartOffImageView;

    public MapFragment(MainActivity m){
        this.m = m;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_layout,container,false);
        initFind(v);
        initSet();
        return v;
    }

    public void initFind(View v){
        teamTextView = (TextView) v.findViewById(R.id.teamTextView);
        backButton = (LinearLayout) v.findViewById(R.id.backButton2);
        father = (ConstraintLayout) v.findViewById(R.id.pinFather);
        mapPlaceDetailLayout = (ConstraintLayout) v.findViewById(R.id.mapPlaceDetailLayout);
        mapPlaceCategoryTextView = (TextView) v.findViewById(R.id.mapPlaceCategoryTextView);
        mapPlaceNameTextView = (TextView) v.findViewById(R.id.mapPlaceNameTextView);
        mapPlaceIllustrationTextView = (TextView) v.findViewById(R.id.mapPlaceIllustrationTextView);
        mapPlaceDetailTextView = (TextView) v.findViewById(R.id.mapPlaceDetailTextView);
        mapPlaceUpColumn = (ImageView) v.findViewById(R.id.mapPlaceUpColumn);
        mapPlaceMiddleColumn = (ImageView) v.findViewById(R.id.mapPlaceMiddleColumn);
        mapPlaceDownColumn = (ImageView) v.findViewById(R.id.mapPlaceDownColumn);
        mapPlaceStartOffImageView = (ImageView) v.findViewById(R.id.mapPlaceStartOffImageView);
    }

    public void initSet(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回按钮
                m.setFragment(MainActivity.MAINMANU);
            }
        });
        teamTextView.setText("当前编队: "+ (MainMenuFragment.chooseTeam+1));
        for(int i = 0; i < MainActivity.placeList.size(); i++){
            PinLayout p = new PinLayout(m,MainActivity.placeList.get(i).name,MainActivity.placeList.get(i).color);
            p.setId(View.generateViewId());
            father.addView(p);
            ConstraintSet sampleSet = new ConstraintSet();
            sampleSet.clone(father);
            sampleSet.connect(p.getId(),ConstraintSet.LEFT,father.getId(),ConstraintSet.LEFT,(MainActivity.placeList.get(i).coordinary[0]-2)*50);
            sampleSet.connect(p.getId(),ConstraintSet.TOP,father.getId(),ConstraintSet.TOP,(MainActivity.placeList.get(i).coordinary[1]-2)*50);
            sampleSet.applyTo(father);
            father.setVisibility(View.VISIBLE);
            final int temp = i;
            p.father.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isVillage = MainActivity.placeList.get(temp) instanceof Village;
                    Place p = MainActivity.placeList.get(temp);
                    String color = p.color;
                    if(isVillage){
                        Village temp = (Village) p;
                        mapPlaceDetailTextView.setText("村庄等级:"+temp.villageLevel+"\n与村民关系等级："+temp.friendship);
                    }else{
                        BattleField temp = (BattleField) p;
                        mapPlaceDetailTextView.setText("推荐等级:"+temp.averageLevel);
                    }
                    mapPlaceDetailLayout.setVisibility(View.VISIBLE);
                    mapPlaceDetailLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mapPlaceDetailLayout.setVisibility(View.INVISIBLE);
                        }
                    });
                    mapPlaceCategoryTextView.setText(isVillage? "村庄":"战场");
                    mapPlaceCategoryTextView.setTextColor(getColorByString(color));
                    mapPlaceNameTextView.setText(p.name);
                    mapPlaceIllustrationTextView.setText(p.illustration);
                    mapPlaceUpColumn.setImageResource(getDrawableByString("place_infor_up_"+color));
                    mapPlaceMiddleColumn.setImageResource(getDrawableByString("place_infor_middle_"+color));
                    mapPlaceDownColumn.setImageResource(getDrawableByString("place_infor_down_"+color));
                    mapPlaceStartOffImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changePlace(temp);
                        }
                    });
                }
            });
        }
    }

    private void changePlace(int temp){
        //更改队伍地点
        Team t = MainActivity.teamList.get(MainMenuFragment.chooseTeam);
        t.site = MainActivity.placeList.get(temp).name;
        t.x = MainActivity.placeList.get(temp).coordinary[0];
        t.y = MainActivity.placeList.get(temp).coordinary[1];
        m.setFragment(MainActivity.MAINMANU);
    }

    private int getDrawableByString(String str){
        Resources res = m.getResources();
        int resourceId = res.getIdentifier(str,"drawable",m.getPackageName());
        return resourceId;
    }

    private int getColorByString(String str){
        Resources res = m.getResources();
        int resourceId = res.getIdentifier("color"+captureName(str),"color",m.getPackageName());
        return res.getColor(resourceId);
    }

    public String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }
}
