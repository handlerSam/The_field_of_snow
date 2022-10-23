package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.PRINT_SERVICE;
import static android.view.View.GONE;
import static com.example.myapplication.BattleFragment.PHYSIC;
import static com.example.myapplication.BattleFragment.chooseIcon;


public class CharacterFragment extends Fragment {
    public static final int BROWSER = 1;
    public static final int CHOOSE = 2;
    public static final int DEF = 1;
    public static final int ATK = 2;
    public static final int HP = 3;
    static int mode;
    int maxTeamMember;
    boolean isChooseAll = false;
    boolean isChooseTeamShow = false;
    boolean isEquipmentColumnShow = false;
    int level;
    static RecyclerView characterRecyclerView;
    int[] expCardNumber = new int[4];//每次打开升级界面时更新，内容为背包中的经验卡数量
    int[] bagExpCardNumber = new int[4];//不随expNumber变化而变化，一直为背包中的经验卡数量
    int[] expNumber = new int[4];//目前计划要消耗的经验卡数量
    int levelAfterUpgrade;
    ImageView characterIconImage;
    TextView characterIconText;
    TextView characterIconName;
    ImageView characterIconElite;
    ImageView characterIconEliteUp;
    TextView characterIconLevel;
    TextView characterIconMaxLevel;
    ImageView characterIconLevelUp;
    TextView characterIconHp;
    TextView characterIconDefend;
    TextView characterIconAttack;
    TextView characterIconMagicalDefend;
    TextView characterIconRetreat;
    TextView characterIconCost;
    TextView characterIconBlock;
    TextView characterIconSpeed;
    TextView characterIconSkillName;
    TextView characterIconSkillIncreasingMethod;
    TextView characterIconSkillAutoRelease;
    TextView characterIconSkillDuration;
    TextView characterIconSkillIllustration;
    ImageView characterIconSkillImage;
    TextView characterIconState;
    TextView characterIconSkillPoint;
    ImageView characterIconLighting;
    LinearLayout characterIconChooseColumn;
    Context MAINCONTEXT;
    TextView characterIconButton;
    TextView characterIconShade;
    TextView characterIconChooseNumber;
    CharacterAdapter characterAdapter;
    TextView characterIconContinue;
    ImageView characterIconWeapon;
    TextView characterIconArrange;
    TextView characterIconRetreatable;
    ConstraintLayout characterIconEquipmentChoose;
    EquipmentSquare characterIconEquipmentSquare;//主页面上的
    EquipmentSquare equipmentSquare;//装备选择界面的
    RecyclerView weaponRecyclerView;
    RecyclerView armorRecyclerView;
    LinearLayout equipmentSquareLayout;

    TextView equipmentSquareWeaponChangePosition;
    TextView equipmentSquareArmorChangePosition;
    TextView equipmentSquareBack;

    ImageView characterIconWeaponIcon;
    TextView characterIconWeaponQuality;
    TextView characterIconWeaponName;
    TextView characterIconWeaponIllustration;
    ImageView characterIconArmorIcon;
    TextView characterIconArmorQuality;
    TextView characterIconArmorName;
    TextView characterIconArmorIllustration;
    LinearLayout characterIconArmorLayout;
    LinearLayout characterIconWeaponLayout;
    LinearLayout characterIconMin;
    LinearLayout characterIconReduce;
    TextView characterIconLevelText;
    LinearLayout characterIconMax;
    LinearLayout characterIconAdd;
    LinearLayout characterIconLevelUpLayout;

    LinearLayout characterIconPromotionLayout;
    ImageView item1ImageView;
    TextView item1Number;
    ImageView item2ImageView;
    TextView item2Number;
    TextView characterIconPromotionCancel;
    TextView characterIconPromotionConfirm;
    TextView characterIconPromotionBeforeLevel;
    TextView characterIconPromotionAfterLevel;
    ImageView eliteBeforeImageView;
    ImageView eliteAfterImageView;
    LinearLayout backButton;
    TextView characterIconShade3;
    TextView exp1Number;
    TextView exp2Number;
    TextView exp3Number;
    TextView exp4Number;
    ArrayList<TextView> expNumberList = new ArrayList<>();
    TextView characterIconLevelCancel;
    TextView characterIconLevelConfirm;
    TextView characterLevelUpAbilityChangeTextView;
    SharedPreferences pref;
    ArrayList<TextView> armorIconQualityList= new ArrayList<>();
    ArrayList<TextView> weaponIconQualityList= new ArrayList<>();
    ArrayList<CharacterIconMessage> addIconList;
    public TextView characterIconChooseAll;
    ImageView shade2;
    ImageView shade3;

    TextView characterIconDismiss;

    TextView characterIconShade6;
    LinearLayout characterIconDismissConfirmLayout;
    TextView characterIconDismissCancel;
    TextView characterIconDismissConfirm;
    ImageView characterDismissIcon;
    ImageView characterDismissElite;
    TextView characterDismissLv;
    ImageView characterDismissWeapon;
    ImageView characterIconDismissIcon;
    public CharacterFragment(Operation operation, int chooseTeam, int mode, MainActivity m) {
        super();
        this.mode = mode;
        this.MAINCONTEXT = m;
        if(mode == CHOOSE){
            this.maxTeamMember = operation.maxNumber;
            this.level = operation.level;
        }
        this.addIconList = MainActivity.teamList.get(chooseTeam).characterList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.character_fragment,container,false);
        initFind(v);
        initRecyclerView();
        initSet();
        initSound();
        return v;
    }

    private void initSound(){
        if(((MainActivity)MAINCONTEXT).backgroundSound != null){
            ((MainActivity)MAINCONTEXT).backgroundSound.destroy();
            ((MainActivity)MAINCONTEXT).backgroundSound = null;
        }
        ((MainActivity)MAINCONTEXT).backgroundSound = new BackgroundSound(R.raw.game02intro,R.raw.game02loop,((MainActivity)MAINCONTEXT));
        Log.d("Sam","game02Start");
    }

    private void initFind(View v){
        characterRecyclerView = (RecyclerView) v.findViewById(R.id.characterRecyclerView);
        characterIconImage = (ImageView) v.findViewById(R.id.characterIconImage);
        characterIconText = (TextView) v.findViewById(R.id.characterIconText);
        characterIconName = (TextView) v.findViewById(R.id.characterIconName);
        characterIconElite = (ImageView) v.findViewById(R.id.characterIconElite);
        characterIconEliteUp = (ImageView) v.findViewById(R.id.characterIconEliteUp);
        characterIconLevel = (TextView) v.findViewById(R.id.characterIconLevel);
        characterIconMaxLevel = (TextView) v.findViewById(R.id.characterIconMaxLevel);
        characterIconLevelUp = (ImageView) v.findViewById(R.id.characterIconLevelUp);
        characterIconHp = (TextView) v.findViewById(R.id.characterIconHp);
        characterIconDefend = (TextView) v.findViewById(R.id.characterIconDefend);
        characterIconAttack = (TextView) v.findViewById(R.id.characterIconAttack);
        characterIconMagicalDefend = (TextView) v.findViewById(R.id.characterIconMagicalDefend);
        characterIconRetreat = (TextView) v.findViewById(R.id.characterIconRetreat);
        characterIconCost = (TextView) v.findViewById(R.id.characterIconCost);
        characterIconBlock = (TextView) v.findViewById(R.id.characterIconBlock);
        characterIconSpeed = (TextView) v.findViewById(R.id.characterIconSpeed);
        characterIconSkillName = (TextView) v.findViewById(R.id.characterIconSkillName);
        characterIconSkillIncreasingMethod = (TextView) v.findViewById(R.id.characterIconSkillIncreasingMethod);
        characterIconSkillAutoRelease = (TextView) v.findViewById(R.id.characterIconSkillAutoRelease);
        characterIconSkillDuration = (TextView) v.findViewById(R.id.characterIconSkillDuration);
        characterIconSkillIllustration = (TextView) v.findViewById(R.id.characterIconSkillIllustration);
        characterIconState = (TextView) v.findViewById(R.id.characterIconState);
        characterIconSkillImage = (ImageView) v.findViewById(R.id.characterIconSkillImage);
        characterIconSkillPoint = (TextView) v.findViewById(R.id.characterIconSkillPoint);
        characterIconLighting = (ImageView) v.findViewById(R.id.characterIconLighting);
        characterIconChooseColumn = (LinearLayout) v.findViewById(R.id.characterIconChooseColumn);
        characterIconButton = (TextView) v.findViewById(R.id.characterIconButton);
        characterIconShade = (TextView) v.findViewById(R.id.characterIconShade);
        characterIconChooseNumber = (TextView) v.findViewById(R.id.characterIconChooseNumber);
        characterIconContinue = (TextView) v.findViewById(R.id.characterIconContinue);
        characterIconWeapon = (ImageView) v.findViewById(R.id.characterIconWeapon);
        characterIconArrange = (TextView) v.findViewById(R.id.characterIconArrange);
        characterIconRetreatable = (TextView) v.findViewById(R.id.characterIconRetreatable);
        characterIconEquipmentSquare = (EquipmentSquare) v.findViewById(R.id.characterIconWeaponSquare);
        equipmentSquare = (EquipmentSquare) v.findViewById(R.id.equipmentSquare);
        weaponRecyclerView = (RecyclerView) v.findViewById(R.id.characterIconWeaponList);
        armorRecyclerView = (RecyclerView) v.findViewById(R.id.characterIconArmorList);
        equipmentSquareWeaponChangePosition = (TextView) v.findViewById(R.id.equipmentSquareWeaponChangePosition);
        equipmentSquareArmorChangePosition = (TextView) v.findViewById(R.id.equipmentSquareArmorChangePosition);
        equipmentSquareBack = (TextView) v.findViewById(R.id.equipmentSquareBack);
        characterIconEquipmentChoose = (ConstraintLayout) v.findViewById(R.id.characterIconEquipmentChoose);
        equipmentSquareLayout = (LinearLayout) v.findViewById(R.id.equipmentSquareLayout);
        characterIconWeaponIcon = (ImageView) v.findViewById(R.id.characterIconWeaponIcon);
        characterIconWeaponQuality = (TextView) v.findViewById(R.id.characterIconWeaponQuality);
        characterIconWeaponName = (TextView) v.findViewById(R.id.characterIconWeaponName);
        characterIconWeaponIllustration = (TextView) v.findViewById(R.id.characterIconWeaponIllustration);
        characterIconArmorIcon = (ImageView) v.findViewById(R.id.characterIconArmorIcon);
        characterIconArmorQuality = (TextView) v.findViewById(R.id.characterIconArmorQuality);
        characterIconArmorName = (TextView) v.findViewById(R.id.characterIconArmorName);
        characterIconArmorIllustration = (TextView) v.findViewById(R.id.characterIconArmorIllustration);
        characterIconArmorLayout = (LinearLayout) v.findViewById(R.id.characterIconArmorLayout);
        characterIconWeaponLayout = (LinearLayout) v.findViewById(R.id.characterIconWeaponLayout);
        TextView armorIconQuality = (TextView) v.findViewById(R.id.armorIconQuality0);
        armorIconQualityList.add(armorIconQuality);
        armorIconQuality = (TextView) v.findViewById(R.id.armorIconQuality1);
        armorIconQualityList.add(armorIconQuality);
        armorIconQuality = (TextView) v.findViewById(R.id.armorIconQuality2);
        armorIconQualityList.add(armorIconQuality);
        armorIconQuality = (TextView) v.findViewById(R.id.armorIconQuality3);
        armorIconQualityList.add(armorIconQuality);
        armorIconQuality = (TextView) v.findViewById(R.id.armorIconQuality4);
        armorIconQualityList.add(armorIconQuality);
        armorIconQuality = (TextView) v.findViewById(R.id.armorIconQuality5);
        armorIconQualityList.add(armorIconQuality);
        TextView weaponIconQuality = (TextView) v.findViewById(R.id.weaponIconQuality0);
        weaponIconQualityList.add(weaponIconQuality);
        weaponIconQuality = (TextView) v.findViewById(R.id.weaponIconQuality1);
        weaponIconQualityList.add(weaponIconQuality);
        weaponIconQuality = (TextView) v.findViewById(R.id.weaponIconQuality2);
        weaponIconQualityList.add(weaponIconQuality);
        weaponIconQuality = (TextView) v.findViewById(R.id.weaponIconQuality3);
        weaponIconQualityList.add(weaponIconQuality);
        weaponIconQuality = (TextView) v.findViewById(R.id.weaponIconQuality4);
        weaponIconQualityList.add(weaponIconQuality);
        weaponIconQuality = (TextView) v.findViewById(R.id.weaponIconQuality5);
        weaponIconQualityList.add(weaponIconQuality);

        characterIconMin = (LinearLayout) v.findViewById(R.id.characterIconMin);
        characterIconReduce = (LinearLayout) v.findViewById(R.id.characterIconReduce);
        characterIconLevelText = (TextView) v.findViewById(R.id.characterIconLevelText);
        characterIconMax = (LinearLayout) v.findViewById(R.id.characterIconMax);
        characterIconAdd = (LinearLayout) v.findViewById(R.id.characterIconAdd);
        characterIconLevelUpLayout = (LinearLayout) v.findViewById(R.id.characterIconLevelUpLayout);
        characterIconShade3 = (TextView) v.findViewById(R.id.characterIconShade3);
        expNumberList.clear();
        exp1Number = (TextView) v.findViewById(R.id.exp1Number);
        expNumberList.add(exp1Number);
        exp2Number = (TextView) v.findViewById(R.id.exp2Number);
        expNumberList.add(exp2Number);
        exp3Number = (TextView) v.findViewById(R.id.exp3Number);
        expNumberList.add(exp3Number);
        exp4Number = (TextView) v.findViewById(R.id.exp4Number);
        expNumberList.add(exp4Number);
        characterIconLevelCancel = (TextView) v.findViewById(R.id.characterIconLevelCancel);
        characterIconLevelConfirm = (TextView) v.findViewById(R.id.characterIconLevelConfirm);
        characterLevelUpAbilityChangeTextView = (TextView) v.findViewById(R.id.characterLevelUpAbilityChangeTextView);
        characterIconPromotionBeforeLevel = (TextView) v.findViewById(R.id.characterIconPromotionBeforeLevel);
        characterIconPromotionAfterLevel = (TextView) v.findViewById(R.id.characterIconPromotionAfterLevel);
        eliteBeforeImageView = (ImageView) v.findViewById(R.id.eliteBeforeImageView);
        eliteAfterImageView = (ImageView) v.findViewById(R.id.eliteAfterImageView);
        characterIconPromotionLayout = (LinearLayout) v.findViewById(R.id.characterIconPromotionLayout);
        item1ImageView = (ImageView) v.findViewById(R.id.item1ImageView);
        item1Number = (TextView) v.findViewById(R.id.item1Number);
        item2ImageView = (ImageView) v.findViewById(R.id.item2ImageView);
        item2Number = (TextView) v.findViewById(R.id.item2Number);
        characterIconPromotionCancel = (TextView) v.findViewById(R.id.characterIconPromotionCancel);
        characterIconPromotionConfirm = (TextView) v.findViewById(R.id.characterIconPromotionConfirm);
        backButton = (LinearLayout) v.findViewById(R.id.backButton);
        characterIconChooseAll = (TextView)v.findViewById(R.id.characterIconChooseAll);
        shade2 = (ImageView)v.findViewById(R.id.shade2);
        shade3 = (ImageView)v.findViewById(R.id.shade3);
        characterIconDismiss = (TextView) v.findViewById(R.id.characterIconDismiss);
        characterIconShade6 = v.findViewById(R.id.characterIconShade6);
        characterIconDismissConfirmLayout = v.findViewById(R.id.characterIconDismissConfirmLayout);
        characterIconDismissCancel = v.findViewById(R.id.characterIconDismissCancel);
        characterIconDismissConfirm = v.findViewById(R.id.characterIconDismissConfirm);
        characterDismissIcon = v.findViewById(R.id.characterDismissIcon);
        characterDismissElite = v.findViewById(R.id.characterDismissElite);
        characterDismissLv = v.findViewById(R.id.characterDismissLv);
        characterDismissWeapon = v.findViewById(R.id.characterDismissWeapon);
        characterIconDismissIcon = v.findViewById(R.id.characterIconDismissIcon);
    }

    private void initSet(){
        pref = MAINCONTEXT.getSharedPreferences("Item", MODE_PRIVATE);
        switch(mode){
            case BROWSER:
                characterIconShade3.setVisibility(View.INVISIBLE);
                characterIconLevelUpLayout.setVisibility(View.INVISIBLE);
                characterIconChooseColumn.setVisibility(GONE);
                characterIconButton.setVisibility(GONE);
                characterIconShade.setVisibility(GONE);
                characterIconChooseNumber.setVisibility(GONE);
                characterIconContinue.setVisibility(GONE);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Message m = new Message();
                        m.what = MainActivity.MAINMANU;
                        ((MainActivity)MAINCONTEXT).handler.sendMessage(m);
                    }
                });
                characterIconChooseAll.setVisibility(View.INVISIBLE);
                shade2.setVisibility(View.INVISIBLE);
                shade3.setVisibility(View.INVISIBLE);
                break;
            case CHOOSE:
                characterIconShade3.setVisibility(View.INVISIBLE);
                characterIconLevelUpLayout.setVisibility(View.INVISIBLE);
                characterIconChooseColumn.setVisibility(View.INVISIBLE);
                characterIconButton.setVisibility(View.VISIBLE);
                characterIconShade.setVisibility(View.INVISIBLE);
                characterIconLevelUp.setVisibility(View.GONE);
                characterIconChooseNumber.setVisibility(View.VISIBLE);
                characterIconButton.setSoundEffectsEnabled(false);
                characterIconButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)MAINCONTEXT).playSound(MainActivity.voiceDetailCheck,1.0f,1.0f);
                        if(!isChooseTeamShow && !isEquipmentColumnShow){
                            Collections.sort(MainActivity.mChooseList);
                            characterIconChooseColumn.setVisibility(View.VISIBLE);
                            characterIconShade.setVisibility(View.VISIBLE);
                            characterIconChooseColumn.removeAllViews();
                            for(int i = 0; i < MainActivity.mChooseList.size(); i++){
                                CharacterIconMessage m = MainActivity.mChooseList.get(i);
                                CharacterBattleIcon icon = new CharacterBattleIcon(MAINCONTEXT,m.name,m.level,m.elite,m.cost,m.weapon,m.revivalTime,m.retreatable, m.characterSkill,null,m);
                                int id = View.generateViewId();
                                icon.id = i;
                                icon.setId(id);
                                characterIconChooseColumn.addView(icon);
                            }
                            isChooseTeamShow = true;
                        }else{
                            characterIconChooseColumn.setVisibility(View.INVISIBLE);
                            characterIconShade.setVisibility(View.INVISIBLE);
                            isChooseTeamShow = false;
                        }
                    }
                });
                characterIconContinue.setText("开始行动");
                characterIconContinue.setSoundEffectsEnabled(false);
                characterIconContinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isChooseTeamShow && !isEquipmentColumnShow){
                            if(MainActivity.mChooseList.size()>1){
                                ((MainActivity)MAINCONTEXT).playSound(MainActivity.voiceConfirm,1.0f,1.0f);
                                ((MainActivity)MAINCONTEXT).backgroundSound.endMusic(1500);
                                MainActivity m = (MainActivity) MAINCONTEXT;
                                m.setFragment(MainActivity.BATTLESTARTFRAGMENT);
                                Collections.sort(MainActivity.mChooseList);
                            }else{
                                ((MainActivity)MAINCONTEXT).playSound(MainActivity.voiceDanger,1.0f,1.0f);
                                Toast.makeText(MAINCONTEXT,"请至少携带两名干员！",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Message m = new Message();
                        m.what = MainActivity.MAINMANU;
                        ((MainActivity)MAINCONTEXT).handler.sendMessage(m);
                    }
                });
                setCharacterNumber(MainActivity.mChooseList.size());
                characterIconChooseAll.setVisibility(View.VISIBLE);
                characterIconChooseAll.setText("选择全部");
                isChooseAll = false;
                characterIconChooseAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isChooseAll){
                            characterIconChooseAll.setText("选择全部");
                            for(CharacterAdapter.ViewHolder holder: characterAdapter.mViewHolderList){
                                characterAdapter.mCharacterIconList.get(holder.position).inChooseList = false;
                                MainActivity.mChooseList.remove(characterAdapter.mCharacterIconList.get(holder.position));
                                holder.iconState.setVisibility(View.INVISIBLE);
                                isChooseAll = false;
                            }
                        }else{
                            characterIconChooseAll.setText("取消选择");
                            for(CharacterAdapter.ViewHolder holder: characterAdapter.mViewHolderList){
                                characterAdapter.mCharacterIconList.get(holder.position).inChooseList = true;
                                MainActivity.mChooseList.remove(characterAdapter.mCharacterIconList.get(holder.position));
                                MainActivity.mChooseList.add(characterAdapter.mCharacterIconList.get(holder.position));
                                holder.iconState.setVisibility(View.VISIBLE);
                                isChooseAll = true;
                            }
                        }
                        setCharacterNumber(MainActivity.mChooseList.size());
                    }
                });
                shade2.setVisibility(View.VISIBLE);
                shade3.setVisibility(View.VISIBLE);
                break;
            default:
        }
    }

    private void initRecyclerView(){
        StaggeredGridLayoutManager m = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        characterRecyclerView.setLayoutManager(m);
        characterAdapter = new CharacterAdapter(addIconList,MAINCONTEXT,this);
        characterRecyclerView.setAdapter(characterAdapter);
        characterRecyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
                return true;
            }
        });
        /*characterIconSkillName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.mCharacterIconList.remove(0);
                a.notifyItemRemoved(0);
                a.notifyItemRangeChanged(0,a.getItemCount());
            }
        });*/
    }

    public void onChosen(final int choose){
        if(choose != -1){
            final CharacterIconMessage m = addIconList.get(choose);
            initLevelUpLayout(m, choose);
            if(m.elite < 3 && (m.level == m.maxLevel)){
                initPromotionUpLayout(m, choose);
            }
            characterIconImage.setImageResource(getResourceByString(m.name + "icon"));
            characterIconText.setText(m.codeName);
            characterIconName.setText(m.realName);
            characterIconElite.setVisibility(View.VISIBLE);
            characterIconElite.setImageResource(getResourceByString("elite"+m.elite));
            characterIconEliteUp.setVisibility((m.level == m.maxLevel) && (m.elite < 3)? View.VISIBLE:View.INVISIBLE);
            characterIconLevel.setText(String.valueOf(m.level));
            characterIconLevelUp.setVisibility(View.VISIBLE);
            characterIconMaxLevel.setText("/"+m.maxLevel);
            characterIconHp.setText(String.valueOf(m.getAbility(HP)));
            characterIconDefend.setText(String.valueOf(m.getAbility(DEF)));
            characterIconAttack.setText(String.valueOf(m.getAbility(ATK)));
            characterIconMagicalDefend.setText(String.valueOf(m.magicalDefend));
            characterIconRetreat.setText(((int)m.revivalTime)+"s");
            characterIconCost.setText(String.valueOf(m.cost));
            characterIconBlock.setText(String.valueOf(m.attackRange == 0? m.maxBlock:m.maxBlock-1));
            characterIconSpeed.setText(String.valueOf(m.moveSpeed));
            characterIconArrange.setText(m.attackRange > 0 ? String.valueOf(m.attackRange) : "近战");
            characterIconRetreatable.setText(String.valueOf(m.retreatable));
            characterIconWeapon.setVisibility(View.VISIBLE);
            characterIconWeapon.setImageResource(m.weapon == MainActivity.PHYSIC ?  R.drawable.physicicon:R.drawable.magicicon);
            String tmp = "";
            switch(m.status){
                case -1:
                    tmp = "死亡";
                    characterIconState.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.redrect));
                    break;
                case 0:
                    tmp = "濒危";
                    characterIconState.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.redrect));
                    break;
                case 1:
                    tmp = "重伤";
                    characterIconState.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.orangerect));
                    break;
                case 2:
                    tmp = "受伤";
                    characterIconState.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.pinkrect));
                    break;
                case 3:
                    tmp = "轻伤";
                    characterIconState.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.yellowrect));
                    break;
                case 4:
                    tmp = "健康";
                    characterIconState.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.greenrect));
                    break;
            }
            characterIconState.setText(tmp);
            if(m.characterSkill != null){
                characterIconSkillImage.setBackground(m.characterSkill.skill.skillImage);
                characterIconSkillName.setText(m.characterSkill.skill.name);
                tmp = "";
                switch(m.characterSkill.skill.increaseMethod){
                    case Skill.AUTO:
                        tmp = "自动回复";
                        characterIconSkillIncreasingMethod.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.greenrect));
                        break;
                    case Skill.ATTACK:
                        tmp = "攻击回复";
                        characterIconSkillIncreasingMethod.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.redrect));
                        break;
                    case Skill.DEFEND:
                        tmp = "受击回复";
                        characterIconSkillIncreasingMethod.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.yellowrect));
                        break;
                    case Skill.MOVING:
                        tmp = "移动回复";
                        characterIconSkillIncreasingMethod.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.pinkrect));
                        break;
                }
                characterIconSkillIncreasingMethod.setText(tmp);
                characterIconSkillAutoRelease.setText(m.characterSkill.skill.autoRelease? "自动触发":"手动触发");
                characterIconSkillDuration.setText(m.characterSkill.duration/33+"s");
                characterIconSkillIllustration.setVisibility(View.VISIBLE);
                characterIconSkillIllustration.setText(m.characterSkill.getCharacterSkillIllustration());
                characterIconSkillPoint.setVisibility(View.VISIBLE);
                characterIconLighting.setVisibility(View.VISIBLE);
                characterIconSkillPoint.setText(m.characterSkill.initSkillPoint/33 + "/" + m.characterSkill.skillPoint/33);
            }else{
                characterIconSkillImage.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.undefined));
                characterIconSkillName.setText("");
                characterIconSkillIncreasingMethod.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.grey2rect));
                characterIconSkillIncreasingMethod.setText("--");
                characterIconSkillAutoRelease.setText("--");
                characterIconSkillDuration.setText("--");
                characterIconSkillIllustration.setVisibility(GONE);
                characterIconSkillIllustration.setText("");
                characterIconSkillPoint.setVisibility(View.INVISIBLE);
                characterIconLighting.setVisibility(View.INVISIBLE);
            }
            if(mode == BROWSER){
                characterIconDismissIcon.setVisibility(View.VISIBLE);
                characterIconDismiss.setVisibility(View.VISIBLE);
                characterIconDismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        characterIconShade6.setVisibility(View.VISIBLE);
                        characterIconShade6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        characterIconDismissConfirmLayout.setVisibility(View.VISIBLE);
                        characterIconDismissCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                characterIconShade6.setVisibility(View.INVISIBLE);
                                characterIconDismissConfirmLayout.setVisibility(View.INVISIBLE);
                            }
                        });
                        characterDismissIcon.setImageResource(getResourceByString(""+m.name+"icon"));
                        characterDismissElite.setImageResource(getResourceByString("elite"+m.elite));
                        characterDismissLv.setText("Lv"+m.level);
                        characterDismissWeapon.setImageResource(getResourceByString((m.weapon == PHYSIC?"physic":"magic")+"battleicon"));
                        characterIconDismissConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addIconList.remove(m);
                                characterAdapter.notifyItemRemoved(choose);
                                characterAdapter.notifyItemRangeChanged(choose,characterAdapter.getItemCount()-choose);
                                onChosen(-1);
                                characterIconShade6.setVisibility(View.INVISIBLE);
                                characterIconDismissConfirmLayout.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });
            }
            updateCharacterSquare(m);
        }else{
            characterIconImage.setImageResource(getResourceByString("undefined"));
            characterIconText.setText("??");
            characterIconName.setText("??");
            characterIconElite.setVisibility(View.INVISIBLE);
            characterIconEliteUp.setVisibility(View.INVISIBLE);
            characterIconLevel.setText("??");
            characterIconMaxLevel.setText("/??");
            characterIconHp.setText("---");
            characterIconDefend.setText("---");
            characterIconAttack.setText("---");
            characterIconMagicalDefend.setText("---");
            characterIconRetreat.setText("---");
            characterIconCost.setText("---");
            characterIconBlock.setText("---");
            characterIconSpeed.setText("---");
            characterIconLevelUp.setVisibility(View.INVISIBLE);
            characterIconState.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.greyrect));
            characterIconState.setText("??");
            characterIconSkillImage.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.undefined));
            characterIconSkillName.setText("");
            characterIconSkillIncreasingMethod.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.grey2rect));
            characterIconSkillIncreasingMethod.setText("--");
            characterIconSkillAutoRelease.setText("--");
            characterIconSkillDuration.setText("--");
            characterIconSkillIllustration.setVisibility(GONE);
            characterIconSkillIllustration.setText("");
            characterIconSkillPoint.setVisibility(View.INVISIBLE);
            characterIconLighting.setVisibility(View.INVISIBLE);
            equipmentSquareLayout.setVisibility(GONE);
            characterIconArrange.setText("--");
            characterIconRetreatable.setText("--");
            characterIconWeapon.setVisibility(View.INVISIBLE);
            characterIconDismiss.setVisibility(View.INVISIBLE);
            characterIconDismissIcon.setVisibility(View.INVISIBLE);
        }
    }

    private void updateCharacterSquare(final CharacterIconMessage m){
        if(m.characterSquare != null){
            equipmentSquareLayout.setVisibility(View.VISIBLE);
            characterIconEquipmentSquare.setCharacterSquare(m.characterSquare);
            characterIconEquipmentSquare.updateEquipment();
            equipmentSquareLayout.setSoundEffectsEnabled(false);
            equipmentSquareLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)MAINCONTEXT).playSound(MainActivity.voiceBtn,1.0f,1.0f);
                    if(!isEquipmentColumnShow && !isChooseTeamShow){
                        onEquipmentChoosing(m);
                    }
                }
            });
            if(m.characterSquare.weapon != null){
                characterIconWeaponLayout.setVisibility(View.VISIBLE);
                equipmentSquareLayout.setSoundEffectsEnabled(false);
                equipmentSquareLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)MAINCONTEXT).playSound(MainActivity.voiceBtn,1.0f,1.0f);
                        if(!isEquipmentColumnShow && !isChooseTeamShow){
                            onEquipmentChoosing(m);
                        }
                    }
                });
                if(m.characterSquare.weapon.imageId != 0) characterIconWeaponIcon.setImageResource(m.characterSquare.weapon.imageId);
                //4卓越黄 3优良紫 2标准蓝 1普通绿 0缺陷灰
                switch(m.characterSquare.weapon.quality){
                    case 0:
                        characterIconWeaponQuality.setText("缺陷");
                        characterIconWeaponQuality.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.greyrect));
                        break;
                    case 1:
                        characterIconWeaponQuality.setText("普通");
                        characterIconWeaponQuality.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.greenrect));
                        break;
                    case 2:
                        characterIconWeaponQuality.setText("标准");
                        characterIconWeaponQuality.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.bluerect));
                        break;
                    case 3:
                        characterIconWeaponQuality.setText("优良");
                        characterIconWeaponQuality.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.pinkrect));
                        break;
                    default:
                        characterIconWeaponQuality.setText("卓越");
                        characterIconWeaponQuality.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.yellowrect));
                        break;
                }
                characterIconWeaponName.setText(m.characterSquare.weapon.name);
                characterIconWeaponName.setTextColor(MAINCONTEXT.getResources().getColor(R.color.colorYellow));
                characterIconWeaponIllustration.setText(m.characterSquare.weapon.illustration);;
            }else{
                characterIconWeaponLayout.setVisibility(View.INVISIBLE);
            }

            if(m.characterSquare.armor != null){
                characterIconArmorLayout.setVisibility(View.VISIBLE);
                if(m.characterSquare.armor.imageId != 0) characterIconArmorIcon.setImageResource(m.characterSquare.armor.imageId);
                //4卓越黄 3优良紫 2标准蓝 1普通绿 0缺陷灰
                switch(m.characterSquare.armor.quality){
                    case 0:
                        characterIconArmorQuality.setText("缺陷");
                        characterIconArmorQuality.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.greyrect));
                        break;
                    case 1:
                        characterIconArmorQuality.setText("普通");
                        characterIconArmorQuality.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.greenrect));
                        break;
                    case 2:
                        characterIconArmorQuality.setText("标准");
                        characterIconArmorQuality.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.bluerect));
                        break;
                    case 3:
                        characterIconArmorQuality.setText("优良");
                        characterIconArmorQuality.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.pinkrect));
                        break;
                    default:
                        characterIconArmorQuality.setText("卓越");
                        characterIconArmorQuality.setBackground(MAINCONTEXT.getResources().getDrawable(R.drawable.yellowrect));
                        break;
                }
                characterIconArmorName.setText(m.characterSquare.armor.name);
                characterIconArmorName.setTextColor(MAINCONTEXT.getResources().getColor(R.color.colorLightBlue));
                characterIconArmorIllustration.setText(m.characterSquare.armor.illustration);;
            }else{
                characterIconArmorLayout.setVisibility(View.INVISIBLE);
            }
        }else{
            equipmentSquareLayout.setVisibility(GONE);
        }

    }

    private int getResourceByString(String str){
        Resources res = MAINCONTEXT.getResources();
        int resourceId = res.getIdentifier(str,"drawable",MAINCONTEXT.getPackageName());
        return resourceId;
    }

    public void setCharacterNumber(int number){
        characterIconChooseNumber.setText("队伍人数："+number+"/"+maxTeamMember);
    }

    private void onEquipmentChoosing(final CharacterIconMessage iconMessage){//展开equipment装备栏时调用
        final CharacterSquare characterSquare = iconMessage.characterSquare;
        characterIconEquipmentChoose.setVisibility(View.VISIBLE);
        isEquipmentColumnShow = true;
        equipmentSquare.setCharacterSquare(characterSquare);
        equipmentSquare.updateEquipment();
        weaponRecyclerView.setLayoutManager(new LinearLayoutManager(MAINCONTEXT));
        final EquipmentAdapter weaponAdapter = new EquipmentAdapter(MainActivity.weaponList,MAINCONTEXT,characterSquare,iconMessage,equipmentSquare);
        final EquipmentAdapter armorAdapter = new EquipmentAdapter(MainActivity.armorList,MAINCONTEXT,characterSquare,iconMessage,equipmentSquare);
        weaponAdapter.anotherAdapter = armorAdapter;
        armorAdapter.anotherAdapter = weaponAdapter;
        weaponRecyclerView.setAdapter(weaponAdapter);
        armorRecyclerView.setLayoutManager(new LinearLayoutManager(MAINCONTEXT));
        armorRecyclerView.setAdapter(armorAdapter);
        equipmentSquareWeaponChangePosition.setSoundEffectsEnabled(false);
        equipmentSquareWeaponChangePosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)MAINCONTEXT).playSound(MainActivity.voiceBtn,1.0f,1.0f);
                characterSquare.setEquipment(characterSquare.weapon);
                equipmentSquare.updateEquipment();
                armorAdapter.notifyDataSetChanged();
                weaponAdapter.notifyDataSetChanged();
            }
        });
        equipmentSquareArmorChangePosition.setSoundEffectsEnabled(false);
        equipmentSquareArmorChangePosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)MAINCONTEXT).playSound(MainActivity.voiceBtn,1.0f,1.0f);
                characterSquare.setEquipment(characterSquare.armor);
                equipmentSquare.updateEquipment();
                armorAdapter.notifyDataSetChanged();
                weaponAdapter.notifyDataSetChanged();
            }
        });

        for(int i = 0; i < 6; i++){
            TextView temp = armorIconQualityList.get(i);
            final int j = i;
            temp.setSoundEffectsEnabled(false);
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)MAINCONTEXT).playSound(MainActivity.voiceBtn,1.0f,1.0f);
                    armorAdapter.getFilter().filter(String.valueOf(j));
                }
            });
            temp = weaponIconQualityList.get(i);
            temp.setSoundEffectsEnabled(false);
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)MAINCONTEXT).playSound(MainActivity.voiceBtn,1.0f,1.0f);
                    weaponAdapter.getFilter().filter(String.valueOf(j));
                }
            });
        }
        equipmentSquareBack.setSoundEffectsEnabled(false);
        equipmentSquareBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)MAINCONTEXT).playSound(MainActivity.voiceBtn,1.0f,1.0f);
                characterIconEquipmentChoose.setVisibility(GONE);
                characterIconEquipmentSquare.updateEquipment();
                isEquipmentColumnShow = false;
                if(characterAdapter.chooseIcon != -1)updateCharacterSquare(addIconList.get(characterAdapter.chooseIcon));
            }
        });
    }

    private void initLevelUpLayout(CharacterIconMessage m1, final  int chooseIcon){
        final CharacterIconMessage m = m1;
        characterIconLevelUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                characterIconShade3.setVisibility(View.VISIBLE);
                characterIconLevelUpLayout.setVisibility(View.VISIBLE);
                levelAfterUpgrade = m.level;
                characterIconLevelText.setText("Lv " + m.level + " -> Lv " + levelAfterUpgrade);
                for(int i = 0; i < 4; i++){
                    expNumber[i] = 0;
                    expCardNumber[i] = pref.getInt(""+i,0);
                    bagExpCardNumber[i] = expCardNumber[i];
                    expNumberList.get(i).setText("x0/"+bagExpCardNumber[i]);
                }
                characterLevelUpAbilityChangeTextView.setText("耐久 "+m.getAbility(HP)+" -> "+(int)(m.getAbility(HP)+ m.levelUpHp*(levelAfterUpgrade - m.level))
                        +"      攻击 "+m.getAbility(ATK)+" -> "+(int)(m.getAbility(ATK)+ m.levelUpAtk*(levelAfterUpgrade - m.level))
                        +"      防御 "+m.getAbility(DEF)+" -> "+(int)(m.getAbility(DEF)+ m.levelUpDef*(levelAfterUpgrade - m.level))+"  ");
            }
        });
        characterIconShade3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                characterIconShade3.setVisibility(View.GONE);
                characterIconLevelUpLayout.setVisibility(View.GONE);
                characterIconPromotionLayout.setVisibility(View.GONE);
            }
        });
        characterIconLevelCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                characterIconShade3.setVisibility(View.GONE);
                characterIconLevelUpLayout.setVisibility(View.GONE);
            }
        });
        characterIconLevelUpLayout.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {}});
        characterIconAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < 4; i++){
                    if(levelAfterUpgrade < (20*(i+1)) || (i==3 && levelAfterUpgrade >= 80)){
                        if(expCardNumber[i] > 0 && levelAfterUpgrade < m.maxLevel){
                            levelAfterUpgrade++;
                            characterIconLevelText.setText("Lv " + m.level + " -> Lv " + levelAfterUpgrade);
                            expCardNumber[i]--;
                            expNumber[i]++;
                            expNumberList.get(i).setText("x"+expNumber[i] + "/"+ +bagExpCardNumber[i]);
                        }
                        break;
                    }
                }
                characterLevelUpAbilityChangeTextView.setText("耐久 "+m.getAbility(HP)+" -> "+(int)(m.getAbility(HP)+ m.levelUpHp*(levelAfterUpgrade - m.level))
                        +"      攻击 "+m.getAbility(ATK)+" -> "+(int)(m.getAbility(ATK)+ m.levelUpAtk*(levelAfterUpgrade - m.level))
                        +"      防御 "+m.getAbility(DEF)+" -> "+(int)(m.getAbility(DEF)+ m.levelUpDef*(levelAfterUpgrade - m.level))+"  ");
            }
        });
        characterIconReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(levelAfterUpgrade > m.level){
                    for(int i = 0; i < 4; i++){
                        if(levelAfterUpgrade <= (20*(i+1)) || (i==3 && levelAfterUpgrade > 80)){
                            levelAfterUpgrade--;
                            characterIconLevelText.setText("Lv " + m.level + " -> Lv " + levelAfterUpgrade);
                            expCardNumber[i]++;
                            expNumber[i]--;
                            expNumberList.get(i).setText("x"+expNumber[i]+"/"+bagExpCardNumber[i]);
                            break;
                        }
                    }
                    characterLevelUpAbilityChangeTextView.setText("耐久 "+m.getAbility(HP)+" -> "+(int)(m.getAbility(HP)+ m.levelUpHp*(levelAfterUpgrade - m.level))
                            +"      攻击 "+m.getAbility(ATK)+" -> "+(int)(m.getAbility(ATK)+ m.levelUpAtk*(levelAfterUpgrade - m.level))
                            +"      防御 "+m.getAbility(DEF)+" -> "+(int)(m.getAbility(DEF)+ m.levelUpDef*(levelAfterUpgrade - m.level))+"  ");
                }
            }
        });
        characterIconMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelAfterUpgrade = m.level;
                characterIconLevelText.setText("Lv " + m.level + " -> Lv " + levelAfterUpgrade);
                for(int i = 0; i < 4; i++){
                    expCardNumber[i] = pref.getInt(""+i,0);
                    expNumber[i] = 0;
                    expNumberList.get(i).setText("x"+expNumber[i]+"/"+bagExpCardNumber[i]);
                }
                characterLevelUpAbilityChangeTextView.setText("耐久 "+m.getAbility(HP)+" -> "+(int)(m.getAbility(HP)+ m.levelUpHp*(levelAfterUpgrade - m.level))
                        +"      攻击 "+m.getAbility(ATK)+" -> "+(int)(m.getAbility(ATK)+ m.levelUpAtk*(levelAfterUpgrade - m.level))
                        +"      防御 "+m.getAbility(DEF)+" -> "+(int)(m.getAbility(DEF)+ m.levelUpDef*(levelAfterUpgrade - m.level))+"  ");
            }
        });
        characterIconMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelAfterUpgrade = m.level;
                for(int i = 0; i < 4; i++){
                    expCardNumber[i] = pref.getInt(""+i,0);
                    expNumber[i] = 0;
                    expNumberList.get(i).setText("x"+expNumber[i]+"/"+bagExpCardNumber[i]);
                }
                for(int i = (levelAfterUpgrade < 60 ? (levelAfterUpgrade/20) : 3); i <= (m.maxLevel <= 60 ? ((m.maxLevel-1)/20) : 3); i++){
                    if(i < 3){
                        if(expCardNumber[levelAfterUpgrade/20] > 0){
                            int temp = Math.min(Math.min((i+1)*20 - levelAfterUpgrade, expCardNumber[levelAfterUpgrade/20]),m.maxLevel - levelAfterUpgrade);
                            levelAfterUpgrade += temp;
                            expCardNumber[i] -= temp;
                            expNumber[i] = temp;
                            expNumberList.get(i).setText("x"+expNumber[i]+"/"+bagExpCardNumber[i]);
                            characterIconLevelText.setText("Lv " + m.level + " -> Lv " + levelAfterUpgrade);
                            if(levelAfterUpgrade % 20 != 0){
                                break;
                            }
                        }else{
                            break;
                        }
                    }else{
                        if(expCardNumber[3] > 0){
                            int temp = Math.min(m.maxLevel - levelAfterUpgrade, expCardNumber[3]);
                            levelAfterUpgrade += temp;
                            expCardNumber[i] -= temp;
                            expNumber[i] = temp;
                            expNumberList.get(i).setText("x"+expNumber[i]+"/"+bagExpCardNumber[i]);
                            characterIconLevelText.setText("Lv " + m.level + " -> Lv " + levelAfterUpgrade);
                        }
                    }
                }
                characterLevelUpAbilityChangeTextView.setText("耐久 "+m.getAbility(HP)+" -> "+(int)(m.getAbility(HP)+ m.levelUpHp*(levelAfterUpgrade - m.level))
                        +"      攻击 "+m.getAbility(ATK)+" -> "+(int)(m.getAbility(ATK)+ m.levelUpAtk*(levelAfterUpgrade - m.level))
                        +"      防御 "+m.getAbility(DEF)+" -> "+(int)(m.getAbility(DEF)+ m.levelUpDef*(levelAfterUpgrade - m.level))+"  ");
            }
        });
        characterIconLevelConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.attack += m.levelUpAtk*(levelAfterUpgrade - m.level);
                m.physicalDefend += m.levelUpDef*(levelAfterUpgrade - m.level);
                m.maxHp += m.levelUpHp*(levelAfterUpgrade - m.level);
                if(m.level != levelAfterUpgrade)Toast.makeText(MAINCONTEXT, "干员等级提升成功！", Toast.LENGTH_SHORT).show();
                m.level = levelAfterUpgrade;
                SharedPreferences.Editor editor = MAINCONTEXT.getSharedPreferences("Item", MODE_PRIVATE).edit();
                for(int i = 0; i < 4; i++){
                    editor.putInt(""+i, expCardNumber[i]);
                }
                editor.apply();
                characterIconEliteUp.setVisibility((m.level == m.maxLevel) && (m.elite < 3)? View.VISIBLE:View.INVISIBLE);
                characterIconLevel.setText(String.valueOf(m.level));
                characterIconHp.setText(String.valueOf(m.getAbility(HP)));
                characterIconDefend.setText(String.valueOf(m.getAbility(DEF)));
                characterIconAttack.setText(String.valueOf(m.getAbility(ATK)));
                characterIconShade3.setVisibility(View.GONE);
                characterIconLevelUpLayout.setVisibility(View.GONE);
                if(m.elite < 3 && (m.level == m.maxLevel)){
                    initPromotionUpLayout(m , chooseIcon);
                }
                characterAdapter.notifyItemRangeChanged(chooseIcon, 1);
                MainActivity.mChooseList.remove(m);
                setCharacterNumber(MainActivity.mChooseList.size());
            }
        });
    }

    private void initPromotionUpLayout(CharacterIconMessage m1, final  int chooseIcon){
        final CharacterIconMessage m = m1;
        characterIconElite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m.elite < 3 && m.level == m.maxLevel){
                    characterIconPromotionLayout.setVisibility(View.VISIBLE);
                    characterIconShade3.setVisibility(View.VISIBLE);
                }
            }
        });
        item1ImageView.setImageResource(MainActivity.itemModelList.get(m.getPromotionItem(m.elite,false)).imageId);
        item1Number.setText(" x "+m.getPromotionItemNumber(m.elite,false)+" / "+pref.getInt(""+m.getPromotionItem(m.elite,false),0)+" ");
        if(m.getPromotionItemNumber(m.elite,false) <= pref.getInt(""+m.getPromotionItem(m.elite,false),0)){
            item1Number.setTextColor(getResources().getColor(R.color.colorWhite));
            item1Number.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        }else{
            item1Number.setTextColor(getResources().getColor(R.color.colorRed));
            item1Number.setBackgroundColor(getResources().getColor(R.color.colorLightBlack));
        }
        item2ImageView.setImageResource(MainActivity.itemModelList.get(m.getPromotionItem(m.elite,true)).imageId);
        item2Number.setText(" x "+m.getPromotionItemNumber(m.elite,true)+" / "+pref.getInt(""+m.getPromotionItem(m.elite,true),0)+" ");
        if(m.getPromotionItemNumber(m.elite,true) <= pref.getInt(""+m.getPromotionItem(m.elite,true),0)){
            item2Number.setTextColor(getResources().getColor(R.color.colorWhite));
            item2Number.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        }else{
            item2Number.setTextColor(getResources().getColor(R.color.colorRed));
            item2Number.setBackgroundColor(getResources().getColor(R.color.colorLightBlack));
        }
        characterIconPromotionBeforeLevel.setText(""+m.elite);
        characterIconPromotionAfterLevel.setText(""+(m.elite+1));
        eliteBeforeImageView.setImageResource(getResourceByString("elite"+m.elite));
        eliteAfterImageView.setImageResource(getResourceByString("elite"+(m.elite+1)));
        characterIconPromotionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                characterIconPromotionLayout.setVisibility(View.GONE);
                characterIconShade3.setVisibility(View.GONE);
            }
        });
        characterIconPromotionConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m.getPromotionItemNumber(m.elite,true) <= pref.getInt(""+m.getPromotionItem(m.elite,true),0)){
                    if(m.getPromotionItemNumber(m.elite,false) <= pref.getInt(""+m.getPromotionItem(m.elite,false),0)){
                        SharedPreferences.Editor editor = MAINCONTEXT.getSharedPreferences("Item", MODE_PRIVATE).edit();
                        editor.putInt(""+m.getPromotionItem(m.elite,false), pref.getInt(""+m.getPromotionItem(m.elite,false),0) - m.getPromotionItemNumber(m.elite,false));
                        editor.putInt(""+m.getPromotionItem(m.elite,true), pref.getInt(""+m.getPromotionItem(m.elite,true),0) - m.getPromotionItemNumber(m.elite,true));
                        editor.apply();
                        m.level = 1;
                        m.elite++;
                        m.maxLevel = 40 + 10*(m.elite + m.quality);
                        characterIconEliteUp.setVisibility((m.level == m.maxLevel) && (m.elite < 3)? View.VISIBLE:View.INVISIBLE);
                        characterIconLevel.setText(String.valueOf(m.level));
                        characterIconShade3.setVisibility(View.GONE);
                        characterIconPromotionLayout.setVisibility(View.GONE);
                        if(m.elite < 3 && (m.level == m.maxLevel)){
                            initPromotionUpLayout(m, chooseIcon);
                        }
                        characterIconElite.setImageResource(getResourceByString("elite"+m.elite));
                        characterAdapter.notifyItemRangeChanged(chooseIcon, 1);
                        characterIconMaxLevel.setText("/"+m.maxLevel);
                        Toast.makeText(MAINCONTEXT, "干员晋升成功！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
