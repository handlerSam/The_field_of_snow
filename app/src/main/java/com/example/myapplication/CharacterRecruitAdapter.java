package com.example.myapplication;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.example.myapplication.MainActivity.CASTER;
import static com.example.myapplication.MainActivity.DEFENDER;
import static com.example.myapplication.MainActivity.GUARD;
import static com.example.myapplication.MainActivity.MAGIC;
import static com.example.myapplication.MainActivity.SNIPER;
import static com.example.myapplication.MainActivity.VANGUARD;

public class CharacterRecruitAdapter extends RecyclerView.Adapter<CharacterRecruitAdapter.ViewHolder> {
    private List<CharacterIconMessage> mCharacter;
    private MainActivity m;
    private BarFragment barFragment;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView characterRecruitIcon;
        ImageView characterRecruitElite;
        TextView characterRecruitLv;
        ImageView characterRecruitWeapon;
        TextView characterRecruitHp;
        TextView characterRecruitAtk;
        TextView characterRecruitDef;
        TextView characterRecruitHpUpgrade;
        TextView characterRecruitAtkUpgrade;
        TextView characterRecruitDefUpgrade;
        TextView characterRecruitMagicDefend;
        TextView characterRecruitCost;
        TextView characterRecruitRevival;
        TextView characterRecruitBlock;
        TextView characterRecruitSpeed;
        TextView characterRecruitSkill;
        TextView barRecruitShade;
        ConstraintLayout barRecruitShade3;
        ImageView characterRecruitPriceIcon;
        TextView characterRecruitPrice;
        public ViewHolder(View view){
            super(view);
            characterRecruitIcon = view.findViewById(R.id.characterRecruitIcon);
            characterRecruitElite = view.findViewById(R.id.characterRecruitElite);
            characterRecruitLv = view.findViewById(R.id.characterRecruitLv);
            characterRecruitWeapon = view.findViewById(R.id.characterRecruitWeapon);
            characterRecruitHp = view.findViewById(R.id.characterRecruitHp);
            characterRecruitAtk = view.findViewById(R.id.characterRecruitAtk);
            characterRecruitDef = view.findViewById(R.id.characterRecruitDef);
            characterRecruitHpUpgrade = view.findViewById(R.id.characterRecruitHpUpgrade);
            characterRecruitAtkUpgrade = view.findViewById(R.id.characterRecruitAtkUpgrade);
            characterRecruitDefUpgrade = view.findViewById(R.id.characterRecruitDefUpgrade);
            characterRecruitMagicDefend = view.findViewById(R.id.characterRecruitMagicDefend);
            characterRecruitCost = view.findViewById(R.id.characterRecruitCost);
            characterRecruitRevival = view.findViewById(R.id.characterRecruitRevival);
            characterRecruitBlock = view.findViewById(R.id.characterRecruitBlock);
            characterRecruitSpeed = view.findViewById(R.id.characterRecruitSpeed);
            characterRecruitSkill = view.findViewById(R.id.characterRecruitSkill);
            barRecruitShade = view.findViewById(R.id.barRecruitShade);
            barRecruitShade3 = view.findViewById(R.id.barRecruitShade3);
            characterRecruitPriceIcon = view.findViewById(R.id.characterRecruitPriceIcon);
            characterRecruitPrice = view.findViewById(R.id.characterRecruitPrice);
        }

    }

    public CharacterRecruitAdapter(List<CharacterIconMessage> messageList, MainActivity m, BarFragment barFragment){
        mCharacter = messageList;
        this.m = m;
        this.barFragment = barFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.character_recruit, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final CharacterIconMessage m = mCharacter.get(position);
        holder.characterRecruitIcon.setImageResource(getResource(m.name+"icon"));
        holder.characterRecruitElite.setImageResource(getResource("elite"+m.elite));
        holder.characterRecruitLv.setText("Lv" + m.level);
        holder.characterRecruitWeapon.setImageResource(getResource(m.weapon == MAGIC?"magicbattleicon":"physicbattleicon"));
        holder.characterRecruitHp.setText(""+judgeOriginalAbility(m.name,m.maxHp,"hp"));
        holder.characterRecruitAtk.setText(""+judgeOriginalAbility(m.name,m.attack,"atk"));
        holder.characterRecruitDef.setText(""+judgeOriginalAbility(m.name,m.physicalDefend,"def"));
        holder.characterRecruitHpUpgrade.setText(""+judgeLevel("hp",m.hpLevelUpOccupation,m.levelUpHp));
        holder.characterRecruitAtkUpgrade.setText(""+judgeLevel("atk",m.atkLevelUpOccupation,m.levelUpAtk));
        holder.characterRecruitDefUpgrade.setText(""+judgeLevel("def",m.defLevelUpOccupation,m.levelUpDef));
        holder.characterRecruitMagicDefend.setText(""+m.magicalDefend);
        holder.characterRecruitCost.setText(""+m.cost);
        holder.characterRecruitRevival.setText(""+m.revivalTime+"s");
        holder.characterRecruitBlock.setText(""+m.maxBlock);
        holder.characterRecruitSpeed.setText(""+m.moveSpeed);
        holder.characterRecruitSkill.setText(m.characterSkill == null? "无":"有");
        holder.barRecruitShade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barFragment.setCharacterConfirm(m, CharacterRecruitAdapter.this, holder);
            }
        });
        holder.characterRecruitPriceIcon.setImageResource(m.quality > 1? R.drawable.yellow_ticket:R.drawable.green_ticket);
        holder.characterRecruitPrice.setText(""+(m.quality == 1? 3:1));
    }

    @Override
    public int getItemCount() {
        return mCharacter.size();
    }

    public int getResource(String idName){
        int resId = m.getResources().getIdentifier(idName, "drawable", m.getPackageName());
        return resId;
    }
    private String judgeLevel(String ability, int occupation, float number){
        switch(occupation){
            case VANGUARD:
                switch(ability){
                    case "hp":
                        return judgeGrade(6.5f, 7.5f, number);
                    case "atk":
                        return judgeGrade(1.7f,2.7f, number);
                    case "def":
                        return judgeGrade(1.0f,1.8f, number);
                    default:
                }
                break;
            case GUARD:
                switch(ability){
                    case "hp":
                        return judgeGrade(8.6f,10.8f, number);
                    case "atk":
                        return judgeGrade(2.7f,3.8f, number);
                    case "def":
                        return judgeGrade(0.8f,1.0f, number);
                    default:
                }
                break;
            case DEFENDER:
                switch(ability){
                    case "hp":
                        return judgeGrade(9.7f,11.9f, number);
                    case "atk":
                        return judgeGrade(1.2f,1.8f, number);
                    case "def":
                        return judgeGrade(2f,2.3f, number);
                    default:
                }
                break;
            case SNIPER:
                switch(ability){
                    case "hp":
                        return judgeGrade(5.4f,6.5f, number);
                    case "atk":
                        return judgeGrade(2.6f,3.1f, number);
                    case "def":
                        return judgeGrade(0.6f,0.9f, number);
                    default:
                }
                break;
            case CASTER:
                switch(ability){
                    case "hp":
                        return judgeGrade(5.4f,6.5f, number);
                    case "atk":
                        return judgeGrade(1.9f,2.7f, number);
                    case "def":
                        return judgeGrade(0.6f,0.9f, number);
                    default:
                }
                break;
            default:
        }
        return "";
    }

    public String judgeOriginalAbility(String name, float number, String ability){
        String hpLevel = "";
        String atkLevel = "";
        String defLevel = "";
        float hpModify = 1.0f;
        float atkModify = 1.1f;
        float defModify = 0.8f;
        switch(name){
            case "sy":
                hpLevel = judgeGrade(693,758,number);
                atkLevel = judgeGrade(170*atkModify,185*atkModify,number);
                defLevel = judgeGrade(136*defModify,137*defModify,number);
                break;
            case "sr":
                hpLevel = judgeGrade(727,812,number);
                atkLevel = judgeGrade(183*atkModify,203*atkModify,number);
                defLevel = judgeGrade(139*defModify,147*defModify,number);
                break;
            case "yjdlq":
                hpLevel = judgeGrade(911,975,number);
                atkLevel = judgeGrade(212*atkModify,230*atkModify,number);
                defLevel = judgeGrade(154*defModify,163*defModify,number);
                break;
            case "bbysc":
                hpLevel = judgeGrade(533,618,number);
                atkLevel = judgeGrade(170*atkModify,185*atkModify,number);
                defLevel = judgeGrade(76*defModify,87*defModify,number);
                break;
            case "xgxd":
                hpLevel = judgeGrade(949,990,number);
                atkLevel = judgeGrade(233*atkModify,272*atkModify,number);
                defLevel = judgeGrade(142*defModify,154*defModify,number);
                break;
            case "bbysc2":
                hpLevel = judgeGrade(533,618,number);
                atkLevel = judgeGrade(183*atkModify,203*atkModify,number);
                defLevel = judgeGrade(89*defModify,117*defModify,number);
                break;
            case "sy2":
                hpLevel = judgeGrade(949,949,number);
                atkLevel = judgeGrade(272*atkModify,272*atkModify,number);
                defLevel = judgeGrade(154*defModify,154*defModify,number);
                break;
            case "xgjjs":
                hpLevel = judgeGrade(550,604,number);
                atkLevel = judgeGrade(163*atkModify,166*atkModify,number);
                defLevel = judgeGrade(54*defModify,57*defModify,number);
                break;
            case "xgss":
                hpLevel = judgeGrade(589,619,number);
                atkLevel = judgeGrade(145*atkModify,155*atkModify,number);
                defLevel = judgeGrade(42*defModify,47*defModify,number);
                break;
            case "xgxdzbr":
                hpLevel = judgeGrade(1038,1138,number);
                atkLevel = judgeGrade(754*atkModify,778*atkModify,number);
                defLevel = judgeGrade(241*defModify,249*defModify,number);
                break;
            case "xgjjszz":
                hpLevel = judgeGrade(667,693,number);
                atkLevel = judgeGrade(171*atkModify,177*atkModify,number);
                defLevel = judgeGrade(53*defModify,58*defModify,number);
                break;
            case "yjddw":
                hpLevel = judgeGrade(1539,1602,number);
                atkLevel = judgeGrade(192*atkModify,198*atkModify,number);
                defLevel = judgeGrade(254*defModify,257*defModify,number);
                break;
            case "xgsszz":
                hpLevel = judgeGrade(619,699,number);
                atkLevel = judgeGrade(171*atkModify,189*atkModify,number);
                defLevel = judgeGrade(47*defModify,48*defModify,number);
                break;
            case "xgxdpbz":
                hpLevel = judgeGrade(1238,1338,number);
                atkLevel = judgeGrade(814*atkModify,838*atkModify,number);
                defLevel = judgeGrade(257*defModify,267*defModify,number);
                break;
            case "yjdjjs":
                hpLevel = judgeGrade(711,787,number);
                atkLevel = judgeGrade(253*atkModify,282*atkModify,number);
                defLevel = judgeGrade(57*defModify,62*defModify,number);
                break;
            case "yjdskzzzzz":
                hpLevel = judgeGrade(1236,1253,number);
                atkLevel = judgeGrade(322*atkModify,335*atkModify,number);
                defLevel = judgeGrade(108*defModify,121*defModify,number);
                break;
            case "sx2":
                hpLevel = judgeGrade(732,732,number);
                atkLevel = judgeGrade(362*atkModify,362*atkModify,number);
                defLevel = judgeGrade(48*defModify,48*defModify,number);
                break;
            case "agz":
                hpLevel = judgeGrade(1602,1602,number);
                atkLevel = judgeGrade(415*atkModify,415*atkModify,number);
                defLevel = judgeGrade(279*defModify,279*defModify,number);
                break;
            default:
        }
        switch(ability){
            case "hp":
                return hpLevel;
            case "atk":
                return atkLevel;
            case "def":
                return defLevel;
            default:
        }
        return "";
    }

    public String judgeGrade(float min,float max,float number){
        float temp = (max - min)/4;
        if(number < min+temp){
            return "C";
        }else if(number < min + 2*temp){
            return "B";
        }else if(number < min + 3*temp){
            return "A";
        }else{
            return "S";
        }
    }

    public void setShade(ViewHolder holder, boolean isShow){
        holder.barRecruitShade3.setVisibility(isShow? View.VISIBLE:View.INVISIBLE);
        if(isShow){
            holder.barRecruitShade3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
