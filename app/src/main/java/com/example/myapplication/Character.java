package com.example.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class Character extends ConstraintLayout {
    public static final boolean PHYSIC = true;
    public static final boolean MAGIC = false;

    public static final int HIGH = 3;
    public static final int MIDDLE = 2;
    public static final int LOW = 1;

    public static final int CHARGING = 1;
    public static final int READY = 2;
    public static final int USING = 3;

    public static ConstraintSet sampleSet= new ConstraintSet();

    String charName = "";
    GifImageView gifImageView;
    Context context;
    TextView hpTextView;
    ImageView characterOnChosenIcon;
    ConstraintLayout characterBackground;
    TextView skillTextView;
    TextView skillBackground;
    ImageView skillReady;

    int maxHp;
    int hp;
    int virtualHp;
    int attack;
    int physicalDefend;
    int magicalDefend;
    int characterHeight;
    boolean weapon;
    String direction = "right";
    boolean canExplosion;
    CharacterMessage message;
    GifDrawable drawable;
    CharacterBattleIcon icon = null;
    CharacterSkill enemySkill = null;
    int skillState;
    BattleFragment battleFragment;
    TextView hpVirtualTextView;

    public Character(Context context, String charName, int maxHp, int attack, boolean relationship, int physicalDefend, int magicalDefend, boolean weapon, boolean canExplosion, BattleFragment battleFragment, OnClickListener onClickListener){
        super(context);
        this.context = context;
        this.charName = charName;
        this.attack = attack;
        this.hp = maxHp;
        this.virtualHp = maxHp;
        this.maxHp = maxHp;
        this.physicalDefend = physicalDefend;
        this.magicalDefend = magicalDefend;
        this.weapon = weapon;
        this.canExplosion = canExplosion;
        this.battleFragment = battleFragment;
        View v = LayoutInflater.from(context).inflate(R.layout.characterlayout,Character.this);
        hpTextView = (TextView) v.findViewById(R.id.hp);
        hpVirtualTextView = (TextView) v.findViewById(R.id.hpVirtual);
        gifImageView = (GifImageView) v.findViewById(R.id.characterMovement);
        characterOnChosenIcon = (ImageView) v.findViewById(R.id.characterChosenIcon);
        characterBackground = (ConstraintLayout) v.findViewById(R.id.characterBackground);
        skillTextView = (TextView) v.findViewById(R.id.skill);
        skillBackground = (TextView) v.findViewById(R.id.skillBackground);
        skillReady = (ImageView) v.findViewById(R.id.characterSkillReady);
        idle(direction);
        updateHp();
        setHpColor(relationship);
        setHeight();
        if(onClickListener != null){
            TextView temp = (TextView) v.findViewById(R.id.clickRange);
            temp.setOnClickListener(onClickListener);
        }
    }
    public void setHp(int hp){
        this.hp = hp;
        LayoutParams params = (ConstraintLayout.LayoutParams)hpTextView.getLayoutParams();
        params.width = (int)(1.0*hp/maxHp*100);
        hpTextView.setLayoutParams(params);
    }
    public void setVirtualHp(int hp){
        this.virtualHp = hp;
        LayoutParams params = (ConstraintLayout.LayoutParams)hpVirtualTextView.getLayoutParams();
        params.width = (int)(1.0*virtualHp/maxHp*100);
        hpVirtualTextView.setLayoutParams(params);
    }
    public void setHpColor(boolean relationship){
        if(relationship == true){
            hpTextView.setBackgroundColor(getResources().getColor(R.color.colorTrueHp));
        }else{
            hpTextView.setBackgroundColor(getResources().getColor(R.color.colorFalseHp));
        }

    }
    public void updateHp(){
        setHp(this.hp);
        if(battleFragment != null){
            if(BattleFragment.chooseIcon != -1 && BattleFragment.iconList.get(BattleFragment.chooseIcon).state == CharacterBattleIcon.ONBATTLE && BattleFragment.iconList.get(BattleFragment.chooseIcon).character == this){
                battleFragment.stateColumnHp.setText("生命  "+this.hp+"/"+this.maxHp);
            }
        }
    }
    public void attack(String direction){
        this.direction = direction;
        playGif(charName+direction+"attackpre");
    }
    public void attack(){
        if(charName.equals("agz")){
            playGif(charName+direction+"attack2pre");
        }else{
            playGif(charName+direction+"attackpre");
        }
    }
    public void setHeight(){
        characterHeight = HIGH;
        switch(charName){
            case "agz": case "fmj": case "fmls": case "gjwzry":
            case "llz": case "shz": case "tf": case "wzry":
                characterHeight = HIGH;
                break;
            case "xgxd": case "sx2": case "sy2": case "xgjjs":
            case "xgjjszz": case "xgss": case "xgsszz": case "xgxdpbz":
            case "xgxdzbr": case "yjddw":
                characterHeight = MIDDLE;
                break;
            case "bbysc": case "bbysc2": case "px": case "sr":
            case "sy": case "ysc": case "ysc2": case "ysc3":
                characterHeight = LOW;
                break;
        }
        sampleSet.clone(characterBackground);
        switch(characterHeight){
            case HIGH:
                sampleSet.setMargin(characterOnChosenIcon.getId(),3,0);
                sampleSet.setMargin(skillReady.getId(),3,0);
                break;
            case MIDDLE:
                sampleSet.setMargin(characterOnChosenIcon.getId(),3,50);
                sampleSet.setMargin(skillReady.getId(),3,50);
                break;
            case LOW:
                sampleSet.setMargin(characterOnChosenIcon.getId(),3,80);
                sampleSet.setMargin(skillReady.getId(),3,80);
                break;
        }
        sampleSet.applyTo(characterBackground);
    }
    public void move(String direction){
        this.direction = direction;
        playGif(charName+direction+"move");
    }
    public void move(){
        playGif(charName+direction+"move");
    }
    public void idle(String direction){
        this.direction = direction;
        playGif(charName+direction+"idle");
    }
    public void idle(){
        playGif(charName+direction+"idle");
    }

    public void die(String direction){
        this.direction = direction;
        playGif(charName+direction+"die");
    }

    public void die(){
        playGif(charName+direction+"die");
    }

    private void playGif(String str){
        try{
            if(Character.this.hp <= 0 && !str.endsWith("die")){
                if(Character.this.icon.characterSkill != null){
                    Character.this.icon.characterSkill.initSkill();
                }
                Character.this.die();
                return;
            }
            drawable = new GifDrawable(context.getResources(),getResourceByString(str));
            if(battleFragment != null &&battleFragment.isPause){
                drawable.stop();
            }
            //if(str.startsWith("sx2")&&str.contains("attack")){
            //    drawable.setSpeed(2.0f*BattleFragment.playSpeed);
            //}else{
            drawable.setSpeed(BattleFragment.playSpeed);
            //}
            final String inputStr = str;
            if(str.endsWith("pre")){
                message.clearBlock();
                if(message.getBlockLength() == 0){
                    switch(message.ai){
                        case BattleFragment.CHARGE:
                            message.state = "moving";
                            Log.d("Sam",message.character.charName + " " + message.state + " " + message.getBlockLength() + " 7");
                            break;
                        case BattleFragment.KEEPSTANDING:
                            message.state = "standing";
                            Log.d("Sam",message.character.charName + " " + message.state + " " + message.getBlockLength() + " 8");
                            break;
                        default:
                    }
                    return;
                }
                drawable.addAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationCompleted(int loopNumber) {
                        if(message.getAttackTarget() != null && (message.relationship || message.getAttackTarget().icon != null)){
                            int damage = calculateDemage(Character.this,message.getAttackTarget());
                            //skill point 计算
                            if(message.relationship){
                                if(message.character.icon.characterSkill != null && message.character.icon.characterSkill.skill.increaseMethod == Skill.ATTACK){
                                    message.character.icon.characterSkill.energy += 33;
                                }
                            }else{
                                if(message.character.enemySkill != null && message.character.enemySkill.skill.increaseMethod == Skill.ATTACK){
                                    message.character.enemySkill.energy += 33;
                                }
                            }
                            if(message.getAttackTarget().message.relationship){
                                if(message.getAttackTarget().icon.characterSkill != null && message.getAttackTarget().icon.characterSkill.skill.increaseMethod == Skill.DEFEND){
                                    message.getAttackTarget().icon.characterSkill.energy += 33;
                                }
                            }else{
                                if(message.getAttackTarget().enemySkill != null && message.getAttackTarget().enemySkill.skill.increaseMethod == Skill.DEFEND){
                                    message.getAttackTarget().enemySkill.energy += 33;
                                }
                            }

                            if(message.getAttackTarget().hp > 0 && message.getAttackTarget().hp-damage <= 0){
                                message.getAttackTarget().hp=0;
                                message.getAttackTarget().updateHp();
                                message.getAttackTarget().die();
                                message.getAttackTarget().message.speedx = 0;
                                message.getAttackTarget().message.state = "dying";
                                Log.d("Sam",message.getAttackTarget().message.character.charName + " " + message.getAttackTarget().message.state + " " + message.getAttackTarget().message.getBlockLength() + " 12");
                                message.block.remove(0);
                            }else{
                                message.getAttackTarget().hp-=damage;
                                message.getAttackTarget().updateHp();
                            }
                            playSound();
                        }
                        playGif(inputStr.substring(0,inputStr.length()-3)+"aft");
                    }
                });
            }else if(str.endsWith("aft")){
                drawable.addAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationCompleted(int loopNumber) {
                        if(message.getAttackTarget() != null && message.getAttackTarget().hp > 0){
                            if(message.attackRange==0){
                                playGif(inputStr.substring(0,inputStr.length()-3)+"pre");
                            }else{
                                if(message.relationship){
                                    if(message.x < message.getAttackTarget().message.x ? (message.getAttackTarget().message.x - message.x < MainActivity.CharAttackWidth + message.attackRange) : (message.x - message.getAttackTarget().message.x  < MainActivity.CharAttackWidth)){
                                        playGif(inputStr.substring(0,inputStr.length()-3)+"pre");
                                    }else{
                                        message.block.remove(0);
                                        message.state="checking";
                                        Log.d("Sam",message.character.charName + " " + message.state + " " + message.getBlockLength() + " 9");
                                    }
                                }else{
                                    if(message.getAttackTarget().icon != null && (message.x < message.getAttackTarget().message.x ? (message.getAttackTarget().message.x - message.x < MainActivity.CharAttackWidth) : (message.x - message.getAttackTarget().message.x  < MainActivity.CharAttackWidth  + message.attackRange))){
                                        playGif(inputStr.substring(0,inputStr.length()-3)+"pre");
                                    }else{
                                        message.block.remove(0);
                                        message.state="checking";
                                        Log.d("Sam",message.character.charName + " " + message.state + " " + message.getBlockLength() + " 9");
                                    }
                                }
                            }
                        }else{
                            message.state="checking";
                            Log.d("Sam",message.character.charName + " " + message.state + " " + message.getBlockLength() + " 10");
                        }
                    }
                });
            }
            if(str.endsWith("die")){
                if(message.relationship && message.character.hp > 0){
                    ((MainActivity)battleFragment.MAINCONTEXT).playSound(MainActivity.voiceDead,1,1);
                    if(icon.iconMessage.status2 >= 0)icon.iconMessage.status2--;
                }
                drawable.addAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationCompleted(int loopNumber) {
                        if(canExplosion){
                            ((MainActivity)battleFragment.MAINCONTEXT).playSound(MainActivity.voiceExplosion,0.5f,0.5f);
                            for(CharacterMessage m : BattleFragment.characterQueue){
                                if(m.relationship != message.relationship){
                                    if(Math.abs(m.layer - message.layer) <= 1 && Math.abs(m.x - message.x) < MainActivity.CharWidth/3*2){
                                        int demage = calculateExplosionDemage(Character.this,m.character);
                                        if(m.relationship){
                                            if(m.character.icon.characterSkill != null && m.character.icon.characterSkill.skill.increaseMethod == Skill.DEFEND){
                                                m.character.icon.characterSkill.energy += 33;
                                            }
                                        }else{
                                            if(m.character.enemySkill != null && m.character.enemySkill.skill.increaseMethod == Skill.DEFEND){
                                                m.character.enemySkill.energy += 33;
                                            }
                                        }
                                        if(m.character.hp > 0 && m.character.hp-demage <= 0){
                                            m.character.hp=0;
                                            m.character.updateHp();
                                            m.character.die();
                                            m.speedx = 0;
                                            m.state = "dying";
                                            Log.d("Sam",m.character.charName + " " + m.state + " " + m.getBlockLength() + " 11");
                                        }else{
                                            m.character.hp-=demage;
                                            m.character.updateHp();
                                        }
                                    }
                                }
                            }

                        }
                        battleFragment.dropCharacter(message);
                    }
                });
            }
            gifImageView.setImageDrawable(drawable);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private int getResourceByString(String str){
        Resources res = context.getResources();
        int resourceId = res.getIdentifier(str,"drawable",context.getPackageName());
        return resourceId;
    }

    private int calculateDemage(Character attacker, Character defender){
        float attackerBuff = 1;
        float defenderBuff = 1;
        int attackerAdd = 0;
        int defenderAdd = 0;
        float magicalDefenderBuff = 1;
        if(attacker.message.relationship){
            if(attacker.icon.characterSkill != null && attacker.icon.characterSkill.skillState == USING){
                switch(attacker.icon.characterSkill.skillId){
                    case 0: case 1:
                        attackerAdd += attacker.icon.characterSkill.affect;
                        Log.d("Sam","SkillDamage!");
                        break;
                    case 2:
                        attackerBuff += 0.01f*attacker.icon.characterSkill.affect;
                        Log.d("Sam","SkillDamage!");
                        break;
                    default:
                }
            }
            if(defender.enemySkill != null && defender.enemySkill.skillState == USING){
                switch(defender.enemySkill.skillId){
                    case 3: case 4:
                        defenderAdd += defender.enemySkill.affect;
                        Log.d("Sam","SkillDefend!");
                        break;
                    case 5:
                        defenderBuff += 0.01f*defender.enemySkill.affect;
                        Log.d("Sam","SkillDefend!");
                        break;
                    default:
                }
            }
        }else{
            if(attacker.enemySkill != null && attacker.enemySkill.skillState == USING){
                switch(attacker.enemySkill.skillId){
                    case 0: case 1:
                        attackerBuff += 0.01f*attacker.enemySkill.affect;
                        Log.d("Sam","SkillDamage!");
                        break;
                    case 2:
                        attackerAdd += attacker.enemySkill.affect;
                        Log.d("Sam","SkillDamage!");
                        break;
                    default:
                }
            }
            if(defender.icon.characterSkill != null && defender.icon.characterSkill.skillState == USING){
                switch(defender.icon.characterSkill.skillId){
                    case 3:  case 4:
                        defenderAdd += defender.icon.characterSkill.affect;
                        Log.d("Sam","SkillDefend!");
                        break;
                    case 5:
                        defenderBuff += 0.01f*defender.icon.characterSkill.affect;
                        Log.d("Sam","SkillDefend!");
                        break;
                    default:
                }
            }
        }
        if(attacker.weapon == PHYSIC){
            return (int) Math.max(((attacker.attack+attackerAdd) * attackerBuff - (defender.physicalDefend+defenderAdd) * defenderBuff), attacker.attack*0.20f);
        }else{
            return (int)((attacker.attack+attackerAdd) * attackerBuff * (1 - 1.0 * defender.magicalDefend * magicalDefenderBuff/100));
        }
    }
    private int calculateExplosionDemage(Character attacker, Character defender){
        float attackerBuff = 1;
        float defenderBuff = 1;
        float magicalDefenderBuff = 1;
        if(attacker.weapon == PHYSIC){
            return (int) Math.max((attacker.attack * attackerBuff * 5 - defender.physicalDefend * defenderBuff), attacker.attack*0.20f);
        }else{
            return (int)(attacker.attack * attackerBuff * 5 * (1 - 1.0 * defender.magicalDefend * magicalDefenderBuff/100))*5;
        }
    }
    public void initSkill(){
        if(message.relationship){
            if(icon != null && icon.characterSkill != null){
                icon.characterSkill.character = this;
                skillTextView.setVisibility(VISIBLE);
                skillBackground.setVisibility(VISIBLE);
            }else{
                skillTextView.setVisibility(INVISIBLE);
                skillBackground.setVisibility(INVISIBLE);
            }
        }else{
            if(enemySkill != null){
                enemySkill.character = this;
                skillTextView.setVisibility(VISIBLE);
                skillBackground.setVisibility(VISIBLE);
            }else{
                skillTextView.setVisibility(INVISIBLE);
                skillBackground.setVisibility(INVISIBLE);
            }
        }

    }
    public void stopPlayingGif(){
        drawable.stop();
    }
    public void startPlayingGif(){
        drawable.start();
    }
    public void changeSpeed(float speed){
        drawable.setSpeed(speed);
    }
    public void updateEnergy(int energy, int maxEnergy, int skillState, int leftOverTime, int duration) {
        switch (skillState) {
            case CHARGING:
                if (this.skillState != skillState) {
                    skillReady.setVisibility(INVISIBLE);
                    setSkillColor(true);
                    this.skillState = skillState;
                }
                setSkillProcess(1.0f * energy / maxEnergy);
                break;
            case READY:
                if (this.skillState != skillState) {
                    skillReady.setVisibility(VISIBLE);
                    setSkillColor(true);
                    this.skillState = skillState;
                }
                break;
            case USING:
                if (this.skillState != skillState) {
                    skillReady.setVisibility(INVISIBLE);
                    setSkillColor(false);
                    this.skillState = skillState;
                }
                setSkillProcess(1.0f * leftOverTime / duration);
                break;
            default:
        }
    }
    private void setSkillProcess(float process){
        LayoutParams params = (ConstraintLayout.LayoutParams)skillTextView.getLayoutParams();
        params.width = (int)(process*100);
        skillTextView.setLayoutParams(params);
    }
    private void setSkillColor(boolean isCharge){
        skillTextView.setBackgroundColor(getResources().getColor(isCharge? R.color.colorSkillCharge:R.color.colorSkillUsing));
    }
    public void onChosen(){
        characterOnChosenIcon.setVisibility(VISIBLE);
    }
    public void onCancel(){
        characterOnChosenIcon.setVisibility(INVISIBLE);
    }
    private void playSound(){
        if(battleFragment != null){
            MainActivity mainActivity = (MainActivity) battleFragment.MAINCONTEXT;
            int voiceId;
            switch(charName){
                case "agz": case "fmj": case "fmls": case "wzry": case "gjwzry": case "llz":
                case "shz": case "tf": case "yjddw": case "skzdjs":
                    voiceId = MainActivity.voiceBigAxe;
                    break;
                case "px": case "sy2": case "xgxd": case "xgxdpbz": case "xgxdzbr": case "skzdb":
                case "skzmjs": case "skzmjszz": case "yjdskzzszz":
                    voiceId = MainActivity.voiceSword;
                    break;
                case "sx2": case "xgss": case "xgsszz":
                    voiceId = MainActivity.voiceMagic;
                    break;
                case "xgjjs": case "xgjjszz": case "skzjjs": case "yjdjjs":
                    voiceId = MainActivity.voiceArrow;
                    break;
                case "sr": case "sy": case "yjdlq":
                    voiceId = MainActivity.voiceBite;
                    break;
                default:
                    voiceId = MainActivity.voiceCommonAttack;
            }
            if(voiceId != -1){
                mainActivity.playSound(voiceId,0.8f,0.8f);
            }
        }
    }
}
