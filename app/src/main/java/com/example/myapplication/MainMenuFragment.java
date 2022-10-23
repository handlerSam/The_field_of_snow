package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static com.example.myapplication.CharacterFragment.ATK;
import static com.example.myapplication.CharacterFragment.DEF;
import static com.example.myapplication.CharacterFragment.HP;
import static com.example.myapplication.MainActivity.BAGFRAGMENT;
import static com.example.myapplication.MainActivity.BROWSERFRAGMENT;
import static com.example.myapplication.MainActivity.MAPFRAGMENT;
import static com.example.myapplication.MainActivity.placeList;

public class MainMenuFragment extends Fragment {
    public ConstraintSet sampleSet = new ConstraintSet();
    ConstraintLayout shadeConstraintLayout;
    ArrayList<ImageView> shadeList = new ArrayList<>();
    Context MAINCONTEXT;
    ArrayList<ConstraintLayout> layerList = new ArrayList<>();
    FrameLayout menuTeamLayout;
    TextView menuTeam;
    TextView menuSourceNumber;
    TextView menuHCYNumber;
    TextView menuSourceIngotNumber;
    //TextView menuFoodNumber;
    //TextView menuWineNumber;
    ConstraintLayout teamUnavailable;
    LinearLayout teamList;
    ConstraintLayout characterUnavailable;
    LinearLayout characterList;
    FrameLayout teamManage;
    TextView XYText;
    TextView siteText;
    ImageView characterIconImage;
    TextView characterIconName;
    ImageView characterIconElite;
    TextView characterIconLV;
    TextView characterIconState;
    TextView stateColumnHp;
    TextView stateColumnAttack;
    TextView stateColumnDefend;
    TextView stateColumnMagicalDefend;
    TextView stateColumnBlock;
    EquipmentSquare characterIconWeaponSquare;
    ImageView characterIconWeaponIcon;
    TextView characterIconWeaponQuality;
    TextView characterIconWeaponName;
    TextView characterIconWeaponIllustration;
    ImageView characterIconArmorIcon;
    TextView characterIconArmorQuality;
    TextView characterIconArmorName;
    TextView characterIconArmorIllustration;
    ImageView characterIconSkillImage;
    TextView characterIconSkillPoint;
    TextView characterIconSkillName;
    TextView characterIconSkillIncreasingMethod;
    TextView characterIconSkillAutoRelease;
    TextView characterIconSkillDuration;
    TextView characterIconSkillIllustration;
    LinearLayout characterIconWeaponLayout;
    LinearLayout characterIconArmorLayout;
    LinearLayout characterDetailInformation;
    FrameLayout toCharacterFragment;
    FrameLayout mapIcon;
    TextView hint1;
    FrameLayout toBagFragment;
    TextView placeEventNumberTextView;
    LinearLayout placeEventButtonLayout;
    RecyclerView placeContentRecyclerView;
    ConstraintLayout placeContentLayout;
    public static int chooseTeam = 0;
    public SharedPreferences pref;
    //仅在character更改编队时以下两个变量不为null
    public CharacterIcon changingCharacter;
    public Team changingCharacrerFormerTeam;

    boolean isTeamShow = false;
    boolean isCharacterShow = false;
    boolean isShadeShow = false;
    boolean isCharacterDetailShow = false;
    int initX = 150 + 60;//阴影的中心
    int initY = 550;
    int intervalX = 250;//两个阴影的X间距
    int intervalY = 100;

    public MainMenuFragment(Context context){
        MAINCONTEXT = context;
        pref = context.getSharedPreferences("Item",MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_layout,container,false);
        initFind(v);
        initSet(v);
        return v;
    }

    private void initFind(View v){
        changingCharacter = null;
        changingCharacrerFormerTeam = null;
        shadeConstraintLayout = (ConstraintLayout) v.findViewById(R.id.temp22);
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 5; j++){
                ImageView temp = (ImageView) v.findViewById(getResource("shade_"+j+""+i));
                shadeList.add(temp);
            }
        }

        for(int i = 4; i > 0; i--){
            ConstraintLayout temp = (ConstraintLayout) v.findViewById(getResource("main_layer"+i));
            layerList.add(temp);
        }
        menuTeam = (TextView) v.findViewById(R.id.menuTeam);
        menuTeamLayout = (FrameLayout) v.findViewById(R.id.menuTeamLayout);
        menuSourceNumber = (TextView) v.findViewById(R.id.menuSourceNumber);
        menuHCYNumber = (TextView) v.findViewById(R.id.menuHCYNumber);
        menuSourceIngotNumber = (TextView) v.findViewById(R.id.menuSourceIngotNumber);
        teamUnavailable = (ConstraintLayout) v.findViewById(R.id.teamUnavailable);
        teamList = (LinearLayout) v.findViewById(R.id.teamList);
        //menuFoodNumber = v.findViewById(R.id.menuFoodNumber);
        //menuWineNumber = v.findViewById(R.id.menuWineNumber);
        characterUnavailable = (ConstraintLayout) v.findViewById(R.id.characterUnavailable);
        characterList = (LinearLayout) v.findViewById(R.id.characterList);
        teamManage = (FrameLayout) v.findViewById(R.id.teamManage);
        XYText = (TextView) v.findViewById(R.id.XYText);
        siteText = (TextView) v.findViewById(R.id.SiteText);
        characterIconImage = (ImageView) v.findViewById(R.id.characterIconImage);
        characterIconName = (TextView) v.findViewById(R.id.characterIconName);
        characterIconElite = (ImageView) v.findViewById(R.id.characterIconElite);
        characterIconLV = (TextView) v.findViewById(R.id.characterIconLV);
        characterIconState = (TextView) v.findViewById(R.id.characterIconState);
        stateColumnHp = (TextView) v.findViewById(R.id.stateColumnHp);
        stateColumnAttack = (TextView) v.findViewById(R.id.stateColumnAttack);
        stateColumnDefend = (TextView) v.findViewById(R.id.stateColumnDefend);
        stateColumnMagicalDefend = (TextView) v.findViewById(R.id.stateColumnMagicalDefend);
        stateColumnBlock = (TextView) v.findViewById(R.id.stateColumnBlock);
        characterIconWeaponSquare = (EquipmentSquare) v.findViewById(R.id.characterIconWeaponSquare);
        characterIconWeaponIcon = (ImageView) v.findViewById(R.id.characterIconWeaponIcon);
        characterIconWeaponQuality = (TextView) v.findViewById(R.id.characterIconWeaponQuality);
        characterIconWeaponName = (TextView) v.findViewById(R.id.characterIconWeaponName);
        characterIconWeaponIllustration = (TextView) v.findViewById(R.id.characterIconWeaponIllustration);
        characterIconArmorIcon = (ImageView) v.findViewById(R.id.characterIconArmorIcon);
        characterIconArmorQuality = (TextView) v.findViewById(R.id.characterIconArmorQuality);
        characterIconArmorName = (TextView) v.findViewById(R.id.characterIconArmorName);
        characterIconArmorIllustration = (TextView) v.findViewById(R.id.characterIconArmorIllustration);
        characterIconSkillImage = (ImageView) v.findViewById(R.id.characterIconSkillImage);
        characterIconSkillPoint = (TextView) v.findViewById(R.id.characterIconSkillPoint);
        characterIconSkillName = (TextView) v.findViewById(R.id.characterIconSkillName);
        characterIconSkillIncreasingMethod = (TextView) v.findViewById(R.id.characterIconSkillIncreasingMethod);
        characterIconSkillAutoRelease = (TextView) v.findViewById(R.id.characterIconSkillAutoRelease);
        characterIconSkillDuration = (TextView) v.findViewById(R.id.characterIconSkillDuration);
        characterIconSkillIllustration = (TextView) v.findViewById(R.id.characterIconSkillIllustration);
        characterIconWeaponLayout = (LinearLayout) v.findViewById(R.id.characterIconWeaponLayout);
        characterIconArmorLayout = (LinearLayout) v.findViewById(R.id.characterIconArmorLayout);
        characterDetailInformation = (LinearLayout) v.findViewById(R.id.characterDetailInformation);
        toCharacterFragment = (FrameLayout) v.findViewById(R.id.toCharacterFragment);
        mapIcon = (FrameLayout) v.findViewById(R.id.mapIcon);
        hint1 = (TextView) v.findViewById(R.id.hint1);
        toBagFragment = (FrameLayout) v.findViewById(R.id.toBagFragment);
        placeEventNumberTextView = (TextView) v.findViewById(R.id.placeEventNumberTextView);
        placeEventButtonLayout = (LinearLayout) v.findViewById(R.id.placeEventButtonLayout);
        placeContentRecyclerView = (RecyclerView) v.findViewById(R.id.placeContentRecyclerView);
        placeContentLayout = (ConstraintLayout) v.findViewById(R.id.placeContentLayout);
    }

    private void initSet(View v){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCharacterDetailShow){
                    characterDetailInformation.setVisibility(View.GONE);
                    isCharacterDetailShow = false;
                }
            }
        });
        toCharacterFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.chooseTeam = chooseTeam;
                Message message = new Message();
                message.what = BROWSERFRAGMENT;
                ((MainActivity)MAINCONTEXT).handler.sendMessage(message);
            }
        });
        toBagFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = BAGFRAGMENT;
                ((MainActivity)MAINCONTEXT).handler.sendMessage(message);
            }
        });
        menuTeamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCharacterDetailShow){
                    characterDetailInformation.setVisibility(View.GONE);
                    isCharacterDetailShow = false;
                }
                if(isTeamShow){//已经展开
                    teamUnavailable.setVisibility(View.INVISIBLE);
                    teamList.removeAllViews();
                }else{
                    teamUnavailable.setVisibility(View.VISIBLE);
                    for(int i = 0; i < MainActivity.teamList.size(); i++){
                        final int j = i;
                        final TeamInformationLayout layout = new TeamInformationLayout(getContext(),MainActivity.teamList.get(i), teamList,null, MainMenuFragment.this);
                        View.OnClickListener onClickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(changingCharacter == null){
                                    chooseTeam = j;
                                    setTeam(MainActivity.teamList.get(j));
                                    teamUnavailable.setVisibility(GONE);
                                    teamList.removeAllViews();
                                    isTeamShow = !isTeamShow;
                                }else{
                                    if(MainActivity.teamList.get(j).characterList.size() < 12 && changingCharacrerFormerTeam != MainActivity.teamList.get(j)){
                                        //成功修改了干员的所属编队
                                        changingCharacrerFormerTeam.characterList.remove(changingCharacter.message);
                                        MainActivity.teamList.get(j).characterList.add(changingCharacter.message);
                                        changingCharacter.message.position = -1;
                                        changingCharacter.layout.removeView(changingCharacter.v);
                                        layout.teamCharacterColumn.addView(changingCharacter.v);
                                        changingCharacter.teamId = j;
                                        changingCharacter.layout = layout.teamCharacterColumn;
                                        if(changingCharacrerFormerTeam == MainActivity.teamList.get(chooseTeam))setTeam(changingCharacrerFormerTeam);
                                        changingCharacter.setState(false);
                                        hint1.setVisibility(View.INVISIBLE);
                                        changingCharacter = null;
                                        changingCharacrerFormerTeam = null;
                                    }
                                }
                            }
                        };
                        layout.setOnClickListener(onClickListener);
                    }
                }
                isTeamShow = !isTeamShow;
            }
        });
        teamUnavailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCharacterShow){
                    characterDetailInformation.setVisibility(View.GONE);
                    isCharacterShow = false;
                }
                changingCharacter = null;
                changingCharacrerFormerTeam = null;
                teamUnavailable.setVisibility(View.INVISIBLE);
                teamList.removeAllViews();
                isTeamShow = !isTeamShow;
            }
        });
        menuSourceNumber.setText(""+MainActivity.sourceIngotNumber);
        menuHCYNumber.setText(""+MainActivity.sourceNumber);
        menuSourceIngotNumber.setText(""+MainActivity.hcyNumber);
        setTeam(MainActivity.teamList.get(chooseTeam));
        for(int i = 0; i < shadeList.size(); i++){
            ImageView image = shadeList.get(i);
            final int temp = i;
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isCharacterShow){//已经展开
                        characterUnavailable.setVisibility(View.INVISIBLE);
                        characterList.removeAllViews();
                    }else{
                        characterUnavailable.setVisibility(View.VISIBLE);
                        for(int i = 0; i < MainActivity.teamList.get(chooseTeam).characterList.size(); i++){
                            final CharacterIconMessage message = MainActivity.teamList.get(chooseTeam).characterList.get(i);
                            for(CharacterIconMessage temp2: MainActivity.teamList.get(chooseTeam).characterList){
                                if(temp2.character != null && temp2.position != -1){
                                    if(temp2.position == temp){
                                        layerList.get(temp/5).removeView(temp2.character);
                                        temp2.position = -1;
                                        temp2.character = null;
                                        break;
                                    }
                                }
                            }
                            View.OnClickListener listener = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //删去这个位置本来的人
                                    if(message.character != null){
                                        layerList.get(message.position/5).removeView(message.character);
                                    }
                                    Character character = new Character(MAINCONTEXT,message.name,message.getAbility(HP),message.getAbility(ATK),true,message.getAbility(DEF),message.magicalDefend,message.weapon,message.canExplosion,null,null);
                                    message.character = character;
                                    message.position = temp;
                                    ConstraintLayout backGround = layerList.get(temp/5);
                                    int id = View.generateViewId();
                                    character.setId(id);
                                    backGround.addView(character);
                                    sampleSet.clone(backGround);
                                    sampleSet.connect(id,ConstraintSet.LEFT,backGround.getId(),ConstraintSet.LEFT, initX-MainActivity.CharWidth/2 + (temp % 5)*intervalX);
                                    sampleSet.connect(id,ConstraintSet.TOP,backGround.getId(),ConstraintSet.TOP,initY-MainActivity.CharHeight/7*5 + (temp/5)*intervalY);
                                    sampleSet.applyTo(backGround);
                                    character.move("right");
                                    character.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            showCharacterDetail(message);

                                        }
                                    });
                                    characterUnavailable.setVisibility(View.INVISIBLE);
                                    characterList.removeAllViews();
                                    isCharacterShow = !isCharacterShow;
                                }
                            };
                            CharacterIcon icon = new CharacterIcon(MAINCONTEXT, message, characterList, listener, -1, MainMenuFragment.this);
                            if(message.position != -1) icon.unavailable.setVisibility(View.VISIBLE);
                        }
                    }
                    isCharacterShow = !isCharacterShow;
                }
            });
        }
        characterUnavailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                characterUnavailable.setVisibility(View.INVISIBLE);
                characterList.removeAllViews();
                isCharacterShow = !isCharacterShow;
            }
        });
        teamManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCharacterDetailShow){
                    characterDetailInformation.setVisibility(View.GONE);
                    isCharacterDetailShow = false;
                }
                if(isShadeShow){
                    shadeConstraintLayout.setVisibility(View.INVISIBLE);
                }else{
                    shadeConstraintLayout.setVisibility(View.VISIBLE);
                }
                isShadeShow = !isShadeShow;
            }
        });
        mapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)MAINCONTEXT).setFragment(MAPFRAGMENT);
            }
        });
        menuSourceNumber.setText(""+pref.getInt("4",0));
        menuSourceIngotNumber.setText(""+pref.getInt("ysd",0));
        menuHCYNumber.setText(""+pref.getInt("5",0));
        refreshPlaceInfo();
    }

    public void refreshPlaceInfo(){
        int tempNumber = 0;
        int tempI = -1;
        for(int i = 0; i < placeList.size(); i++){
            if(placeList.get(i).name.equals(MainActivity.teamList.get(chooseTeam).site)){
                tempNumber = placeList.get(i).sceneList.size();
                tempI = i;
            }
        }
        final int tempI2 = tempI;
        placeEventNumberTextView.setText(""+tempNumber);
        placeEventButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.teamList.get(chooseTeam).characterList.size() > 0) {
                    MainActivity.chooseTeam = chooseTeam;
                    placeContentLayout.setVisibility(View.VISIBLE);
                    placeContentLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            placeContentLayout.setVisibility(View.INVISIBLE);
                        }
                    });
                    placeContentRecyclerView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            placeContentLayout.setVisibility(View.INVISIBLE);
                        }
                    });
                    StaggeredGridLayoutManager m = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                    placeContentRecyclerView.setLayoutManager(m);
                    placeContentRecyclerView.setAdapter(new SceneAdapter(placeList.get(tempI2).sceneList, (MainActivity) MAINCONTEXT));
                }
            }
        });
    }

    //更换主页上显示的编队
    private void setTeam(Team team){
        menuTeam.setText(team.teamName);
        //menuFoodNumber.setText(String.valueOf(team.food));
        //menuWineNumber.setText(String.valueOf(team.wine));
        siteText.setText(team.site);
        XYText.setText("X="+team.x+",Y="+team.y);
        for(int i = 0; i < layerList.size(); i++){
            layerList.get(i).removeAllViews();
        }
        for(int i = 0; i < team.characterList.size(); i++){
            team.characterList.get(i).character = null;
        }
        for(CharacterIconMessage icon : team.characterList){
            if(icon.position != -1){
                final CharacterIconMessage message = icon;
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCharacterDetail(message);
                    }
                };
                Character character = new Character(MAINCONTEXT,icon.name,icon.getAbility(HP),icon.getAbility(ATK),true,icon.getAbility(DEF),icon.magicalDefend,icon.weapon,icon.canExplosion,null, listener);
                icon.character = character;
                ConstraintLayout backGround = layerList.get(icon.position/5);
                int id = View.generateViewId();
                character.setId(id);
                backGround.addView(character);
                sampleSet.clone(backGround);
                sampleSet.connect(id,ConstraintSet.LEFT,backGround.getId(),ConstraintSet.LEFT, initX-MainActivity.CharWidth/2 + (icon.position % 5)*intervalX);
                sampleSet.connect(id,ConstraintSet.TOP,backGround.getId(),ConstraintSet.TOP,initY-MainActivity.CharHeight/7*5 + (icon.position/5)*intervalY);
                sampleSet.applyTo(backGround);
                character.move("right");
            }
        }
        refreshPlaceInfo();
    }

    public int getResource(String idName){
        Context ctx = MAINCONTEXT;
        int resId = getResources().getIdentifier(idName, "id", ctx.getPackageName());
        return resId;
    }

    public void showCharacterDetail(CharacterIconMessage m){
        if(!isTeamShow && !isShadeShow && !isCharacterShow){
            isCharacterDetailShow = true;
            characterDetailInformation.setVisibility(View.VISIBLE);
            characterIconImage.setImageResource(getResourceByString(m.name+"icon"));
            characterIconName.setText(m.realName);
            characterIconElite.setImageResource(getResourceByString("elite"+m.elite));
            characterIconLV.setText("LV " + m.level);
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
            stateColumnHp.setText("生命  "+m.getAbility(HP)+"/"+m.getAbility(HP));
            stateColumnAttack.setText("攻击  "+m.getAbility(ATK));
            stateColumnDefend.setText("防御  "+m.getAbility(DEF));
            stateColumnMagicalDefend.setText("法抗  "+m.magicalDefend);
            stateColumnBlock.setText("阻挡  "+m.maxBlock);
            if(m.characterSquare != null && (m.characterSquare.weapon != null || m.characterSquare.armor != null)){
                characterIconWeaponSquare.setVisibility(View.VISIBLE);
                characterIconWeaponSquare.setCharacterSquare(m.characterSquare);
                characterIconWeaponSquare.updateEquipment();
                if(m.characterSquare.weapon != null){
                    characterIconWeaponLayout.setVisibility(View.VISIBLE);
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
                    characterIconWeaponLayout.setVisibility(View.GONE);
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
                    characterIconArmorLayout.setVisibility(View.GONE);
                }
            }else{
                characterIconWeaponSquare.setVisibility(View.INVISIBLE);
                characterIconWeaponLayout.setVisibility(View.GONE);
                characterIconArmorLayout.setVisibility(View.GONE);

            }
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
            }
        }
    }

    private int getResourceByString(String str){
        Resources res = MAINCONTEXT.getResources();
        int resourceId = res.getIdentifier(str,"drawable",MAINCONTEXT.getPackageName());
        return resourceId;
    }


}
