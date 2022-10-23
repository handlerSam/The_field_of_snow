package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;


public class SkillIcon extends ConstraintLayout {
    public static final int CHARGING = 1;
    public static final int READY = 2;
    public static final int USING = 3;

    ImageView icon;
    ImageView skillCharge;
    ConstraintLayout stage;
    ConstraintSet stageSet = new ConstraintSet();
    float process = 0f;
    boolean isCharge = true;
    int energy;
    int maxEnergy;
    int skillState;
    int leftOverTime;
    int duration;
    BattleFragment battleFragment = MainActivity.battleFragment;

    public SkillIcon(Context context, AttributeSet set){
        super(context,set);
        View v = LayoutInflater.from(context).inflate(R.layout.skill_icon,this);
        icon = (ImageView) v.findViewById(R.id.skillIcon);
        skillCharge = (ImageView) v.findViewById(R.id.skillState);
        stage = (ConstraintLayout) v.findViewById(R.id.skillStage);
        setColor(true);
        setProcess(0f);
    }
    public void updateEnergy(int energy, int maxEnergy, int skillState, int leftOverTime, int duration){
        this.energy = energy;
        this.maxEnergy = maxEnergy;
        this.leftOverTime = leftOverTime;
        this.duration = duration;
        switch(skillState){
            case CHARGING:
                if(this.skillState != skillState){
                    battleFragment.stateColumnSkillChargeIcon.setImageResource(R.drawable.lightening);
                    battleFragment.stateColumnSkillChargeText.setBackgroundColor(getResources().getColor(R.color.colorSkillChargeBackground));
                    battleFragment.stateColumnSkillChargeText.setTextColor(getResources().getColor(R.color.colorWhite));
                    setColor(true);
                    this.skillState = skillState;
                }
                battleFragment.stateColumnSkillChargeText.setText((energy/30)+"/"+(maxEnergy/30));
                setProcess(1.0f*energy/maxEnergy);
                break;
            case READY:
                if(this.skillState != skillState){
                    battleFragment.stateColumnSkillChargeIcon.setImageResource(R.drawable.lightening2);
                    battleFragment.stateColumnSkillChargeText.setBackgroundColor(getResources().getColor(R.color.colorSkillReadyBackground));
                    battleFragment.stateColumnSkillChargeText.setText("Ready");
                    battleFragment.stateColumnSkillChargeText.setTextColor(getResources().getColor(R.color.colorBlack));
                    skillCharge.setVisibility(INVISIBLE);
                    this.skillState = skillState;
                }
                break;
            case USING:
                if(this.skillState != skillState){
                    battleFragment.stateColumnSkillChargeIcon.setImageResource(R.drawable.lightening);
                    battleFragment.stateColumnSkillChargeText.setBackgroundColor(getResources().getColor(R.color.colorSkillChargeBackground));
                    battleFragment.stateColumnSkillChargeText.setTextColor(getResources().getColor(R.color.colorWhite));
                    battleFragment.stateColumnSkillChargeText.setText(0+"/"+(maxEnergy/30));
                    setColor(false);
                    this.skillState = skillState;
                }
                setProcess(1.0f*leftOverTime/duration);
                break;
            default:
        }


    }

    public void setProcess(float process){
        this.process = process;
        stageSet.clone(stage);
        stageSet.setMargin(skillCharge.getId(),3,(int)(110*(1-process)));
        stageSet.applyTo(stage);
    }

    public void setColor(boolean isCharge){
        this.isCharge = isCharge;
        skillCharge.setVisibility(VISIBLE);
        skillCharge.setBackgroundColor(getResources().getColor(isCharge? R.color.colorSkillCharge:R.color.colorSkillUsing));
    }

    public void setImage(Drawable d){
        icon.setImageDrawable(d);
    }

    public void setVisibility(boolean visibility){
        battleFragment.stateColumnSkill.setVisibility(visibility? VISIBLE:GONE);
        battleFragment.stateColumnSkillChargeIcon.setVisibility(visibility? VISIBLE:GONE);
        battleFragment.stateColumnSkillChargeText.setVisibility(visibility? VISIBLE:GONE);
        battleFragment.stateColumnSKillName.setVisibility(visibility? VISIBLE:GONE);
        battleFragment.stateColumnSkillIllustration.setVisibility(visibility? VISIBLE:GONE);
        battleFragment.stateColumnSkillReleaseMethod.setVisibility(visibility? VISIBLE:GONE);
        battleFragment.stateColumnSkillChargeMethod.setVisibility(visibility? VISIBLE:GONE);
    }
}
