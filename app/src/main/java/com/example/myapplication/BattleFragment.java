package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.example.myapplication.CharacterFragment.ATK;
import static com.example.myapplication.CharacterFragment.DEF;
import static com.example.myapplication.CharacterFragment.HP;
import static com.example.myapplication.MainActivity.CASTER;
import static com.example.myapplication.MainActivity.DEFENDER;
import static com.example.myapplication.MainActivity.GUARD;
import static com.example.myapplication.MainActivity.MAINMANU;
import static com.example.myapplication.MainActivity.SCREENWIDTH;
import static com.example.myapplication.MainActivity.SNIPER;
import static com.example.myapplication.MainActivity.VANGUARD;


public class BattleFragment extends Fragment {
    public static final int ONCREATE = 1;
    public static final int DISPLAY = 2;

    public static final int KEEPSTANDING = 1;
    public static final int CHARGE = 0;

    public static final int CHARGING = 1;
    public static final int READY = 2;
    public static final int USING = 3;

    public static final boolean PHYSIC = true;
    public static final boolean MAGIC = false;

    public static float playSpeed = 1;
    public float formSpeed = 1;
    public static final int CostAddWhenCharGetFloatCost = 15;
    public static final int FloatCostRevivalTime = 1000;

    public static final int MAXCHARACTERNUMBER = 30;

    public static int smallTime;
    public static int largeTime;

    int level;

    public static int LastLinePosition = 800;
    public static int ICONWIDTH = 180;
    public boolean isStateColumnShow = false; // Column的管理在CharacterBattleIcon的OnChosen里面
    public boolean isPause = false;
    public static int isVictory = 0;// 1 victory -1 defeated
    public static ConstraintLayout stage;
    public static ConstraintSet sampleSet = new ConstraintSet();
    public CostIcon costIcon;
    public static ArrayList<CharacterMessage> characterQueue = new ArrayList<>();
    public static int chooseIcon = -1;
    LinearLayout characterList;

    public static ArrayList<CharacterBattleIcon> iconList = new ArrayList<>();
    public static int CALCULATE1;
    public static ArrayList<ConstraintLayout> layerList = new ArrayList<>();
    public static ArrayList<EnemyMessage> enemyList = new ArrayList<>();
    public static ArrayList<GifImageView> floatCostList = new ArrayList<>();

    public static int[] floatCostRevivalTime = {0,0,0,0};
    public static int[] floatCostAdvance = {0,0,0,0};
    public static final int FLOATCOSTPACE = 500;
    public static final int FLOATCOSTMAXADVANCE = 2;
    public static int characterNumber = 0;
    public Character characterOnDrag;
    public static int characterOnDragState = DISPLAY;
    public static Timer timer;
    public static TimerTask task;
    public EnemyNumberIcon enemyNumberIcon;
    public ImageView pauseButton;
    public static ConstraintLayout pauseConstraintLayout;
    public TextView highLight;
    public LinearLayout stateColumn;
    public ImageView stateColumnIcon;
    public ImageView stateColumnRetreat;
    public ImageView stateColumnSkillChargeIcon;
    public TextView stateColumnSkillChargeText;
    public SkillIcon stateColumnSkill;
    public TextView stateColumnHp;
    public TextView stateColumnAttack;
    public TextView stateColumnDefend;
    public TextView stateColumnMagicalDefend;
    public TextView stateColumnBlock;
    public TextView stateColumnSKillName;
    public TextView stateColumnSkillIllustration;
    public TextView stateColumnSkillChargeMethod;
    public TextView stateColumnSkillReleaseMethod;

    TextView stateColumnHurtStateBefore;
    TextView stateColumnHurtStateAfter;
    GifImageView endGifImageView;
    ImageView endBlack;
    ConstraintLayout failBackground;
    ImageView speedButton;

    Context MAINCONTEXT;

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case 1:
                    for(int i = 0; i < characterQueue.size(); i++){
                        CharacterMessage characterMessage = characterQueue.get(i);
                        //更新virtualHp
                        if(characterMessage.character.hp < characterMessage.character.virtualHp){
                            characterMessage.character.virtualHp -= (int)Math.max(1,(characterMessage.character.virtualHp-characterMessage.character.hp)/7);
                            if(characterMessage.character.virtualHp < characterMessage.character.hp){
                                characterMessage.character.virtualHp = characterMessage.character.hp;
                            }
                            characterMessage.character.setVirtualHp(characterMessage.character.virtualHp);
                        }else if (characterMessage.character.hp > characterMessage.character.virtualHp){
                            characterMessage.character.virtualHp = characterMessage.character.hp;
                            characterMessage.character.setVirtualHp(characterMessage.character.virtualHp);
                        }

                        //记得如果要给敌人做技能，需要在修改下一行
                        if(characterMessage.relationship && !characterMessage.state.equals("dying") && characterMessage.character.icon.characterSkill != null){
                            if(characterMessage.character.icon.characterSkill.skill.increaseMethod == Skill.AUTO){
                                characterMessage.character.icon.characterSkill.energy++;
                            }
                            characterMessage.character.icon.characterSkill.updateCharacterSkill();
                            CharacterSkill tmp = characterMessage.character.icon.characterSkill;
                            characterMessage.character.updateEnergy(tmp.energy,tmp.skillPoint,tmp.skillState,tmp.leftoverTime,tmp.duration);
                        }else if(!characterMessage.relationship && characterMessage.character.enemySkill != null && !characterMessage.state.equals("dying")){
                            if(characterMessage.character.enemySkill.skill.increaseMethod == Skill.AUTO){
                                characterMessage.character.enemySkill.energy++;
                            }
                            characterMessage.character.enemySkill.updateCharacterSkill();
                            CharacterSkill tmp = characterMessage.character.enemySkill;
                            characterMessage.character.updateEnergy(tmp.energy,tmp.skillPoint,tmp.skillState,tmp.leftoverTime,tmp.duration);
                        }
                        if(characterMessage.state.equals("attack") || characterMessage.state.equals("dying")) continue;
                        int movex = characterMessage.speedx;
                        characterMessage.x += movex;
                        //在道路中间吃到cost
                        if(floatCostRevivalTime[characterMessage.layer] == 0 && characterMessage.relationship && Math.abs(SCREENWIDTH - floatCostAdvance[characterMessage.layer]*FLOATCOSTPACE - characterMessage.x) < 50){
                            ((MainActivity)MAINCONTEXT).playSound(MainActivity.voiceGetCost,1.0f,1.0f);
                            costIcon.addCost(CostAddWhenCharGetFloatCost);
                            if(floatCostAdvance[characterMessage.layer] < FLOATCOSTMAXADVANCE){
                                floatCostAdvance[characterMessage.layer]++;
                                ConstraintSet sampleSet = new ConstraintSet();
                                sampleSet.clone(stage);//克隆一个father的Constrainset副本，father为父ConstraintLayout布局的id
                                sampleSet.connect(floatCostList.get(characterMessage.layer).getId(),ConstraintSet.RIGHT,stage.getId(),ConstraintSet.RIGHT,floatCostAdvance[characterMessage.layer]*FLOATCOSTPACE);
                                sampleSet.applyTo(stage);//应用该副本
                                floatCostList.get(characterMessage.layer).setVisibility(View.VISIBLE);
                            }
                            floatCostRevivalTime[characterMessage.layer] = FloatCostRevivalTime;
                            floatCostList.get(characterMessage.layer).setVisibility(View.INVISIBLE);
                        }
                        CharacterMessage temp = testAttack(i);//temp is enemy who have block left or has been in the attackqueue
                        if(temp != null){
                            if(characterMessage.getBlockLength() == 0 && characterMessage.attackRange == 0){
                                temp.block.add(characterMessage);
                                characterMessage.block.add(temp);
                            }
                            if(characterMessage.getBlockLength() == 0 && characterMessage.attackRange != 0){
                                characterMessage.block.add(temp);
                            }
                            if(!characterMessage.state.equals("attack")){
                                characterMessage.character.attack();
                                characterMessage.state = "attack";
                                Log.d("Sam",characterMessage.character.charName + " " + characterMessage.state + " " + characterMessage.getBlockLength() + " 1");
                            }
                            if(!temp.state.equals("attack") && characterMessage.attackRange == 0){
                                temp.character.attack();
                                temp.state = "attack";
                                Log.d("Sam",temp.character.charName + " " + temp.state + " " + temp.getBlockLength() + " 2");
                            }
                            characterMessage.x -= movex;
                        }else if((characterMessage.x > MainActivity.SCREENWIDTH && characterMessage.relationship) || (characterMessage.x < MainActivity.CharWidth/2 && !characterMessage.relationship)){
                            //character run out of map
                            if(!characterMessage.relationship && isVictory == 0){
                                setOutCome(false);
                                isVictory = -1;
                            }
                            dropCharacter(characterMessage);
                            //在道路末尾吃到cost
                            if(floatCostRevivalTime[characterMessage.layer] == 0 && characterMessage.relationship && floatCostAdvance[characterMessage.layer] == 0){
                                ((MainActivity)MAINCONTEXT).playSound(MainActivity.voiceGetCost,1.0f,1.0f);
                                costIcon.addCost(CostAddWhenCharGetFloatCost);
                                if(floatCostAdvance[characterMessage.layer] < FLOATCOSTMAXADVANCE){
                                    floatCostAdvance[characterMessage.layer]++;
                                    ConstraintSet sampleSet = new ConstraintSet();
                                    sampleSet.clone(stage);//克隆一个father的Constrainset副本，father为父ConstraintLayout布局的id
                                    sampleSet.connect(floatCostList.get(characterMessage.layer).getId(),ConstraintSet.RIGHT,stage.getId(),ConstraintSet.RIGHT,floatCostAdvance[characterMessage.layer]*FLOATCOSTPACE);
                                    sampleSet.applyTo(stage);//应用该副本
                                    floatCostList.get(characterMessage.layer).setVisibility(View.VISIBLE);
                                }
                                floatCostRevivalTime[characterMessage.layer] = FloatCostRevivalTime;
                                floatCostList.get(characterMessage.layer).setVisibility(View.INVISIBLE);
                            }
                        }else{
                            switch(characterMessage.ai){
                                case KEEPSTANDING:
                                    moveCharacter(0,characterMessage);
                                    if(!characterMessage.state.equals("standing")){
                                        characterMessage.state = "standing";
                                        Log.d("Sam",characterMessage.character.charName + " " + characterMessage.state + " " + characterMessage.getBlockLength() + " 3");
                                    }
                                    characterMessage.x -= movex;
                                    break;
                                case CHARGE:
                                    moveCharacter(movex,characterMessage);
                                    if(!characterMessage.state.equals("moving")){
                                        characterMessage.state = "moving";
                                        Log.d("Sam",characterMessage.character.charName + " " + characterMessage.state + " " + characterMessage.getBlockLength() + " 4");
                                    }
                                    //moving skill increase point
                                    if(characterMessage.relationship){
                                        if(characterMessage.character.icon.characterSkill != null && characterMessage.character.icon.characterSkill.skill.increaseMethod == Skill.MOVING){
                                            characterMessage.character.icon.characterSkill.energy++;
                                        }
                                    }else{
                                        if(characterMessage.character.enemySkill != null && characterMessage.character.enemySkill.skill.increaseMethod == Skill.MOVING){
                                            characterMessage.character.enemySkill.energy++;
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                    costIcon.costBarAdd();
                    changeRevivalTime();
                    changeFloatCostRevivalTime();
                    updateStateColumn();
                    break;
                case 2:
                    releaseEnemy();
                    break;
                default:
                    Log.d("Sam","unHandledMessage!");
            }
        }
    };

    public BattleFragment(Operation operation){
        initStaticThing();
        this.MAINCONTEXT = operation.context;
        this.level = operation.level;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.battle_fragment,container,false);
        initFind(v);
        initCharacterIconList();
        initSound();
        initEnemyList();
        initStageOnTouch();
        initColumn();
        setCostIcon();
        setEnemyNumberIcon();
        startTimer();
        return v;
    }

    public Character addCharacter(int x, int layer, String name, int moveSpeed, int maxBlock, boolean relationship, int hp, int attack, int ai, int attackRange, int physicalDefend, int magicalDefend, boolean weapon, boolean canExplosion){
        //layer 0-3
        if(x < MainActivity.CharWidth/2) x = MainActivity.CharWidth/2;
        int y = LastLinePosition - layer*100;
        Character character = new Character(MAINCONTEXT,name,hp,attack,relationship,physicalDefend,magicalDefend,weapon,canExplosion,this,null);
        int id = View.generateViewId();
        character.setId(id);
        ConstraintLayout backGround = layerList.get(layer);
        backGround.addView(character);

        sampleSet.clone(backGround);
        sampleSet.connect(id,ConstraintSet.LEFT,backGround.getId(),ConstraintSet.LEFT, x-MainActivity.CharWidth/2);
        sampleSet.connect(id,ConstraintSet.TOP,backGround.getId(),ConstraintSet.TOP,y-MainActivity.CharHeight/7*6);
        sampleSet.applyTo(backGround);

        CharacterMessage characterMessage = new CharacterMessage(x,layer,character,maxBlock,relationship,moveSpeed,ai,attackRange);
        characterQueue.add(characterMessage);
        character.message = characterMessage;
        characterNumber++;
        return character;
    }

    public static Character addCharacter(Character character){
        //layer 0-3
        ConstraintLayout backGround = layerList.get(character.message.layer);
        backGround.addView(character);
        character.gifImageView.setAlpha(1.0f);
        sampleSet.clone(backGround);
        sampleSet.connect(character.getId(),ConstraintSet.LEFT,backGround.getId(),ConstraintSet.LEFT, character.message.x-MainActivity.CharWidth/2);
        sampleSet.connect(character.getId(),ConstraintSet.TOP,backGround.getId(),ConstraintSet.TOP,LastLinePosition - character.message.layer*100 - MainActivity.CharHeight/7*6);
        sampleSet.applyTo(backGround);
        characterQueue.add(character.message);
        switch(character.message.ai){
            case CHARGE:
                character.move(character.message.relationship ? "right":"left");
                break;
            case KEEPSTANDING:
                character.idle(character.message.relationship ? "right":"left");
                break;
        }
        characterNumber++;
        return character;
    }

    public Character addCharacterImage(int x,int layer, String name, int moveSpeed, int maxBlock, boolean relationship, int hp, int attack, int ai, int icon, int attackRange, int physicalDefend, int magicalDefend, boolean weapon, boolean canExplosion){
        //layer 0-3
        if(x < MainActivity.CharWidth/2) x = MainActivity.CharWidth/2;
        Character character = new Character(MAINCONTEXT,name,hp,attack,relationship,physicalDefend,magicalDefend,weapon, canExplosion,this,null);
        int id = View.generateViewId();
        int y = LastLinePosition - layer*100;
        character.setId(id);
        stage.addView(character);
        character.gifImageView.setAlpha(0.5f);
        CharacterMessage characterMessageOnDrag = new CharacterMessage(x,layer,character,maxBlock,relationship,moveSpeed,ai,attackRange);
        characterMessageOnDrag.icon = icon;
        character.message = characterMessageOnDrag;
        highLight.setVisibility(View.VISIBLE);
        sampleSet.clone(stage);
        sampleSet.connect(id,ConstraintSet.LEFT,stage.getId(),ConstraintSet.LEFT, x-MainActivity.CharWidth/2);
        sampleSet.connect(id,ConstraintSet.TOP,stage.getId(),ConstraintSet.TOP,y-MainActivity.CharHeight/7*6);
        sampleSet.setMargin(R.id.highLight,3,y-MainActivity.CharHeight/7);
        sampleSet.applyTo(stage);
        return character;
    }

    public void dropCharacter(CharacterMessage characterMessage){
        //before dropCharacter, you need to set character.hp = 0 in order to release it from other character's blockList
        if(characterMessage != null){
            if(characterMessage.character.icon != null){
                characterMessage.character.icon.character = null;
                characterMessage.character.icon = null;
            }
            if(!characterMessage.relationship){
                enemyNumberIcon.reduceEnemyNumber();
                if(enemyNumberIcon.enemyNumber == 0 && isVictory == 0){
                    setOutCome(true);
                    isVictory = 1;
                }
            }else{
                iconList.get(characterMessage.icon).changeState(CharacterBattleIcon.DIE);
            }
            layerList.get(characterMessage.layer).removeView(characterMessage.character);
            characterQueue.remove(characterMessage);
            characterNumber--;
        }
    }

    public static void moveCharacter(int x, CharacterMessage characterMessage){
        switch(characterMessage.ai){
            case KEEPSTANDING:
                if(!characterMessage.state.equals("standing")){
                    characterMessage.character.idle();
                }
                break;
            case CHARGE:
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) characterMessage.character.getLayoutParams();
                params.leftMargin += x;
                if(characterMessage.relationship){
                    if(characterMessage.character.icon.characterSkill != null && characterMessage.character.icon.characterSkill.skillState == USING){
                        switch(characterMessage.character.icon.characterSkill.skillId){
                            case 9:
                                params.leftMargin += characterMessage.character.icon.characterSkill.affect;
                                characterMessage.x += characterMessage.character.icon.characterSkill.affect;
                                break;
                            default:
                        }
                    }
                }else{
                    if(characterMessage.character.enemySkill != null && characterMessage.character.enemySkill.skillState == USING){
                        switch(characterMessage.character.enemySkill.skillId){
                            case 9:
                                params.leftMargin -= characterMessage.character.enemySkill.affect;
                                characterMessage.x -= characterMessage.character.enemySkill.affect;
                                break;
                            default:
                        }
                    }
                }
                characterMessage.character.setLayoutParams(params);
                if(!characterMessage.state.equals("moving")){
                    characterMessage.character.move();
                }
                break;
        }

    }

    private void startTimer(){
        smallTime = 0;
        largeTime = 0;
        if(timer != null){
            timer.cancel();
        }
        timer = new Timer();
        setTimerTask();
        timer.schedule(task, 0,(int)(30/playSpeed));
    }

    private static CharacterMessage testAttack(int id){
        if(characterQueue.get(id).attackRange==0){
            boolean relationship = characterQueue.get(id).relationship;
            while(characterQueue.get(id).getBlockLength() != 0){
                if(characterQueue.get(id).getAttackTarget() == null){
                    characterQueue.get(id).block.remove(0);
                }else if (characterQueue.get(id).getAttackTarget().hp <= 0){
                    characterQueue.get(id).block.remove(0);
                }else{
                    break;
                }
            }
            if(characterQueue.get(id).getAttackTarget() != null && characterQueue.get(id).getAttackTarget().hp > 0) return characterQueue.get(id).block.get(0);
            int x = characterQueue.get(id).x;
            int layer = characterQueue.get(id).layer;
            if(characterQueue.get(id).getBlockLength() >= characterQueue.get(id).maxBlock) return null;
            for(int i = 0; i < characterQueue.size(); i++){
                if(characterQueue.get(i).layer == layer){
                    if(relationship != characterQueue.get(i).relationship){
                        if(characterQueue.get(i).character.hp > 0){
                            if(characterQueue.get(i).getBlockLength() < characterQueue.get(i).maxBlock){
                                if(Math.abs(x-characterQueue.get(i).x) < MainActivity.CharAttackWidth){
                                    return characterQueue.get(i);
                                }
                            }
                        }
                    }
                }
            }
        }else{
            boolean relationship = characterQueue.get(id).relationship;
            while(characterQueue.get(id).getBlockLength() != 0){
                if(characterQueue.get(id).getAttackTarget() == null){
                    characterQueue.get(id).block.remove(0);
                }else if (characterQueue.get(id).getAttackTarget().hp <= 0){
                    characterQueue.get(id).block.remove(0);
                }else if (characterQueue.get(id).relationship && characterQueue.get(id).getAttackTarget().message.x - characterQueue.get(id).x > 0 ? (characterQueue.get(id).getAttackTarget().message.x - characterQueue.get(id).x > MainActivity.CharAttackWidth + characterQueue.get(id).attackRange) : (characterQueue.get(id).x-characterQueue.get(id).getAttackTarget().message.x > MainActivity.CharAttackWidth)){
                    characterQueue.get(id).block.remove(0);
                    Log.d("OUTOFRANGE","OUTOFRANGE!");
                }else if (!characterQueue.get(id).relationship && characterQueue.get(id).getAttackTarget().message.x - characterQueue.get(id).x > 0 ? (characterQueue.get(id).getAttackTarget().message.x - characterQueue.get(id).x > MainActivity.CharAttackWidth) : (characterQueue.get(id).x-characterQueue.get(id).getAttackTarget().message.x > MainActivity.CharAttackWidth  + characterQueue.get(id).attackRange)){
                    characterQueue.get(id).block.remove(0);
                    Log.d("OUTOFRANGE","OUTOFRANGE!");
                }else{
                    break;
                }
            }
            if(characterQueue.get(id).getAttackTarget() != null && characterQueue.get(id).getAttackTarget().hp > 0) return characterQueue.get(id).block.get(0);
            int x = characterQueue.get(id).x;
            int layer = characterQueue.get(id).layer;
            if(characterQueue.get(id).getBlockLength() >= characterQueue.get(id).maxBlock) return null;
            for(int i = 0; i < characterQueue.size(); i++){
                if(Math.abs(characterQueue.get(i).layer - layer) <= 1){
                    if(relationship != characterQueue.get(i).relationship){
                        if(characterQueue.get(i).character.hp > 0){
                            if(relationship && ((characterQueue.get(i).x - x > 0) ? (characterQueue.get(i).x - x < MainActivity.CharAttackWidth + characterQueue.get(id).attackRange) : (x-characterQueue.get(i).x < MainActivity.CharAttackWidth))){
                                return characterQueue.get(i);
                            }else if(!relationship && ((characterQueue.get(i).x - x > 0) ? (characterQueue.get(i).x - x < MainActivity.CharAttackWidth) : (x-characterQueue.get(i).x < MainActivity.CharAttackWidth  + characterQueue.get(id).attackRange))){
                                if(characterQueue.get(i).character.icon != null){
                                    return characterQueue.get(i);
                                }
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    private void setCostIcon(){
        costIcon = new CostIcon(MAINCONTEXT,8,3,99,this);
        int id = View.generateViewId();
        costIcon.setId(id);
        stage.addView(costIcon);
        sampleSet.clone(stage);
        sampleSet.connect(id,ConstraintSet.RIGHT,stage.getId(),ConstraintSet.RIGHT, 0);
        sampleSet.connect(id,ConstraintSet.BOTTOM,characterList.getId(),ConstraintSet.TOP,0);
        sampleSet.applyTo(stage);
    }

    private void addCharacterIconToList(){
        for(int i = 0; i < MainActivity.mChooseList.size(); i++){
            CharacterIconMessage m = MainActivity.mChooseList.get(i);
            CharacterBattleIcon icon = new CharacterBattleIcon(MAINCONTEXT,m.name,m.level,m.elite,m.cost,m.weapon,m.revivalTime,m.retreatable, m.characterSkill,this,m);
            int id = View.generateViewId();
            icon.id = i;
            icon.setId(id);
            characterList.addView(icon);
            iconList.add(icon);
        }
        CALCULATE1 = -MainActivity.SCREENWIDTH + iconList.size()*ICONWIDTH;
    }

    public void onCostChange(){
        for(CharacterBattleIcon icon: iconList){
            if(icon.state != CharacterBattleIcon.ONBATTLE && icon.state != CharacterBattleIcon.DIE){
                if(costIcon.cost < icon.cost){
                    if(icon.state != CharacterBattleIcon.UNAVAILABLE){
                        icon.changeState(CharacterBattleIcon.UNAVAILABLE);
                    }
                }else{
                    if(icon.iconMessage.status2 >= 0){
                        if(icon.state != CharacterBattleIcon.AVAILABLE){
                            icon.changeState(CharacterBattleIcon.AVAILABLE);
                        }
                    }
                }
            }
        }
    }

    private void initFind(View v){
        stage = (ConstraintLayout) v.findViewById(R.id.stage);
        characterList = (LinearLayout) v.findViewById(R.id.battleCharacterIconList);
        pauseButton = (ImageView) v.findViewById(R.id.pauseButton);
        pauseConstraintLayout = (ConstraintLayout) v.findViewById(R.id.pauseBackground);
        highLight = (TextView) v.findViewById(R.id.highLight);
        stateColumn = (LinearLayout) v.findViewById(R.id.stateColumn);
        stateColumnIcon = (ImageView) v.findViewById(R.id.stateColumnIcon);
        stateColumnRetreat = (ImageView) v.findViewById(R.id.stateColumnRetreat);
        stateColumnSkillChargeIcon = (ImageView) v.findViewById(R.id.stateColumnSkillChargeIcon);
        stateColumnSkillChargeText = (TextView) v.findViewById(R.id.stateColumnSkillChargeText);
        stateColumnSkill = (SkillIcon) v.findViewById(R.id.stateColumnSkill);
        stateColumnHp = (TextView) v.findViewById(R.id.stateColumnHp);
        stateColumnAttack = (TextView) v.findViewById(R.id.stateColumnAttack);
        stateColumnDefend = (TextView) v.findViewById(R.id.stateColumnDefend);
        stateColumnMagicalDefend = (TextView) v.findViewById(R.id.stateColumnMagicalDefend);
        stateColumnBlock = (TextView) v.findViewById(R.id.stateColumnBlock);
        stateColumnSKillName = (TextView) v.findViewById(R.id.stateColumnSkillName);
        stateColumnSkillIllustration = (TextView) v.findViewById(R.id.stateColumnSkillIllustrate);
        stateColumnSkillChargeMethod = (TextView) v.findViewById(R.id.stateColumnSkillChargeMethod);
        stateColumnSkillReleaseMethod = (TextView) v.findViewById(R.id.stateColumnSkillReleaseMethod);
        stateColumnHurtStateBefore = (TextView) v.findViewById(R.id.stateColumnHurtStateBefore);
        stateColumnHurtStateAfter = (TextView) v.findViewById(R.id.stateColumnHurtStateAfter);
        endBlack = (ImageView) v.findViewById(R.id.endBlack);
        speedButton = (ImageView) v.findViewById(R.id.speedButton);
        endGifImageView = (GifImageView) v.findViewById(R.id.endGifImageView);
        failBackground = (ConstraintLayout) v.findViewById(R.id.failBackground);
        GifImageView c1 = (GifImageView) v.findViewById(R.id.costFloat0);
        GifImageView c2 = (GifImageView) v.findViewById(R.id.costFloat1);
        GifImageView c3 = (GifImageView) v.findViewById(R.id.costFloat2);
        GifImageView c4 = (GifImageView) v.findViewById(R.id.costFloat3);
        floatCostList.add(c1);
        floatCostList.add(c2);
        floatCostList.add(c3);
        floatCostList.add(c4);
        ConstraintLayout layer1 = (ConstraintLayout) v.findViewById(R.id.layer1);
        ConstraintLayout layer2 = (ConstraintLayout) v.findViewById(R.id.layer2);
        ConstraintLayout layer3 = (ConstraintLayout) v.findViewById(R.id.layer3);
        ConstraintLayout layer4 = (ConstraintLayout) v.findViewById(R.id.layer4);
        layerList.add(layer1);
        layerList.add(layer2);
        layerList.add(layer3);
        layerList.add(layer4);

    }

    private static void changeRevivalTime(){
        for(int i = 0; i < iconList.size(); i++){
            if(iconList.get(i).state == CharacterBattleIcon.DIE){
                iconList.get(i).changeRevivalTime(0.03f);
            }
        }
    }

    private void initEnemyList(){
        int width = MainActivity.SCREENWIDTH + 100;
        int temp;
        int temp2;
        switch(level){
            case 1:
                temp = 10;
                temp2 = 0;
                enemyList.add(addEnemy(getEnemy("ysc",temp,temp2, CHARGE),null, width, 0, 200, 0));
                enemyList.add(addEnemy(getEnemy("ysc",temp,temp2, CHARGE),null, width, 3, 250, 0));
                enemyList.add(addEnemy(getEnemy("ysc",temp,temp2, CHARGE),null, width, 2, 300, 0));
                enemyList.add(addEnemy(getEnemy("ysc2",temp,temp2, CHARGE),null,width, 2, 550, 1));
                enemyList.add(addEnemy(getEnemy("ysc2",temp,temp2, CHARGE),null,width, 3, 590, 1));
                enemyList.add(addEnemy(getEnemy("ysc3",temp,temp2, CHARGE),null,width, 1, 630, 1));
                enemyList.add(addEnemy(getEnemy("ysc",temp,temp2, CHARGE),null, width, 2, 680, 1));
                enemyList.add(addEnemy(getEnemy("ysc3",temp,temp2, CHARGE),null,width, 0, 800, 1));
                enemyList.add(addEnemy(getEnemy("shz",temp,temp2, CHARGE),null,width, 3, 200, 2));
                enemyList.add(addEnemy(getEnemy("bbysc",temp,temp2,CHARGE),null,width,2,230,2));
                enemyList.add(addEnemy(getEnemy("tf",temp,temp2, CHARGE),null, width, 0, 400, 2));
                enemyList.add(addEnemy(getEnemy("fmj",temp,temp2, CHARGE),null, width, 2, 600, 2));
                enemyList.add(addEnemy(getEnemy("fmls",temp,temp2, CHARGE),null, width, 1, 800, 2));
                enemyList.add(addEnemy(getEnemy("bbysc",temp,temp2,CHARGE),null,width,2,500,3));
                enemyList.add(addEnemy(getEnemy("bbysc",temp,temp2,CHARGE),null,width,1,503,3));
                enemyList.add(addEnemy(getEnemy("px",temp,temp2, CHARGE),null, width, 0, 400, 4));
                enemyList.add(addEnemy(getEnemy("px",temp,temp2, CHARGE),null, width, 1, 401, 4));
                enemyList.add(addEnemy(getEnemy("px",temp,temp2, CHARGE),null, width, 2, 402, 4));
                enemyList.add(addEnemy(getEnemy("px",temp,temp2, CHARGE),null, width, 3, 403, 4));
                enemyList.add(addEnemy(getEnemy("ysc",temp,temp2, CHARGE),null, width, 0, 50, 5));
                enemyList.add(addEnemy(getEnemy("ysc2",temp,temp2, CHARGE),null,width, 3, 52, 5));
                enemyList.add(addEnemy(getEnemy("ysc",temp,temp2, CHARGE),null, width, 1, 54, 5));
                enemyList.add(addEnemy(getEnemy("ysc2",temp,temp2, CHARGE),null,width, 2, 56, 5));
                enemyList.add(addEnemy(getEnemy("ysc",temp,temp2, CHARGE),null, width, 0, 80, 5));
                enemyList.add(addEnemy(getEnemy("ysc2",temp,temp2, CHARGE),null,width, 3, 82, 5));
                enemyList.add(addEnemy(getEnemy("ysc",temp,temp2, CHARGE),null, width, 1, 84, 5));
                enemyList.add(addEnemy(getEnemy("ysc2",temp,temp2, CHARGE),null,width, 2, 86, 5));
                enemyList.add(addEnemy(getEnemy("ysc",temp,temp2, CHARGE),null, width, 0, 110, 5));
                enemyList.add(addEnemy(getEnemy("ysc2",temp,temp2, CHARGE),null,width, 3, 112, 5));
                enemyList.add(addEnemy(getEnemy("ysc",temp,temp2, CHARGE),null, width, 1, 114, 5));
                enemyList.add(addEnemy(getEnemy("ysc2",temp,temp2, CHARGE),null,width, 2, 116, 5));
                enemyList.add(addEnemy(getEnemy("gjwzry",temp,temp2, CHARGE),null, width, 2, 700, 6));
                enemyList.add(addEnemy(getEnemy("gjwzry",temp,temp2, CHARGE),null, width, 1, 702, 6));
                enemyList.add(addEnemy(getEnemy("tf",temp,temp2, CHARGE),null, width, 3, 704, 6));
                enemyList.add(addEnemy(getEnemy("tf",temp,temp2, CHARGE),null, width, 0, 706, 6));
                break;
            case 2:
                temp = 20;
                temp2 = 1;
                enemyList.add(addEnemy(getEnemy("skzss",temp,temp2,KEEPSTANDING),null,1400,2,0,0));
                enemyList.add(addEnemy(getEnemy("yjdlq",temp,temp2,CHARGE),null,width,0,350,0));
                enemyList.add(addEnemy(getEnemy("yjdlq",temp,temp2,CHARGE),null,width,2,525,0));
                enemyList.add(addEnemy(getEnemy("bbysc",temp,temp2,CHARGE),null,width,3,535,0));
                enemyList.add(addEnemy(getEnemy("yjdlq",temp,temp2,CHARGE),null,width,3,550,0));
                enemyList.add(addEnemy(getEnemy("yjdlq",temp,temp2,CHARGE),null,width,2,275,1));
                enemyList.add(addEnemy(getEnemy("yjdlq",temp,temp2,CHARGE),null,width,1,375,1));
                enemyList.add(addEnemy(getEnemy("skzdb",temp,temp2,CHARGE),null,width,0,700,1));
                enemyList.add(addEnemy(getEnemy("skzdb",temp,temp2,CHARGE),null,width,3,800,1));
                enemyList.add(addEnemy(getEnemy("skzdb",temp,temp2,CHARGE),null,width,1,970,1));
                enemyList.add(addEnemy(getEnemy("bbysc",temp,temp2,CHARGE),null,width,1,200,2));
                enemyList.add(addEnemy(getEnemy("skzdb",temp,temp2,CHARGE),null,width,0,440,2));
                enemyList.add(addEnemy(getEnemy("skzdb",temp,temp2,CHARGE),null,width,2,480,2));
                enemyList.add(addEnemy(getEnemy("skzjjs",temp,temp2,CHARGE),null,width,1,520,2));
                enemyList.add(addEnemy(getEnemy("yjdlq",temp,temp2,CHARGE),null,width,1,560,2));
                enemyList.add(addEnemy(getEnemy("skzdb",temp,temp2,CHARGE),null,width,2,870,2));
                enemyList.add(addEnemy(getEnemy("skzdb",temp,temp2,CHARGE),null,width,3,900,2));
                enemyList.add(addEnemy(getEnemy("bbysc",temp,temp2,CHARGE),null,width,2,950,2));
                enemyList.add(addEnemy(getEnemy("skzmjs",temp,temp2,CHARGE),null,width,3,300,3));
                enemyList.add(addEnemy(getEnemy("skzmjs",temp,temp2,CHARGE),null,width,1,470,3));
                enemyList.add(addEnemy(getEnemy("skzdjs",temp,temp2,CHARGE),null,width,0,500,3));
                enemyList.add(addEnemy(getEnemy("skzmjs",temp,temp2,CHARGE),null,width,2,580,3));
                enemyList.add(addEnemy(getEnemy("skzdjs",temp,temp2,CHARGE),null,width,1,720,3));
                enemyList.add(addEnemy(getEnemy("skzmjs",temp,temp2,CHARGE),null,width,3,800,3));
                enemyList.add(addEnemy(getEnemy("skzmjszz",temp,temp2,CHARGE),null,width,1,400,4));
                enemyList.add(addEnemy(getEnemy("skzmjszz",temp,temp2,CHARGE),null,width,2,401,4));
                enemyList.add(addEnemy(getEnemy("skzmjszz",temp,temp2,CHARGE),null,width,0,500,4));
                enemyList.add(addEnemy(getEnemy("skzmjszz",temp,temp2,CHARGE),null,width,3,501,4));
                enemyList.add(addEnemy(getEnemy("skzjjs",temp,temp2,CHARGE),null,width,1,530,4));
                enemyList.add(addEnemy(getEnemy("skzjjs",temp,temp2,CHARGE),null,width,2,531,4));
                break;
            case 3:
                temp = 40;
                temp2 = 2;
                enemyList.add(addEnemy(getEnemy("grzjcg", temp, temp2, CHARGE), null, width, 1, 777, 0));
                enemyList.add(addEnemy(getEnemy("wssls", temp, temp2, CHARGE), null, width, 2, 950, 0));
                enemyList.add(addEnemy(getEnemy("grzjcg", temp, temp2, CHARGE), null, width, 0, 964, 0));
                enemyList.add(addEnemy(getEnemy("grzjcg", temp, temp2, CHARGE), null, width, 1, 161, 1));
                enemyList.add(addEnemy(getEnemy("grzjcg", temp, temp2, CHARGE), null, width, 0, 409, 1));
                enemyList.add(addEnemy(getEnemy("wssls", temp, temp2, CHARGE), null, width, 1, 506, 1));
                enemyList.add(addEnemy(getEnemy("wssls", temp, temp2, CHARGE), null, width, 1, 774, 1));
                enemyList.add(addEnemy(getEnemy("wssls", temp, temp2, CHARGE), null, width, 1, 794, 1));
                enemyList.add(addEnemy(getEnemy("wssls", temp, temp2, CHARGE), null, width, 1, 803, 1));
                enemyList.add(addEnemy(getEnemy("grzjcg", temp, temp2, CHARGE), null, width, 2, 829, 1));
                enemyList.add(addEnemy(getEnemy("wssls", temp, temp2, CHARGE), null, width, 2, 5, 2));
                enemyList.add(addEnemy(getEnemy("grzjcg", temp, temp2, CHARGE), null, width, 2, 72, 2));
                enemyList.add(addEnemy(getEnemy("grzjcg", temp, temp2, CHARGE), null, width, 0, 273, 2));
                enemyList.add(addEnemy(getEnemy("wssls", temp, temp2, CHARGE), null, width, 2, 273, 2));
                enemyList.add(addEnemy(getEnemy("wssls", temp, temp2, CHARGE), null, width, 2, 441, 2));
                enemyList.add(addEnemy(getEnemy("grzjcg", temp, temp2, CHARGE), null, width, 0, 904, 2));
                enemyList.add(addEnemy(getEnemy("wssls", temp, temp2, CHARGE), null, width, 2, 127, 3));
                enemyList.add(addEnemy(getEnemy("grzjcg", temp, temp2, CHARGE), null, width, 1, 206, 3));
                enemyList.add(addEnemy(getEnemy("grzjcg", temp, temp2, CHARGE), null, width, 2, 211, 3));
                enemyList.add(addEnemy(getEnemy("grzjcg", temp, temp2, CHARGE), null, width, 2, 303, 3));
                enemyList.add(addEnemy(getEnemy("wssls", temp, temp2, CHARGE), null, width, 0, 313, 3));
                enemyList.add(addEnemy(getEnemy("grzjcg", temp, temp2, CHARGE), null, width, 0, 561, 3));
                enemyList.add(addEnemy(getEnemy("wssls", temp, temp2, CHARGE), null, width, 0, 590, 3));
                enemyList.add(addEnemy(getEnemy("grzjcg", temp, temp2, CHARGE), null, width, 0, 652, 3));
                enemyList.add(addEnemy(getEnemy("grzjcg", temp, temp2, CHARGE), null, width, 2, 767, 3));
                enemyList.add(addEnemy(getEnemy("wssls", temp, temp2, CHARGE), null, width, 2, 36, 4));
                enemyList.add(addEnemy(getEnemy("wssls", temp, temp2, CHARGE), null, width, 0, 200, 4));
                enemyList.add(addEnemy(getEnemy("wssls", temp, temp2, CHARGE), null, width, 0, 630, 4));
                enemyList.add(addEnemy(getEnemy("wsszkss", temp, temp2, CHARGE), null, width, 0, 949, 4));
                enemyList.add(addEnemy(getEnemy("grzjcg", temp, temp2, CHARGE), null, width, 2, 401, 5));
                enemyList.add(addEnemy(getEnemy("dgqfjr", temp, temp2, CHARGE), null, width, 1, 838, 5));
                enemyList.add(addEnemy(getEnemy("wssls", temp, temp2, CHARGE), null, width, 0, 190, 6));
                enemyList.add(addEnemy(getEnemy("wsstjz", temp, temp2, CHARGE), null, width, 2, 483, 6));
                enemyList.add(addEnemy(getEnemy("dgqfjr", temp, temp2, CHARGE), null, width, 0, 575, 6));
                enemyList.add(addEnemy(getEnemy("grzjcg", temp, temp2, CHARGE), null, width, 2, 579, 6));
                enemyList.add(addEnemy(getEnemy("dgqfjr", temp, temp2, CHARGE), null, width, 1, 619, 6));
                enemyList.add(addEnemy(getEnemy("grzjcg", temp, temp2, CHARGE), null, width, 1, 634, 6));
                enemyList.add(addEnemy(getEnemy("grzjcg", temp, temp2, CHARGE), null, width, 0, 638, 6));
                break;
        }

    }

    private static void releaseEnemy(){
        if(enemyList.size()>0 && enemyList.get(0).bigTime <= largeTime && enemyList.get(0).smallTime <= smallTime) {
            Character c = enemyList.get(0).character;
            enemyList.remove(0);
            addCharacter(c);
            c.initSkill();
            Log.d("Sam","addEnemy:"+c.charName);
        }
    }

    private void setEnemyNumberIcon(){
        enemyNumberIcon = new EnemyNumberIcon(MAINCONTEXT,enemyList.size());
        enemyNumberIcon.setId(View.generateViewId());
        stage.addView(enemyNumberIcon);
        sampleSet.clone(stage);
        sampleSet.connect(enemyNumberIcon.getId(),ConstraintSet.LEFT,stage.getId(),ConstraintSet.LEFT,0);
        sampleSet.connect(enemyNumberIcon.getId(),ConstraintSet.TOP,stage.getId(),ConstraintSet.TOP,0);
        sampleSet.connect(enemyNumberIcon.getId(),ConstraintSet.RIGHT,stage.getId(),ConstraintSet.RIGHT,0);
        sampleSet.applyTo(stage);
    }

    private EnemyMessage addEnemy(String name, int maxHp, int attack, int x, int layer, int maxBlock, int speed, int ai, int smallTime, int bigTime, int attackRange, int physicalDefend, int magicalDefend, boolean weapon, boolean canExplosion, CharacterSkill enemySkill){
        Character e1 = new Character(MAINCONTEXT,name,maxHp,attack,false,physicalDefend,magicalDefend,weapon,canExplosion,this,null);
        e1.enemySkill = enemySkill;
        e1.setId(View.generateViewId());
        CharacterMessage m1 = new CharacterMessage(x,layer,e1,maxBlock,false,speed,ai,attackRange);
        e1.message = m1;
        EnemyMessage eM1 = new EnemyMessage(e1,smallTime,bigTime);
        return eM1;
    }

    private EnemyMessage addEnemy(CharacterIconMessage m, CharacterSkill enemySkill, int x, int layer, int smallTime, int bigTime){
        Character e1 = new Character(MAINCONTEXT,m.name,(int)m.maxHp, (int)m.attack,false, (int)m.physicalDefend,m.magicalDefend,m.weapon,m.canExplosion,this,null);
        e1.enemySkill = enemySkill;
        e1.setId(View.generateViewId());
        CharacterMessage m1 = new CharacterMessage(x,layer,e1,m.maxBlock,false,m.moveSpeed,m.ai,m.attackRange);
        e1.message = m1;
        EnemyMessage eM1 = new EnemyMessage(e1,smallTime,bigTime);
        return eM1;
    }

    private CharacterIconMessage getEnemy(String name, int level, int elite, int ai){
        CharacterIconMessage m = null;
        CharacterSkill s = null;
        float atkAdd = 0;
        float hpAdd = 0;
        float defAdd = 0;
        switch(name){
            case "ysc":
                s = null;
                atkAdd = getAbilityAmplification("atk",VANGUARD);
                hpAdd = getAbilityAmplification("hp",VANGUARD);
                defAdd = getAbilityAmplification("def",VANGUARD);
                m = new CharacterIconMessage(name,"源石虫","O4",1,0,4,0,0,PHYSIC,60,1,1,550,90,ai,0,0,0,false,true,s,hpAdd,defAdd,atkAdd,VANGUARD,VANGUARD,VANGUARD);
                break;
            case "ysc2":
                s = null;
                atkAdd = getAbilityAmplification("atk",VANGUARD);
                hpAdd = getAbilityAmplification("hp",VANGUARD);
                defAdd = getAbilityAmplification("def",VANGUARD);
                m = new CharacterIconMessage(name,"源石虫2","O4",1,0,4,0,0,PHYSIC,60,1,1,570,100,ai,0,50,0,false,true,s,hpAdd,defAdd,atkAdd,VANGUARD,VANGUARD,VANGUARD);
                break;
            case "ysc3":
                s = null;
                atkAdd = getAbilityAmplification("atk",VANGUARD);
                hpAdd = getAbilityAmplification("hp",VANGUARD);
                defAdd = getAbilityAmplification("def",VANGUARD);
                m = new CharacterIconMessage(name,"源石虫3","O4",1,0,4,0,0,PHYSIC,60,1,1,600,120,ai,0,70,0,false,true,s,hpAdd,defAdd,atkAdd,VANGUARD,VANGUARD,VANGUARD);
                break;
            case "shz":
                s = null;
                atkAdd = getAbilityAmplification("atk",VANGUARD);
                hpAdd = getAbilityAmplification("hp",GUARD);
                defAdd = getAbilityAmplification("def",GUARD);
                m = new CharacterIconMessage(name,"拾荒者","O4",1,1,4,0,0,PHYSIC,60,1,2,900,170,ai,0,100,5,false,true,s,hpAdd,defAdd,atkAdd,GUARD,VANGUARD,GUARD);
                break;
            case "llz":
                s = null;
                atkAdd = getAbilityAmplification("atk", GUARD);
                hpAdd = getAbilityAmplification("hp", GUARD);
                defAdd = getAbilityAmplification("def", GUARD);
                m = new CharacterIconMessage(name,"流浪者","O4",1,1,4,0,0,PHYSIC,60,1,2,1200,200,ai,0,110,5,false,true,s,hpAdd,defAdd,atkAdd, GUARD, GUARD, GUARD);
                break;
            case "tf":
                s = null;
                atkAdd = getAbilityAmplification("atk",VANGUARD);
                hpAdd = getAbilityAmplification("hp",GUARD);
                defAdd = getAbilityAmplification("def",GUARD);
                m = new CharacterIconMessage(name,"屠夫","O4",1,1,4,0,0,PHYSIC,60,1,2,900,370,ai,0,150,10,false,true,s,hpAdd,defAdd,atkAdd,GUARD,VANGUARD,GUARD);
                break;
            case "tzls":
                s = null;
                atkAdd = getAbilityAmplification("atk",VANGUARD);
                hpAdd = getAbilityAmplification("hp",GUARD);
                defAdd = getAbilityAmplification("def",GUARD);
                m = new CharacterIconMessage(name,"屠宰老手","O4",1,2,4,0,0,PHYSIC,60,1,2,1200,420,ai,0,170,10,false,true,s,hpAdd,defAdd,atkAdd,GUARD,VANGUARD,GUARD);
                break;
            case "fmj":
                s = null;
                atkAdd = getAbilityAmplification("atk",VANGUARD);
                hpAdd = getAbilityAmplification("hp",DEFENDER);
                defAdd = getAbilityAmplification("def",DEFENDER);
                m = new CharacterIconMessage(name,"伐木机","O4",1,1,4,0,0,PHYSIC,60,1,2,1300,200,ai,0,230,10,false,true,s,hpAdd,defAdd,atkAdd,DEFENDER,VANGUARD,DEFENDER);
                break;
            case "fmls":
                s = null;
                atkAdd = getAbilityAmplification("atk",VANGUARD);
                hpAdd = getAbilityAmplification("hp",DEFENDER);
                defAdd = getAbilityAmplification("def",DEFENDER);
                m = new CharacterIconMessage(name,"伐木老手","O4",1,2,4,0,0,PHYSIC,60,1,2,1500,250,ai,0,270,10,false,true,s,hpAdd,defAdd,atkAdd,DEFENDER,VANGUARD,DEFENDER);
                break;
            case "px":
                s = null;
                atkAdd = getAbilityAmplification("atk",VANGUARD);
                hpAdd = getAbilityAmplification("hp",VANGUARD);
                defAdd = getAbilityAmplification("def",VANGUARD);
                m = new CharacterIconMessage(name,"磐蟹","O4",1,1,4,0,0,PHYSIC,60,1,1,600,150,ai,0,200,80,false,true,s,hpAdd,defAdd,atkAdd,VANGUARD,VANGUARD,VANGUARD);
                break;
            case "wzry":
                s = null;
                atkAdd = getAbilityAmplification("atk",GUARD);
                hpAdd = getAbilityAmplification("hp",GUARD);
                defAdd = getAbilityAmplification("def",DEFENDER);
                m = new CharacterIconMessage(name,"武装人员","O4",1,1,4,0,0,PHYSIC,60,1,3,1300,450,ai,0,270,0,false,true,s,hpAdd,defAdd,atkAdd,GUARD,GUARD,DEFENDER);
                break;
            case "gjwzry":
                s = null;
                atkAdd = getAbilityAmplification("atk",GUARD);
                hpAdd = getAbilityAmplification("hp",GUARD);
                defAdd = getAbilityAmplification("def",DEFENDER);
                m = new CharacterIconMessage(name,"高级武装人员","O4",1,2,4,0,0,PHYSIC,60,1,3,1500,500,ai,0,300,20,false,true,s,hpAdd,defAdd,atkAdd,GUARD,GUARD,DEFENDER);
                break;
            case "skzss":
                s = null;
                atkAdd = getAbilityAmplification("atk",CASTER);
                hpAdd = getAbilityAmplification("hp",CASTER);
                defAdd = getAbilityAmplification("def",CASTER);
                m = new CharacterIconMessage(name,"萨卡兹术士","O4",1,0,4,0,0,MAGIC,60,1,1,500,150,ai,400,50,50,false,true,s,hpAdd,defAdd,atkAdd,CASTER,CASTER,CASTER);
                break;
            case "skzdb":
                s = null;
                atkAdd = getAbilityAmplification("atk",VANGUARD);
                hpAdd = getAbilityAmplification("hp",VANGUARD);
                defAdd = getAbilityAmplification("def",VANGUARD);
                m = new CharacterIconMessage(name,"萨卡兹刀兵","O4",1,0,4,0,0,PHYSIC,60,2,1,900,250,ai,0,100,10,false,true,s,hpAdd,defAdd,atkAdd,VANGUARD,VANGUARD,VANGUARD);
                break;
            case "skzjjs":
                s = null;
                atkAdd = getAbilityAmplification("atk",SNIPER);
                hpAdd = getAbilityAmplification("hp",SNIPER);
                defAdd = getAbilityAmplification("def",SNIPER);
                m = new CharacterIconMessage(name,"萨卡兹狙击手","O4",1,1,4,0,0,PHYSIC,60,1,1,1000,180,ai,400,100,0,false,true,s,hpAdd,defAdd,atkAdd,SNIPER,SNIPER,SNIPER);
                break;
            case "skzmjs":
                s = null;
                atkAdd = getAbilityAmplification("atk", GUARD);
                hpAdd = getAbilityAmplification("hp", GUARD);
                defAdd = getAbilityAmplification("def", GUARD);
                m = new CharacterIconMessage(name,"萨卡兹魔剑士","O4",1,1,4,0,0,MAGIC,60,1,2,1200,200,ai,0,110,30,false,true,s,hpAdd,defAdd,atkAdd, GUARD, GUARD, GUARD);
                break;
            case "skzdjs":
                s = null;
                atkAdd = getAbilityAmplification("atk", GUARD);
                hpAdd = getAbilityAmplification("hp", GUARD);
                defAdd = getAbilityAmplification("def", GUARD);
                m = new CharacterIconMessage(name,"萨卡兹大剑手","O4",1,1,4,0,0,PHYSIC,60,1,2,1500,250,ai,0,130,0,false,true,s,hpAdd,defAdd,atkAdd, GUARD, GUARD, GUARD);
                break;
            case "skzmjszz":
                s = null;
                atkAdd = getAbilityAmplification("atk", GUARD);
                hpAdd = getAbilityAmplification("hp", GUARD);
                defAdd = getAbilityAmplification("def", GUARD);
                m = new CharacterIconMessage(name,"萨卡兹魔剑士组长","O4",1,2,4,0,0,MAGIC,60,1,2,1500,250,ai,0,120,40,false,true,s,hpAdd,defAdd,atkAdd, GUARD, GUARD, GUARD);
                break;
            case "dgqfjr":
                s = null;
                atkAdd = getAbilityAmplification("atk", SNIPER);
                hpAdd = getAbilityAmplification("hp", GUARD);
                defAdd = getAbilityAmplification("def", GUARD);
                m = new CharacterIconMessage(name,"帝国前锋精锐","O4",1,2,4,0,0,PHYSIC,60,1,2,1000,200,ai,100,150,20,false,true,s,hpAdd,defAdd,atkAdd, GUARD, SNIPER, GUARD);
                break;
            case "grzjcg":
                s = null;
                atkAdd = getAbilityAmplification("atk", GUARD);
                hpAdd = getAbilityAmplification("hp", VANGUARD);
                defAdd = getAbilityAmplification("def", VANGUARD);
                m = new CharacterIconMessage(name,"感染者检察官","O4",1,0,4,0,0,PHYSIC,60,2,1,1000,270,ai,0,120,10,false,true,s,hpAdd,defAdd,atkAdd, VANGUARD, GUARD, VANGUARD);
                break;
            case "wssls":
                s = null;
                atkAdd = getAbilityAmplification("atk", VANGUARD);
                hpAdd = getAbilityAmplification("hp", VANGUARD);
                defAdd = getAbilityAmplification("def", VANGUARD);
                m = new CharacterIconMessage(name,"乌萨斯裂兽","O4",1,0,4,0,0,PHYSIC,60,3,1,600,150,ai,0,100,0,false,true,s,hpAdd,defAdd,atkAdd, VANGUARD, VANGUARD, VANGUARD);
                break;
            case "wsstjz":
                s = null;
                atkAdd = getAbilityAmplification("atk", GUARD);
                hpAdd = getAbilityAmplification("hp", GUARD);
                defAdd = getAbilityAmplification("def", GUARD);
                m = new CharacterIconMessage(name,"乌萨斯突击者","O4",1,1,4,0,0,PHYSIC,60,2,2,1000,300,ai,0,20,0,false,true,s,hpAdd,defAdd,atkAdd, GUARD, GUARD, GUARD);
                break;
            case "wsszkss":
                s = null;
                atkAdd = getAbilityAmplification("atk", CASTER);
                hpAdd = getAbilityAmplification("hp", CASTER);
                defAdd = getAbilityAmplification("def", CASTER);
                m = new CharacterIconMessage(name,"乌萨斯着铠术士","O4",1,1,4,0,0,MAGIC,60,1,1,800,170,ai,400,70,50,false,true,s,hpAdd,defAdd,atkAdd, CASTER, CASTER, CASTER);
                break;
            case "bbysc":
                s = null;
                atkAdd = getAbilityAmplification("atk", VANGUARD);
                hpAdd = getAbilityAmplification("hp", VANGUARD);
                defAdd = getAbilityAmplification("def", VANGUARD);
                m = new CharacterIconMessage(name,"冰爆源石虫","O4",1,0,4,0,0,PHYSIC,60,1,2,533,183,ai,0,89,0,true,true,s,hpAdd,defAdd,atkAdd, VANGUARD, VANGUARD, VANGUARD);
                break;
            case "bbysc2":
                s = null;
                atkAdd = getAbilityAmplification("atk", VANGUARD);
                hpAdd = getAbilityAmplification("hp", VANGUARD);
                defAdd = getAbilityAmplification("def", VANGUARD);
                m = new CharacterIconMessage(name,"冰爆源石虫·α","O4",1,0,4,0,0,PHYSIC,60,1,2,533,183,ai,0,89,0,true,true,s,hpAdd,defAdd,atkAdd, VANGUARD, VANGUARD, VANGUARD);
                break;
            case "yjdlq":
                atkAdd = getAbilityAmplification("atk",VANGUARD);
                hpAdd = getAbilityAmplification("hp",VANGUARD);
                defAdd = getAbilityAmplification("def",VANGUARD);
                m = new CharacterIconMessage(name,"游击队猎犬","SI1",1,0,4,0,10,PHYSIC,30,3,1,911,212,CHARGE,0,154,5,false,true,s, hpAdd,defAdd,atkAdd,VANGUARD,VANGUARD,VANGUARD);
                break;
        }
        m.moveSpeed = -m.moveSpeed;
        m.level = Math.min(40 + 10*(elite + m.quality), level);
        m.elite = elite;
        if(m.attackRange > 0){
            m.maxBlock++;
        }
        for(int i = 0; i < elite; i++){
            int maxLevel = 40 + 10*(i + m.quality);
            m.attack += m.levelUpAtk*(maxLevel-1);
            m.physicalDefend += m.levelUpDef*(maxLevel-1);
            m.maxHp += m.levelUpHp*(maxLevel-1);
        }
        m.attack += m.levelUpAtk*(m.level-1);
        m.physicalDefend += m.levelUpDef*(m.level-1);
        m.maxHp += m.levelUpHp*(m.level-1);
        return m;
    }

    private float getAbilityAmplification(String ability, int occupation){
        switch(occupation){
            case VANGUARD:
                switch(ability){
                    case "hp":
                        return getRandom(6.0f,7.0f);
                    case "atk":
                        return getRandom(1.5f,2.5f);

                    case "def":
                        return getRandom(1.3f,2.3f);
                    default:
                }
                break;
            case GUARD:
                switch(ability){
                    case "hp":
                        return getRandom(8.0f,10.0f);
                    case "atk":
                        return getRandom(2.5f,3.5f);
                    case "def":
                        return getRandom(1.0f,1.2f);
                    default:
                }
                break;
            case DEFENDER:
                switch(ability){
                    case "hp":
                        return getRandom(9f,11f);
                    case "atk":
                        return getRandom(1.1f,1.7f);
                    case "def":
                        return getRandom(2.5f,2.9f);
                    default:
                }
                break;
            case SNIPER:
                switch(ability){
                    case "hp":
                        return getRandom(5.0f,6.0f);
                    case "atk":
                        return getRandom(2.2f,2.6f);
                    case "def":
                        return getRandom(0.6f,0.9f);
                    default:
                }
                break;
            case CASTER:
                switch(ability){
                    case "hp":
                        return getRandom(5.0f,6.0f);
                    case "atk":
                        return getRandom(1.8f,2.5f);
                    case "def":
                        return getRandom(0.6f,0.9f);
                    default:
                }
                break;
            default:
        }
        return -1;
    }

    private float getRandom(float min, float max){
        int temp = (int)((Math.random()*(max*10-min*10)+1) + min*10);
        return 0.1f*temp;
    }

    private static void changeFloatCostRevivalTime(){
        for(int i = 0; i < 4; i++){
            if(floatCostRevivalTime[i] > 0){
                floatCostRevivalTime[i]--;
                if(floatCostRevivalTime[i] == 0){
                    floatCostList.get(i).setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initStageOnTouch(){
        stage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!isPause){
                    int temp = (CALCULATE1 + (int)event.getX())/ICONWIDTH;
                    if(CALCULATE1 + (int)event.getX() < 0) temp = -1;
                        switch1:switch(event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                if(chooseIcon == -1 && (int)event.getY() < MainActivity.SCREENHEIGHT-ICONWIDTH){
                                    for(CharacterMessage m : characterQueue){
                                        if(m.relationship && (event.getX() < m.x + (int)(MainActivity.CharWidth/4)) && (event.getX() > m.x - (int)(MainActivity.CharWidth/4))){
                                            if(event.getY() > (int)(LastLinePosition - m.layer*100 - MainActivity.CharHeight/7*3) && event.getY() < (int)(LastLinePosition - m.layer*100 + MainActivity.CharHeight/7)){
                                                changeSpeed(0.2f);
                                                chooseIcon = m.character.icon.id;
                                                m.character.icon.onChosen();
                                                break switch1;
                                            }
                                        }
                                    }
                                }
                                if((temp == chooseIcon && (int)event.getY() >= MainActivity.SCREENHEIGHT-ICONWIDTH) || temp < 0){
                                    if(chooseIcon > -1)iconList.get(chooseIcon).onCancel();
                                    changeSpeed(formSpeed);
                                    chooseIcon = -1;
                                    break;
                                }
                                if(chooseIcon > -1){
                                    if((int)event.getY() < MainActivity.SCREENHEIGHT-ICONWIDTH && iconList.get(chooseIcon).state != CharacterBattleIcon.AVAILABLE){
                                        if(chooseIcon > -1)iconList.get(chooseIcon).onCancel();
                                        changeSpeed(formSpeed);
                                        chooseIcon = -1;
                                        break;
                                    }
                                }
                                if((int)event.getY() >= MainActivity.SCREENHEIGHT-ICONWIDTH){
                                    if(temp != chooseIcon && temp < iconList.size()){
                                        if(chooseIcon > -1)iconList.get(chooseIcon).onCancel();
                                        changeSpeed(0.2f);
                                        chooseIcon = temp;
                                        if(chooseIcon > -1)iconList.get(chooseIcon).onChosen();
                                    }
                                }
                            case MotionEvent.ACTION_MOVE:
                                if(chooseIcon != -1 && (int)event.getY() < LastLinePosition+50 && (int)event.getY() > LastLinePosition-350){
                                    if(iconList.get(chooseIcon).state == CharacterBattleIcon.AVAILABLE){
                                        if(characterOnDragState == DISPLAY){
                                            CharacterIconMessage m = MainActivity.mChooseList.get(chooseIcon);
                                            characterOnDrag = addCharacterImage(0,(LastLinePosition+50-(int)event.getY())/100,m.name,m.moveSpeed,m.maxBlock,true, m.getAbility(HP),m.getAbility(ATK),m.ai, chooseIcon, m.attackRange,m.getAbility(DEF),m.magicalDefend,m.weapon, m.canExplosion);
                                            characterOnDrag.idle("right");
                                            characterOnDragState = ONCREATE;
                                            pauseButton.setClickable(false);
                                            break;
                                        }
                                        if(characterOnDragState == ONCREATE){
                                            if((int)event.getY() < LastLinePosition+50 && (int)event.getY() > LastLinePosition-350){
                                                if((int)event.getY() < LastLinePosition-50 - characterOnDrag.message.layer*100 || (int)event.getY() > LastLinePosition+50 - characterOnDrag.message.layer*100){
                                                    sampleSet.clone(stage);
                                                    sampleSet.setMargin(characterOnDrag.getId(),3,LastLinePosition - (LastLinePosition+50-(int)event.getY())/100*100 - MainActivity.CharHeight/7*6);
                                                    sampleSet.setMargin(R.id.highLight,3,LastLinePosition - (LastLinePosition+50-(int)event.getY())/100*100 - MainActivity.CharHeight/7);
                                                    sampleSet.applyTo(stage);
                                                    characterOnDrag.message.layer = (LastLinePosition+50-(int)event.getY())/100;
                                                }
                                            }
                                        }
                                    }
                                }else if(characterOnDragState == ONCREATE){
                                    stage.removeView(characterOnDrag);
                                    highLight.setVisibility(View.INVISIBLE);
                                    characterOnDragState = DISPLAY;
                                    pauseButton.setClickable(true);
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                if(characterOnDragState == ONCREATE){
                                    if((int)event.getY() < LastLinePosition+50 && (int)event.getY() > LastLinePosition-350){
                                        ((MainActivity)MAINCONTEXT).playSound(MainActivity.voiceCharacterSet,1.0f,1.0f);
                                        stage.removeView(characterOnDrag);
                                        Character tempcharacter = addCharacter(characterOnDrag);
                                        tempcharacter.icon = iconList.get(chooseIcon);
                                        tempcharacter.icon.character = tempcharacter;
                                        tempcharacter.initSkill();
                                        if(tempcharacter.icon.characterSkill != null){
                                            tempcharacter.icon.characterSkill.initSkill();
                                        }
                                        characterOnDragState = DISPLAY;
                                        pauseButton.setClickable(true);
                                        highLight.setVisibility(View.INVISIBLE);
                                        costIcon.addCost(-iconList.get(chooseIcon).cost);
                                        iconList.get(chooseIcon).changeState(CharacterBattleIcon.ONBATTLE);
                                        if(chooseIcon != -1)iconList.get(chooseIcon).onCancel();
                                        changeSpeed(formSpeed);
                                        chooseIcon = -1;
                                    }else{
                                        stage.removeView(characterOnDrag);
                                        highLight.setVisibility(View.INVISIBLE);
                                        characterOnDragState = DISPLAY;
                                        pauseButton.setClickable(true);
                                    }
                                }
                                break;
                            default:
                        }

                }
                return true;
            }
        });
        pauseButton.setSoundEffectsEnabled(false);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)MAINCONTEXT).playSound(MainActivity.voiceDetailCheck,1.0f,1.0f);
                pause();
            }
        });
        speedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chooseIcon == -1 && !isPause){
                    ((MainActivity)MAINCONTEXT).playSound(MainActivity.voiceDetailCheck,1.0f,1.0f);
                    changeSpeed();
                }

            }
        });
        ConstraintSet sampleSet = new ConstraintSet();
        sampleSet.clone(stage);//克隆一个father的Constrainset副本，father为父ConstraintLayout布局的id
        for(int i = 0; i < 4; i++){
            sampleSet.connect(floatCostList.get(i).getId(),ConstraintSet.RIGHT,stage.getId(),ConstraintSet.RIGHT,floatCostAdvance[i]*FLOATCOSTPACE);
            floatCostList.get(i).setVisibility(View.VISIBLE);
        }
        sampleSet.applyTo(stage);//应用该副本
    }

    private void initCharacterIconList(){
        addCharacterIconToList();
    }

    private void pause(){
        if(isVictory==0){
            if(isPause){
                for(CharacterMessage c : characterQueue){
                    c.character.startPlayingGif();
                }
                timer = new Timer();
                setTimerTask();
                timer.schedule(task, 0,(int)(30/playSpeed));
                pauseButton.setImageResource(R.drawable.start);
                pauseConstraintLayout.setVisibility(View.INVISIBLE);
                isPause = false;
            }else{
                for(CharacterMessage c : characterQueue){
                    c.character.stopPlayingGif();
                }
                timer.cancel();
                pauseButton.setImageResource(R.drawable.pause);
                pauseConstraintLayout.setVisibility(View.VISIBLE);
                isPause = true;
            }
        }
    }

    private void initSound(){
        if(((MainActivity)MAINCONTEXT).backgroundSound != null){
            ((MainActivity)MAINCONTEXT).backgroundSound.destroy();
            ((MainActivity)MAINCONTEXT).backgroundSound = null;
        }
        ((MainActivity)MAINCONTEXT).backgroundSound = new BackgroundSound(R.raw.qiechengintro,R.raw.qiechengloop,((MainActivity)MAINCONTEXT));
    }

    private void setTimerTask(){
        task = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                if(characterNumber < MAXCHARACTERNUMBER){
                    if(smallTime < 1000){
                        smallTime++;
                        //Log.d("Sam","smallTime "+smallTime);
                    }else{
                        smallTime = 0;
                        largeTime++;
                        //Log.d("Sam","LargeTime "+largeTime);
                    }
                }
                if(enemyList.size() > 0){
                    if(enemyList.get(0).bigTime <= largeTime && enemyList.get(0).smallTime <= smallTime){
                        Message msg2 = new Message();
                        msg2.what = 2;
                        handler.sendMessage(msg2);
                    }
                }
            }
        };
    }

    private void initColumn(){
        stateColumnRetreat.setSoundEffectsEnabled(false);
        stateColumnRetreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)MAINCONTEXT).playSound(MainActivity.voiceDetailCheck,1.0f,1.0f);
                if(chooseIcon > -1){
                    iconList.get(chooseIcon).character.hp = 0;
                    iconList.get(chooseIcon).character.onCancel();
                    dropCharacter(iconList.get(chooseIcon).character.message);
                    //Log.d("Sam",characterQueue.size()+" characters are on the screen");
                }
            }
        });
        stateColumnSkill.setSoundEffectsEnabled(false);
        stateColumnSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chooseIcon > -1 && iconList.get(chooseIcon).characterSkill.skillState==READY){
                    ((MainActivity)MAINCONTEXT).playSound(MainActivity.voiceSkillStart,0.2f,0.2f);
                    iconList.get(chooseIcon).characterSkill.skillState = USING;
                }
            }
        });
    }

    public void setOutCome(boolean isVictory){
        /*AlertDialog.Builder dialog = new AlertDialog.Builder(MAINCONTEXT);//注意getApplicationContext不行，会报错，只能用这个
        dialog.setTitle("战斗结束");//标题
        ((MainActivity)MAINCONTEXT).playSound(isVictory? MainActivity.voiceWin: MainActivity.voiceLose, 1,1);
        ((MainActivity)MAINCONTEXT).backgroundSound.endMusic();
        if(isVictory && this.level != 2){
            dialog.setPositiveButton("下一关", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    timer.cancel();
                    ItemOperation itemOperation = new ItemOperation(MAINCONTEXT);
                    ItemDropRate rate = new ItemDropRate(50,3, MainActivity.itemModelList.get(7));
                    ItemDropRate rate2 = new ItemDropRate(70,1, MainActivity.itemModelList.get(8));
                    itemOperation.addItemDropRate(rate).addItemDropRate(rate2);
                    ((MainActivity)MAINCONTEXT).operation = new Operation("爆炸生物在死亡时会对周围敌人造成十倍的伤害。","乌萨斯雪原","伏尔加河畔",2,12,itemOperation,MAINCONTEXT);
                    ((MainActivity)MAINCONTEXT).setFragment(MainActivity.CHOOSEFRAGMENT);
                }
            });
        }
        dialog.setMessage(isVictory ? "你赢了！":"你输了。");//正文
        dialog.setCancelable(false);//是否能点击屏幕取消该弹窗
        dialog.show();*/
        ((MainActivity)MAINCONTEXT).playSound(isVictory? MainActivity.voiceWin: MainActivity.voiceLose, 1,1);

        ((MainActivity)MAINCONTEXT).backgroundSound.endMusic(1500);
        if(isVictory){
            endAnimation();
        }else{
            ((MainActivity)MAINCONTEXT).playSound(MainActivity.voiceDanger, 1,1);
            for(CharacterMessage c : characterQueue){
                c.character.stopPlayingGif();
            }
            timer.cancel();
            isPause = true;
            failBackground.setVisibility(View.VISIBLE);
            failBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeSpeed(1);
                    if(timer != null)timer.cancel();
                    handler.removeCallbacksAndMessages(null);
                    ((MainActivity) MAINCONTEXT).setFragment(MAINMANU);
                }
            });
        }
    }

    private void updateStateColumn(){
        if(isStateColumnShow){
            CharacterSkill tmp = iconList.get(chooseIcon).characterSkill;
            if(tmp != null){
                stateColumnSkill.setImage(tmp.skill.skillImage);
                stateColumnSkill.updateEnergy(tmp.energy,tmp.skillPoint,tmp.skillState,tmp.leftoverTime,tmp.duration);
            }
        }
    }

    private void initStaticThing(){
        smallTime = 0;
        largeTime = 0;
        isStateColumnShow = false;
        isVictory = 0;
        characterQueue = new ArrayList<>();
        chooseIcon = -1;
        iconList = new ArrayList<>();
        layerList = new ArrayList<>();
        enemyList = new ArrayList<>();
        floatCostList = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            floatCostRevivalTime[i] = 0;
            floatCostAdvance[i] = 0;
        }
        characterNumber = 0;
        characterOnDragState = DISPLAY;
        Log.d("Sam","battleStageInit");
    }

    private void endAnimation(){
        for(CharacterMessage c : characterQueue){
            c.character.stopPlayingGif();
        }
        timer.cancel();
        isPause = true;

        endBlack.setVisibility(View.VISIBLE);
        endBlack.setAlpha(1f);
        endGifImageView.setVisibility(View.VISIBLE);
        try{
            GifDrawable tmp = new GifDrawable(getResources(), R.drawable.mission_accomplish);
            tmp.addAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationCompleted(int loopNumber) {
                    endGifImageView.setVisibility(View.INVISIBLE);
                    endGifImageView.clearAnimation();
                    AlphaAnimation animation = new AlphaAnimation(1,0);
                    animation.setDuration(500);
                    endBlack.setAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            endBlack.setAlpha(0f);
                            endBlack.setVisibility(View.INVISIBLE);
                            ((MainActivity)MAINCONTEXT).operation.star = 3;
                            ((MainActivity)MAINCONTEXT).setFragment(MainActivity.BATTLEENDFRAGMENT);

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    animation.start();
                }
            });
            endGifImageView.setImageDrawable(tmp);
            endGifImageView.setAlpha(1f);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void changeSpeed(){
        if((playSpeed - 1f) < 0.01f){
            speedButton.setImageResource(R.drawable.speed2);
            playSpeed = 2;
            for(CharacterMessage c : characterQueue){
                c.character.changeSpeed(2);
            }
        }else{
            speedButton.setImageResource(R.drawable.speed1);
            playSpeed = 1;
            for(CharacterMessage c : characterQueue){
                c.character.changeSpeed(1);
            }
        }
        formSpeed = playSpeed;
        timer.cancel();
        timer = new Timer();
        setTimerTask();
        timer.schedule(task, 0,(int)(30/playSpeed));
    }

    private void changeSpeed(float speed){
        playSpeed = speed;
        for(CharacterMessage c : characterQueue){
            c.character.changeSpeed(speed);
        }
        timer.cancel();
        timer = new Timer();
        setTimerTask();
        timer.schedule(task, 0,(int)(30/playSpeed));
        Log.d("Sam",formSpeed+"");
    }

    @Override
    public void onDetach() {
        changeSpeed(1);
        super.onDetach();
        if(timer != null)timer.cancel();
        handler.removeCallbacksAndMessages(null);
        Log.d("Sam","BattleDetached!");
    }

    public void setEnemyList(int mode, int Lv){

    }
}

class CharacterMessage{
    int x;
    int ai;
    int layer;//0-3
    int speedx;
    int icon;
    Character character;
    int maxBlock;
    boolean relationship;
    int attackRange;
    String state ;//attack moving checking dying standing
    ArrayList<CharacterMessage> block = new ArrayList<>();

    public CharacterMessage(int x, int layer, Character character, int maxBlock, boolean relationship, int speedx, int ai, int attackRange) {
        this.x = x;
        this.layer = layer;
        this.character = character;
        this.maxBlock = maxBlock;
        this.relationship = relationship;
        this.speedx = speedx;
        this.attackRange = attackRange;
        switch(ai){
            case BattleFragment.KEEPSTANDING:
                this.state = "standing";
                Log.d("Sam",character.charName + " " + state + " " + getBlockLength() + " 5");
                break;
            case BattleFragment.CHARGE:
                this.state = "moving";
                Log.d("Sam",character.charName + " " + state + " " + getBlockLength() + " 6");
                break;
        }
        this.ai = ai;
    }

    public Character getAttackTarget(){
        if (getBlockLength()>0){
            return block.get(0).character;
        }
        return null;
    }

    public void clearBlock(){
        for(int i = 0; i < getBlockLength(); i++){
            if(block.get(i).character.hp<=0){
                block.remove(i);
                i--;
            }
        }
    }

    public int getBlockLength(){
        return block.size();
    }

}

class CharacterIconMessage implements Comparable{
    String name;
    int level;
    int elite;
    int cost;
    boolean weapon;
    float revivalTime;
    int moveSpeed;
    int maxBlock;
    float maxHp;
    float attack;
    int ai;
    int attackRange;
    float physicalDefend;
    int magicalDefend;
    int maxLevel;
    int status;
    int status2;
    int teamNumber = -1;
    int quality;//0-2
    int position = -1;
    float levelUpAtk;
    float levelUpDef;
    float levelUpHp;
    boolean canExplosion;
    boolean retreatable;
    int levelUpItemId; //0-5
    int hpLevelUpOccupation;
    int atkLevelUpOccupation;
    int defLevelUpOccupation;
    /*  数字    材料id  高级材料id
        0       13      37
        1       17      39
        2       21      41
        3       25      43
        4       29      45
        5       33      47

     每次升级使用材料数量：
        品质  0   1   2   3   4   2   3
        0-1       8   3
        1-2           8           5
        2-3               4           4
     */
    String realName;
    String codeName;
    CharacterSkill characterSkill;
    CharacterSquare characterSquare;
    Character character;
    boolean inChooseList = false;
    public CharacterIconMessage(String name, String realName, String codeName, int level, int quality, int status, int elite, int cost, boolean weapon, float revivalTime,int moveSpeed, int maxBlock, int maxHp, int attack, int ai, int attackRange, int physicalDefend, int magicalDefend, boolean canExplosion, boolean retreatable, CharacterSkill characterSkill, int levelUpItemId, float levelUpHp, float levelUpDef, float levelUpAtk, int hpLevelUpOccupation, int atkLevelUpOccupation, int defLevelUpOccupation){
        this.name = name;
        this.level = level;
        this.elite = elite;
        this.cost = cost;
        this.weapon = weapon;
        this.revivalTime = revivalTime;
        this.moveSpeed = moveSpeed;
        this.maxBlock = maxBlock;
        this.maxHp = maxHp;
        this.attack = attack;
        this.ai = ai;
        this.attackRange = attackRange;
        this.physicalDefend = physicalDefend;
        this.magicalDefend = magicalDefend;
        this.canExplosion = canExplosion;
        this.retreatable = retreatable;
        this.characterSkill = characterSkill;
        this.realName = realName;
        this.codeName = codeName;
        this.quality = quality;
        this.maxLevel = 40 + 10*(elite + quality);
        this.status = status;
        this.levelUpItemId = levelUpItemId;
        status2 = status;
        this.levelUpAtk = levelUpAtk;
        this.levelUpDef = levelUpDef;
        this.levelUpHp = levelUpHp;
        this.hpLevelUpOccupation = hpLevelUpOccupation;
        this.atkLevelUpOccupation = atkLevelUpOccupation;
        this.defLevelUpOccupation = defLevelUpOccupation;
    }

    public CharacterIconMessage(String name, String realName, String codeName, int level, int quality, int status, int elite, int cost, boolean weapon, float revivalTime,int moveSpeed, int maxBlock, int maxHp, int attack, int ai, int attackRange, int physicalDefend, int magicalDefend, boolean canExplosion, boolean retreatable, CharacterSkill characterSkill, float levelUpHp, float levelUpDef, float levelUpAtk, int hpLevelUpOccupation, int atkLevelUpOccupation, int defLevelUpOccupation){
        this(name, realName, codeName, level, quality, status, elite, cost, weapon, revivalTime,moveSpeed, maxBlock, maxHp, attack, ai, attackRange, physicalDefend, magicalDefend, canExplosion, retreatable, characterSkill , (int)(Math.random()*6),levelUpHp,levelUpDef,levelUpAtk,hpLevelUpOccupation,atkLevelUpOccupation,defLevelUpOccupation);
    }

    @Override
    public int compareTo(Object o){
        CharacterIconMessage m = (CharacterIconMessage) o;
        if(m.cost > this.cost){
            return -1;
        }else if(m.cost < this.cost){
            return 1;
        }else{
            return 0;
        }
    }

    private float getAbilityAmplification(String ability, int occupation){
        switch(occupation){
            case VANGUARD:
                switch(ability){
                    case "hp":
                        return getRandom(6.0f,7.0f);
                    case "atk":
                        return getRandom(1.5f,2.5f);
                    case "def":
                        return getRandom(1.3f,2.3f);
                    default:
                }
                break;
            case GUARD:
                switch(ability){
                    case "hp":
                        return getRandom(8.0f,10.0f);
                    case "atk":
                        return getRandom(2.5f,3.5f);
                    case "def":
                        return getRandom(1.0f,1.2f);
                    default:
                }
                break;
            case DEFENDER:
                switch(ability){
                    case "hp":
                        return getRandom(9f,11f);
                    case "atk":
                        return getRandom(1.1f,1.7f);
                    case "def":
                        return getRandom(2.5f,2.9f);
                    default:
                }
                break;
            case SNIPER:
                switch(ability){
                    case "hp":
                        return getRandom(5.0f,6.0f);
                    case "atk":
                        return getRandom(2.2f,2.6f);
                    case "def":
                        return getRandom(0.6f,0.9f);
                    default:
                }
                break;
            case CASTER:
                switch(ability){
                    case "hp":
                        return getRandom(5.0f,6.0f);
                    case "atk":
                        return getRandom(1.8f,2.5f);
                    case "def":
                        return getRandom(0.6f,0.9f);
                    default:
                }
                break;
            default:
        }
        return -1;
    }

    private float getRandom(float min, float max){
        int temp = (int)((Math.random()*(max*10-min*10)+1) + min*10);
        return 0.1f*temp;
    }

    public int getAbility(int ability){
        switch(ability){
            case ATK:
                return (int)attack;
            case DEF:
                return (int)physicalDefend;
            case HP:
                return (int)maxHp;
            default:
                return 0;
        }
    }

    public int getPromotionItem(int promotionFromElite, boolean isProItem){
        int itemId = 13 + levelUpItemId*4;
        int proItemId = 37 + levelUpItemId*2;
        switch(promotionFromElite){
            case 0:
                return isProItem ? itemId + 2 : itemId + 1;
            case 1:
                return isProItem ? proItemId : itemId + 2;
            case 2:
                return isProItem ? proItemId + 1 : itemId + 3;
            default:
                return -1;
        }
    }

    public int getPromotionItemNumber(int promotionFromElite, boolean isProItem){
        switch(promotionFromElite){
            case 0:
                return isProItem ? 3:8;
            case 1:
                return isProItem ? 5:8;
            case 2:
                return isProItem ? 4:4;
            default:
                return -1;
        }
    }

}

class EnemyMessage{
    Character character;
    int smallTime;
    int bigTime;
    public EnemyMessage(Character character, int smallTime, int bigTime){
        this.character = character;
        this.smallTime = smallTime;
        this.bigTime = bigTime;
    }
}

class CharacterSkill{
    public static final int CHARGING = 1;
    public static final int READY = 2;
    public static final int USING = 3;

    int skillId;
    Skill skill;
    int lv;
    int skillState;
    int energy;//技能发动期间，能量始终为0
    int leftoverTime;//技能准备期间，剩余时间等于最大时间
    int affect;
    int initSkillPoint;
    int skillPoint;
    int duration;
    Character character;//在Character的initSkill里注册，不区分敌友'
    MainActivity mainActivity;
    public CharacterSkill(Skill skill, int lv, int skillId, MainActivity mainActivity){
        this.skill = skill;
        this.lv = lv;
        skill.initSkill(lv);
        this.affect = skill.affect;
        this.initSkillPoint = skill.initSkillPoint;
        this.skillPoint = skill.skillPoint;
        this.duration = skill.duration;
        this.skillId = skillId;
        this.mainActivity = mainActivity;
        energy = initSkillPoint;
        skillState = (energy == skill.skillPoint)? READY:CHARGING;
        leftoverTime = skill.duration;
    }
    public String getCharacterSkillIllustration(){
        skill.getAffect(lv);
        return skill.getIllustration();
    }

    public void updateCharacterSkill(){
        switch(skillState){
            case READY:
                if(leftoverTime != duration) leftoverTime = duration;
                if(skill.autoRelease){
                    mainActivity.playSound(MainActivity.voiceSkillStart,0.5f,0.5f);
                    skillState = USING;
                }
                break;
            case CHARGING:
                // increase please put energy++ out of the function
                if(leftoverTime != duration) leftoverTime = duration;
                if(energy >= skillPoint){
                    mainActivity.playSound(MainActivity.voiceSkillReady,0.5f,0.5f);
                    energy = skillPoint;
                    character.updateEnergy(energy,skillPoint,skillState,leftoverTime,duration);
                    skillState = READY;
                    if(skill.autoRelease){
                        mainActivity.playSound(MainActivity.voiceSkillStart,0.5f,0.5f);
                        skillState = USING;
                    }
                }
                break;
            case USING:
                if(energy > 0) energy = 0;
                leftoverTime--;
                if(leftoverTime <= 0){
                    skillState = CHARGING;
                }
                switch(skillId){
                    case 6: case 7: case 8:
                        if(character != null){
                            if(character.hp + affect >= character.maxHp){
                                character.hp = character.maxHp;
                            }else{
                                character.hp += affect;
                            }
                            character.updateHp();
                        }
                        break;
                    default:
                }
                break;
        }

    }

    public void initSkill(){
        energy = initSkillPoint;
        skillState = (energy == skill.skillPoint)? READY:CHARGING;
        leftoverTime = skill.duration;
    }
}