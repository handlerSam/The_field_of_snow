package com.example.myapplication;

import android.content.Context;
import android.content.res.Resources;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import static com.example.myapplication.CharacterFragment.ATK;
import static com.example.myapplication.CharacterFragment.DEF;
import static com.example.myapplication.CharacterFragment.HP;

public class CharacterBattleIcon extends ConstraintLayout {
    public static final int UNAVAILABLE = 0;
    public static final int AVAILABLE = 1;
    public static final int DIE = 2;
    public static final int ONBATTLE = 3;
    public static final boolean PHYSIC = true;
    public static final boolean MAGIC = false;
    public static ConstraintSet sampleSet= new ConstraintSet();

    String name;
    int level;
    int elite;
    int cost;
    boolean weapon;
    float revivalTime;
    Context context;
    int id;//在List中是第几个
    int state = UNAVAILABLE;
    float time;
    boolean retreatable;
    Character character;

    ConstraintLayout battleIconStage;
    ImageView battleIconCharacterImage;
    ImageView battleIconElite;
    TextView battleIconLv;
    ConstraintLayout battleIconState;
    ImageView battleIconWeapon;
    TextView battleIconRevival;
    TextView battleIconCost;
    ImageView battleIconOnBattle;
    ConstraintLayout parent;
    CharacterSkill characterSkill = null;
    CharacterIconMessage iconMessage;
    BattleFragment battleFragment;
    public CharacterBattleIcon(Context context, String name, int level, int elite, int cost, boolean weapon, float revivalTime, boolean retreatable, CharacterSkill characterSkill, BattleFragment battleFragment, CharacterIconMessage iconMessage) {
        super(context);
        View v = LayoutInflater.from(context).inflate(R.layout.character_battle_icon,CharacterBattleIcon.this);
        this.name = name;
        this.level = level;
        this.elite = elite;
        this.cost = cost;
        this.weapon = weapon;
        this.revivalTime = revivalTime;
        this.context = context;
        this.retreatable = retreatable;
        this.characterSkill = characterSkill;
        this.battleFragment = battleFragment;
        this.iconMessage = iconMessage;
        time = revivalTime;
        init(v);
        if(battleFragment != null){
            changeState(UNAVAILABLE);
        }
    }

    public void init(View v){
        this.battleIconStage = (ConstraintLayout) v.findViewById(R.id.battleIconStage);
        this.battleIconCharacterImage = (ImageView) v.findViewById(R.id.battleIconCharacterImage);
        this.battleIconElite = (ImageView) v.findViewById(R.id.battleIconElite);
        this.battleIconLv = (TextView) v.findViewById(R.id.battleIconLv);
        this.battleIconState = (ConstraintLayout) v.findViewById(R.id.battleIconState);
        this.battleIconWeapon = (ImageView) v.findViewById(R.id.battleIconWeapon);
        this.battleIconRevival = (TextView) v.findViewById(R.id.battleIconRevival);
        this.battleIconCost = (TextView) v.findViewById(R.id.battleIconCost);
        this.battleIconOnBattle = (ImageView) v.findViewById(R.id.battleIconOnbattle);
        this.parent = (ConstraintLayout) v.findViewById(R.id.parent);
        battleIconCharacterImage.setImageResource(getResourceByString(name+"icon"));
        battleIconElite.setImageResource(getResourceByString("elite"+elite));
        battleIconLv.setText("LV"+level);
        battleIconCost.setText(String.valueOf(cost));
        battleIconWeapon.setImageResource(getResourceByString(weapon == MAGIC? "magicbattleicon":"physicbattleicon"));
    }
    public void changeState(int state){
        //state onBattle revival
        this.state = state;
        if((state == ONBATTLE || state == DIE) && BattleFragment.chooseIcon == this.id){
            battleFragment.stateColumn.setVisibility(INVISIBLE);
            battleFragment.isStateColumnShow = false;
        }
        switch(state){
            case UNAVAILABLE:
                battleIconState.setVisibility(VISIBLE);
                battleIconState.setBackgroundResource(R.color.colorUnavailable);
                battleIconOnBattle.setVisibility(INVISIBLE);
                battleIconRevival.setVisibility(INVISIBLE);
                break;
            case AVAILABLE:
                battleIconState.setBackgroundResource(R.color.transparent);
                battleIconOnBattle.setVisibility(INVISIBLE);
                battleIconRevival.setVisibility(INVISIBLE);
                break;
            case ONBATTLE:
                battleIconState.setVisibility(VISIBLE);
                battleIconState.setBackgroundResource(R.color.colorUnavailable);
                battleIconOnBattle.setVisibility(VISIBLE);
                battleIconRevival.setVisibility(INVISIBLE);
                break;
            case DIE:
                battleIconState.setVisibility(VISIBLE);
                battleIconState.setBackgroundResource(R.color.colorDie);
                battleIconOnBattle.setVisibility(INVISIBLE);
                battleIconRevival.setVisibility(VISIBLE);
                battleIconRevival.setText(String.valueOf(revivalTime));
                time = revivalTime;
                break;
            default:
        }
    }
    public void updateState(){
        //state onBattle revival
        if(BattleFragment.chooseIcon == this.id){
            battleFragment.stateColumn.setVisibility(INVISIBLE);
            battleFragment.isStateColumnShow = false;
        }
        switch(state){
            case UNAVAILABLE:
                battleIconState.setVisibility(VISIBLE);
                battleIconState.setBackgroundResource(R.color.colorUnavailable);
                battleIconOnBattle.setVisibility(INVISIBLE);
                battleIconRevival.setVisibility(INVISIBLE);
                break;
            case AVAILABLE:
                battleIconState.setBackgroundResource(R.color.transparent);
                battleIconOnBattle.setVisibility(INVISIBLE);
                battleIconRevival.setVisibility(INVISIBLE);
                break;
            case ONBATTLE:
                battleIconState.setVisibility(VISIBLE);
                battleIconState.setBackgroundResource(R.color.colorUnavailable);
                battleIconOnBattle.setVisibility(VISIBLE);
                battleIconRevival.setVisibility(INVISIBLE);
                break;
            case DIE:
                battleIconState.setVisibility(VISIBLE);
                battleIconState.setBackgroundResource(R.color.colorDie);
                battleIconOnBattle.setVisibility(INVISIBLE);
                battleIconRevival.setVisibility(VISIBLE);
                break;
            default:
        }
    }
    private int getResourceByString(String str){
        Resources res = context.getResources();
        int resourceId = res.getIdentifier(str,"drawable",context.getPackageName());
        return resourceId;
    }
    public void onChosen(){
        sampleSet.clone(parent);
        sampleSet.setMargin(battleIconStage.getId(),3,0);
        sampleSet.applyTo(parent);
        if(state == ONBATTLE){
            battleFragment.stateColumnIcon.setImageResource(getResourceByString(name+"icon"));
            battleFragment.stateColumn.setVisibility(VISIBLE);
            battleFragment.isStateColumnShow = true;
            battleFragment.stateColumnHp.setText("生命  "+character.hp+"/"+character.maxHp);
            battleFragment.stateColumnAttack.setText("攻击  "+character.attack);
            battleFragment.stateColumnDefend.setText("防御  " + character.physicalDefend);
            battleFragment.stateColumnMagicalDefend.setText("法抗  "+ character.magicalDefend);
            battleFragment.stateColumnBlock.setText("阻挡  "+ character.message.maxBlock);
            CharacterIconMessage m = MainActivity.mChooseList.get(BattleFragment.chooseIcon);
            String tmp = "";
            switch(m.status){
                case -1:
                    tmp = "死亡";
                    battleFragment.stateColumnHurtStateBefore.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.redrect));
                    break;
                case 0:
                    tmp = "濒危";
                    battleFragment.stateColumnHurtStateBefore.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.redrect));
                    break;
                case 1:
                    tmp = "重伤";
                    battleFragment.stateColumnHurtStateBefore.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.orangerect));
                    break;
                case 2:
                    tmp = "受伤";
                    battleFragment.stateColumnHurtStateBefore.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.pinkrect));
                    break;
                case 3:
                    tmp = "轻伤";
                    battleFragment.stateColumnHurtStateBefore.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.yellowrect));
                    break;
                case 4:
                    tmp = "健康";
                    battleFragment.stateColumnHurtStateBefore.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.greenrect));
                    break;
            }
            battleFragment.stateColumnHurtStateBefore.setText(tmp);
            tmp = "";
            switch(m.status2){
                case -1:
                    tmp = "死亡";
                    battleFragment.stateColumnHurtStateAfter.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.redrect));
                    break;
                case 0:
                    tmp = "濒危";
                    battleFragment.stateColumnHurtStateAfter.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.redrect));
                    break;
                case 1:
                    tmp = "重伤";
                    battleFragment.stateColumnHurtStateAfter.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.orangerect));
                    break;
                case 2:
                    tmp = "受伤";
                    battleFragment.stateColumnHurtStateAfter.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.pinkrect));
                    break;
                case 3:
                    tmp = "轻伤";
                    battleFragment.stateColumnHurtStateAfter.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.yellowrect));
                    break;
                case 4:
                    tmp = "健康";
                    battleFragment.stateColumnHurtStateAfter.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.greenrect));
                    break;
            }
            battleFragment.stateColumnHurtStateAfter.setText(tmp);


            if(retreatable && state == ONBATTLE){
                battleFragment.stateColumnRetreat.setVisibility(VISIBLE);
            }else{
                battleFragment.stateColumnRetreat.setVisibility(GONE);
            }
            if(characterSkill != null){
                battleFragment.stateColumnSkill.setVisibility(true);
                battleFragment.stateColumnSKillName.setText(character.icon.characterSkill.skill.name);
                String IllusTmp = character.icon.characterSkill.getCharacterSkillIllustration();
                battleFragment.stateColumnSkillIllustration.setText(IllusTmp);
                String str = "";
                switch(character.icon.characterSkill.skill.increaseMethod){
                    case Skill.ATTACK:
                        str = "攻击";
                        battleFragment.stateColumnSkillChargeMethod.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.redrect));
                        break;
                    case Skill.AUTO:
                        str = "自然";
                        battleFragment.stateColumnSkillChargeMethod.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.greenrect));
                        break;
                    case Skill.DEFEND:
                        str = "防御";
                        battleFragment.stateColumnSkillChargeMethod.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.yellowrect));
                        break;
                    case Skill.MOVING:
                        str = "移动";
                        battleFragment.stateColumnSkillChargeMethod.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.pinkrect));
                        break;
                    default:
                }
                battleFragment.stateColumnSkillChargeMethod.setText(str);
                battleFragment.stateColumnSkillReleaseMethod.setText(character.icon.characterSkill.skill.autoRelease? "自动":"手动");
            }else{
                battleFragment.stateColumnSkill.setVisibility(false);
            }
            character.onChosen();
        }else{
            CharacterIconMessage m = MainActivity.mChooseList.get(BattleFragment.chooseIcon);
            battleFragment.stateColumnIcon.setImageResource(getResourceByString(name+"icon"));
            battleFragment.stateColumn.setVisibility(VISIBLE);
            battleFragment.isStateColumnShow = false;
            battleFragment.stateColumnHp.setText("生命  "+m.getAbility(HP));
            battleFragment.stateColumnAttack.setText("攻击  "+m.getAbility(ATK));
            battleFragment.stateColumnDefend.setText("防御  " + m.getAbility(DEF));
            battleFragment.stateColumnMagicalDefend.setText("法抗  "+ m.magicalDefend);
            battleFragment.stateColumnBlock.setText("阻挡  "+ m.maxBlock);
            battleFragment.stateColumnRetreat.setVisibility(GONE);
            String tmp = "";
            switch(m.status){
                case -1:
                    tmp = "死亡";
                    battleFragment.stateColumnHurtStateBefore.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.redrect));
                    break;
                case 0:
                    tmp = "濒危";
                    battleFragment.stateColumnHurtStateBefore.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.redrect));
                    break;
                case 1:
                    tmp = "重伤";
                    battleFragment.stateColumnHurtStateBefore.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.orangerect));
                    break;
                case 2:
                    tmp = "受伤";
                    battleFragment.stateColumnHurtStateBefore.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.pinkrect));
                    break;
                case 3:
                    tmp = "轻伤";
                    battleFragment.stateColumnHurtStateBefore.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.yellowrect));
                    break;
                case 4:
                    tmp = "健康";
                    battleFragment.stateColumnHurtStateBefore.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.greenrect));
                    break;
            }
            battleFragment.stateColumnHurtStateBefore.setText(tmp);
            tmp = "";
            switch(m.status2){
                case -1:
                    tmp = "死亡";
                    battleFragment.stateColumnHurtStateAfter.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.redrect));
                    break;
                case 0:
                    tmp = "濒危";
                    battleFragment.stateColumnHurtStateAfter.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.redrect));
                    break;
                case 1:
                    tmp = "重伤";
                    battleFragment.stateColumnHurtStateAfter.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.orangerect));
                    break;
                case 2:
                    tmp = "受伤";
                    battleFragment.stateColumnHurtStateAfter.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.pinkrect));
                    break;
                case 3:
                    tmp = "轻伤";
                    battleFragment.stateColumnHurtStateAfter.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.yellowrect));
                    break;
                case 4:
                    tmp = "健康";
                    battleFragment.stateColumnHurtStateAfter.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.greenrect));
                    break;
            }
            battleFragment.stateColumnHurtStateAfter.setText(tmp);
            if(m.characterSkill != null){
                battleFragment.stateColumnSkill.setVisibility(true);
                battleFragment.stateColumnSkill.setImage(m.characterSkill.skill.skillImage);
                battleFragment.stateColumnSkill.updateEnergy(0,m.characterSkill.skillPoint,SkillIcon.CHARGING,m.characterSkill.duration,m.characterSkill.duration);
                battleFragment.stateColumnSKillName.setText(m.characterSkill.skill.name);
                String IllusTmp = m.characterSkill.getCharacterSkillIllustration();
                battleFragment.stateColumnSkillIllustration.setText(IllusTmp);
                String str = "";
                switch(m.characterSkill.skill.increaseMethod){
                    case Skill.ATTACK:
                        str = "攻击";
                        battleFragment.stateColumnSkillChargeMethod.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.redrect));
                        break;
                    case Skill.AUTO:
                        str = "自然";
                        battleFragment.stateColumnSkillChargeMethod.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.greenrect));
                        break;
                    case Skill.DEFEND:
                        str = "防御";
                        battleFragment.stateColumnSkillChargeMethod.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.yellowrect));
                        break;
                    case Skill.MOVING:
                        str = "移动";
                        battleFragment.stateColumnSkillChargeMethod.setBackground(battleFragment.MAINCONTEXT.getResources().getDrawable(R.drawable.pinkrect));
                        break;
                    default:
                }
                battleFragment.stateColumnSkillChargeMethod.setText(str);
                battleFragment.stateColumnSkillReleaseMethod.setText(m.characterSkill.skill.autoRelease? "自动":"手动");
            }else{
                battleFragment.stateColumnSkill.setVisibility(false);
            }
        }

    }
    public void onCancel(){
        sampleSet.clone(parent);
        sampleSet.setMargin(battleIconStage.getId(),3,14);
        sampleSet.applyTo(parent);
        if(state == ONBATTLE){
            character.onCancel();
        }
        updateState();
    }
    public void changeRevivalTime(float reduce){
        time -= reduce;
        battleIconRevival.setText(String.valueOf(((int)(time*10))/10.0f));
        if(time <= 0){
            if(cost > CostIcon.cost){
                changeState(UNAVAILABLE);
            }else{
                if(iconMessage.status2 >= 0){
                    changeState(AVAILABLE);
                }else{
                    changeState(UNAVAILABLE);
                }
            }
        }
    }
}
