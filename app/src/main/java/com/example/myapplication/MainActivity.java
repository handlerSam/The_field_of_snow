package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.myapplication.CharacterFragment.ATK;
import static com.example.myapplication.CharacterFragment.DEF;
import static com.example.myapplication.CharacterFragment.HP;

public class MainActivity extends AppCompatActivity {

    /*
        原石锭格式：item下ysd
     */
    public static final boolean PHYSIC = true;
    public static final boolean MAGIC = false;

    public static final int KEEPSTANDING = 1;
    public static final int CHARGE = 0;

    public static final int VANGUARD = 0;//先锋
    public static final int GUARD = 1;//近卫
    public static final int DEFENDER = 2;//重装
    public static final int SNIPER = 3;//狙击
    public static final int CASTER = 4;//术士

    int soundEffectNumber = 22;

    public static int voiceArrow;
    public static int voiceBigAxe;
    public static int voiceBtn;
    public static int voiceChixiaobadao;
    public static int voiceConfirm;
    public static int voiceDanger;
    public static int voiceDetailCheck;
    public static int voiceMagic;
    public static int voiceSkillStart;
    public static int voiceSword;
    public static int voiceLose;
    public static int voiceWin;
    public static int voiceExplosion;
    public static int voiceDead;
    public static int voiceSkillReady;
    public static int voiceGetCost;
    public static int voiceCharacterSet;
    public static int voiceBite;
    public static int voiceCommonAttack;
    public static int voiceStar;
    public static int voiceStar2;
    public static int voiceStar3;

    SoundPool soundPool;
    int haveLoadNumber;
    public BackgroundSound backgroundSound;
    public Operation operation;
    public Shop shop;//用于给ShopFragment传参
    public Bar bar;//用于给BarFragment传参
    ArrayList<Dialogue> dialoguesList;//用于给GalFragment传参
    public Context MAINCONTEXT;
    public static int SCREENWIDTH;//较小
    public static int SCREENHEIGHT;//较大
    public static int CharHeight = 284;
    public static int CharWidth = 351;
    public static int CharAttackWidth = CharWidth/13*4;

    public static final int BATTLEFRAGMENT = 1;
    public static final int CHOOSEFRAGMENT = 2;
    public static final int BROWSERFRAGMENT = 3;
    public static final int BATTLESTARTFRAGMENT = 4;
    public static final int BATTLEENDFRAGMENT = 5;
    public static final int MAINMANU = 6;
    public static final int GALFRAGMENT = 7;
    public static final int MAPFRAGMENT = 8;
    public static final int BAGFRAGMENT = 9;
    public static final int SHOPFRAGMENT = 10;
    public static final int BARFRAGMENT = 11;

    public static ArrayList<Skill> skillList = new ArrayList<>();
    public static ArrayList<CharacterIconMessage> mChooseList = new ArrayList<>();
    public static ArrayList<ItemModel> itemModelList = new ArrayList<>();
    public static ArrayList<Place> placeList = new ArrayList<>();
    public static ArrayList<Equipment> weaponList = new ArrayList<>();
    public static ArrayList<Equipment> armorList = new ArrayList<>();
    public static BattleFragment battleFragment;
    public static ArrayList<Team> teamList = new ArrayList<>();
    public static ArrayList<Composition> compositionArrayList = new ArrayList<>();
    public static int chooseTeam = 0;//当从主界面切换到详细BrowseFragment的时候，会先往该变量里传值，再由handler调用

    public static int sourceNumber = 200;
    public static int sourceIngotNumber = 76;
    public static int hcyNumber = 8300;

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case BATTLEFRAGMENT:
                    setFragment(BATTLEFRAGMENT);
                    break;
                case BROWSERFRAGMENT:
                    setFragment(BROWSERFRAGMENT);
                    break;
                case MAINMANU:
                    setFragment(MAINMANU);
                    break;
                case BAGFRAGMENT:
                    setFragment(BAGFRAGMENT);
                    break;
                default:
            }
        }
    };

    private void initSoundAndStart(){
        //effect sound:
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(soundEffectNumber);
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        } else {
            soundPool = new SoundPool(soundEffectNumber, AudioManager.STREAM_MUSIC, 0);
        }
        haveLoadNumber = 0;
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    haveLoadNumber++;
                    if(soundEffectNumber == haveLoadNumber){
                        setFragment(MAINMANU); //初始界面代码
                    }
                }
            }
        });
        voiceArrow = soundPool.load(this, R.raw.arrow, 1);
        voiceBigAxe = soundPool.load(this, R.raw.bigaxe, 1);
        voiceBtn = soundPool.load(this, R.raw.btn, 1);
        voiceChixiaobadao = soundPool.load(this, R.raw.chixiaobadao, 1);
        voiceConfirm = soundPool.load(this, R.raw.confirm, 1);
        voiceDanger = soundPool.load(this, R.raw.danger, 1);
        voiceDetailCheck = soundPool.load(this, R.raw.detailcheck, 1);
        voiceMagic = soundPool.load(this, R.raw.magic, 1);
        voiceSkillStart = soundPool.load(this, R.raw.skillstart, 1);
        voiceSword = soundPool.load(this, R.raw.sword, 1);
        voiceLose = soundPool.load(this, R.raw.lose, 1);
        voiceWin = soundPool.load(this, R.raw.win, 1);
        voiceExplosion = soundPool.load(this,R.raw.explosion,1);
        voiceSkillReady = soundPool.load(this,R.raw.skillready,1);
        voiceDead = soundPool.load(this,R.raw.dead,1);
        voiceGetCost = soundPool.load(this,R.raw.getcost,1);
        voiceCharacterSet = soundPool.load(this,R.raw.characterset,1);
        voiceBite = soundPool.load(this,R.raw.dogbite,1);
        voiceCommonAttack = soundPool.load(this,R.raw.commonattack,1);
        voiceStar = soundPool.load(this,R.raw.star,1);
        voiceStar2 = soundPool.load(this,R.raw.star2,1);
        voiceStar3 = soundPool.load(this,R.raw.star3,1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getScreenInfo();
        initSkill();
        setCompound();
        initItem();
        initPlace();
        initCharacterIconList();
        //initEquipment();
        initSoundAndStart();
    }

    public void setFragment(int fragmentType){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
        switch (fragmentType){
            case BATTLEFRAGMENT:
                battleFragment = null;
                battleFragment = new BattleFragment(operation);
                transaction.replace(R.id.fatherLayout, battleFragment);
                break;
            case CHOOSEFRAGMENT:
                transaction.replace(R.id.fatherLayout,new CharacterFragment(operation, chooseTeam, CharacterFragment.CHOOSE,MainActivity.this));
                break;
            case BROWSERFRAGMENT:
                transaction.replace(R.id.fatherLayout,new CharacterFragment(operation, chooseTeam, CharacterFragment.BROWSER, MainActivity.this));
                break;
            case BATTLESTARTFRAGMENT:
                transaction.replace(R.id.fatherLayout,new BattleStartFragment(operation));
                break;
            case BATTLEENDFRAGMENT:
                transaction.replace(R.id.fatherLayout,new BattleEndFragment(operation));
                break;
            case MAINMANU:
                transaction.replace(R.id.fatherLayout,new MainMenuFragment(MAINCONTEXT));
                break;
            case GALFRAGMENT:
                //调用方法前先往MainActivity的dialoguesList里传参
                transaction.replace(R.id.fatherLayout,new GalFragment(MainActivity.this, dialoguesList));
                break;
            case MAPFRAGMENT:
                transaction.replace(R.id.fatherLayout,new MapFragment(MainActivity.this));
                break;
            case BAGFRAGMENT:
                transaction.replace(R.id.fatherLayout,new BagFragment(MainActivity.this));
                break;
            case SHOPFRAGMENT:
                transaction.replace(R.id.fatherLayout,new ShopFragment(this.shop, this));
                break;
            case BARFRAGMENT:
                transaction.replace(R.id.fatherLayout,new BarFragment(this.bar, this));
                break;
            default:
        }
        transaction.commit();
    }

    private void getScreenInfo(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        MAINCONTEXT = MainActivity.this;
        //获取屏幕宽高
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        SCREENWIDTH = metric.widthPixels;
        SCREENHEIGHT = metric.heightPixels;

        Log.d("Sam","width:"+SCREENWIDTH);
        Log.d("Sam","height:"+SCREENHEIGHT);
    }

    private void initSkill(){
        Skill skill = new Skill("攻击·α");//自动开，数值增加
        skill.autoRelease = true;
        skill.increaseMethod = Skill.AUTO;
        skill.illustration1 = "攻击力+";
        skill.illustration2 = "";
        skill.kForAffect = 10;
        skill.bForAffect = 30;
        skill.kForInitSkillPoint = 10;
        skill.bForInitSkillPoint = 150;
        skill.kForSkillPoint = 0;
        skill.bForSkillPoint = 300;
        skill.kForDuration = 20;
        skill.bForDuration = 300;
        skill.skillImage = MAINCONTEXT.getResources().getDrawable(R.drawable.skillattack1);
        skillList.add(skill);

        skill = new Skill("攻击·β");//手动开，数值增加
        skill.autoRelease = false;
        skill.increaseMethod = Skill.AUTO;
        skill.illustration1 = "攻击力+";
        skill.illustration2 = "";
        skill.kForAffect = 15;
        skill.bForAffect = 50;
        skill.kForInitSkillPoint = 10;
        skill.bForInitSkillPoint = 150;
        skill.kForSkillPoint = 0;
        skill.bForSkillPoint = 300;
        skill.kForDuration = 30;
        skill.bForDuration = 270;
        skill.skillImage = MAINCONTEXT.getResources().getDrawable(R.drawable.skillattack2);
        skillList.add(skill);

        skill = new Skill("攻击·γ");//手动开，百分比增加
        skill.autoRelease = false;
        skill.increaseMethod = Skill.AUTO;
        skill.illustration1 = "攻击力+";
        skill.illustration2 = "%";
        skill.kForAffect = 5;
        skill.bForAffect = 25;
        skill.kForInitSkillPoint = 10;
        skill.bForInitSkillPoint = 150;
        skill.kForSkillPoint = 0;
        skill.bForSkillPoint = 300;
        skill.kForDuration = 40;
        skill.bForDuration = 250;
        skill.skillImage = MAINCONTEXT.getResources().getDrawable(R.drawable.skillattack3);
        skillList.add(skill);

        skill = new Skill("防御·α");//自动开，数值增加
        skill.autoRelease = true;
        skill.increaseMethod = Skill.AUTO;
        skill.illustration1 = "防御+";
        skill.illustration2 = "";
        skill.kForAffect = 10;
        skill.bForAffect = 30;
        skill.kForInitSkillPoint = 10;
        skill.bForInitSkillPoint = 150;
        skill.kForSkillPoint = 0;
        skill.bForSkillPoint = 300;
        skill.kForDuration = 20;
        skill.bForDuration = 300;
        skill.skillImage = MAINCONTEXT.getResources().getDrawable(R.drawable.skilldefend1);
        skillList.add(skill);

        skill = new Skill("防御·β");//手动开，数值增加
        skill.autoRelease = false;
        skill.increaseMethod = Skill.AUTO;
        skill.illustration1 = "防御+";
        skill.illustration2 = "";
        skill.kForAffect = 15;
        skill.bForAffect = 50;
        skill.kForInitSkillPoint = 10;
        skill.bForInitSkillPoint = 150;
        skill.kForSkillPoint = 0;
        skill.bForSkillPoint = 300;
        skill.kForDuration = 30;
        skill.bForDuration = 270;
        skill.skillImage = MAINCONTEXT.getResources().getDrawable(R.drawable.skilldefend2);
        skillList.add(skill);

        skill = new Skill("防御·γ");//手动开，百分比增加
        skill.autoRelease = false;
        skill.increaseMethod = Skill.AUTO;
        skill.illustration1 = "防御+";
        skill.illustration2 = "%";
        skill.kForAffect = 5;
        skill.bForAffect = 25;
        skill.kForInitSkillPoint = 10;
        skill.bForInitSkillPoint = 150;
        skill.kForSkillPoint = 0;
        skill.bForSkillPoint = 300;
        skill.kForDuration = 40;
        skill.bForDuration = 250;
        skill.skillImage = MAINCONTEXT.getResources().getDrawable(R.drawable.skilldefend3);
        skillList.add(skill);

        skill = new Skill("治疗·α");//自动开，逐渐回血
        skill.autoRelease = true;
        skill.increaseMethod = Skill.AUTO;
        skill.illustration1 = "每帧生命+";
        skill.illustration2 = "";
        skill.kForAffect = 1;
        skill.bForAffect = 2;
        skill.kForInitSkillPoint = 10;
        skill.bForInitSkillPoint = 150;
        skill.kForSkillPoint = 0;
        skill.bForSkillPoint = 300;
        skill.kForDuration = 0;
        skill.bForDuration = 30;
        skill.skillImage = MAINCONTEXT.getResources().getDrawable(R.drawable.skillcure1);
        skillList.add(skill);

        skill = new Skill("治疗·β");//手动开，逐渐回血
        skill.autoRelease = false;
        skill.increaseMethod = Skill.AUTO;
        skill.illustration1 = "每帧生命+";
        skill.illustration2 = "";
        skill.kForAffect = 1;
        skill.bForAffect = 2;
        skill.kForInitSkillPoint = 10;
        skill.bForInitSkillPoint = 150;
        skill.kForSkillPoint = 0;
        skill.bForSkillPoint = 300;
        skill.kForDuration = 0;
        skill.bForDuration = 25;
        skill.skillImage = MAINCONTEXT.getResources().getDrawable(R.drawable.skillcure2);
        skillList.add(skill);

        skill = new Skill("治疗·γ");//手动开，直接回血
        skill.autoRelease = false;
        skill.increaseMethod = Skill.AUTO;
        skill.illustration1 = "生命+";
        skill.illustration2 = "";
        skill.kForAffect = 50;
        skill.bForAffect = 250;
        skill.kForInitSkillPoint = 10;
        skill.bForInitSkillPoint = 150;
        skill.kForSkillPoint = 0;
        skill.bForSkillPoint = 300;
        skill.kForDuration = 0;
        skill.bForDuration = 1;
        skill.skillImage = MAINCONTEXT.getResources().getDrawable(R.drawable.skillcure3);
        skillList.add(skill);

        skill = new Skill("奔跑");//手动开
        skill.autoRelease = true;
        skill.increaseMethod = Skill.AUTO;
        skill.illustration1 = "速度+";
        skill.illustration2 = "";
        skill.kForAffect = 1;
        skill.bForAffect = 0;
        skill.kForInitSkillPoint = 10;
        skill.bForInitSkillPoint = 150;
        skill.kForSkillPoint = 0;
        skill.bForSkillPoint = 300;
        skill.kForDuration = 0;
        skill.bForDuration = 100;
        skill.skillImage = MAINCONTEXT.getResources().getDrawable(R.drawable.skillspeed);
        skillList.add(skill);
    }

    private void initCharacterIconList(){
        teamList.clear();
        int temp = 70;
        int temp2 = 1;
        CharacterIconMessage m1 = createCharacter("agz",temp, temp2);
        CharacterIconMessage m2 = createCharacter("sr",temp, temp2);
        CharacterIconMessage m14 =createCharacter("sr",temp, temp2);
        CharacterIconMessage m15 =createCharacter("sy",temp, temp2);
        CharacterIconMessage m3 = createCharacter("sy",temp, temp2);
        CharacterIconMessage m4 = createCharacter("sx2",temp, temp2);
        CharacterIconMessage m5 = createCharacter("yjddw",temp, temp2);
        CharacterIconMessage m6 = createCharacter("xgxd",temp, temp2);
        CharacterIconMessage m16 =createCharacter("xgxd",temp, temp2);
        CharacterIconMessage m11 =createCharacter("xgxd",temp, temp2);
        CharacterIconMessage m7 = createCharacter("xgxdpbz",temp, temp2);
        CharacterIconMessage m8 = createCharacter("xgxdzbr",temp, temp2);
        CharacterIconMessage m9 = createCharacter("xgjjs",temp, temp2);
        CharacterIconMessage m19 =createCharacter("xgjjs",temp, temp2);
        CharacterIconMessage m17 =createCharacter("xgsszz",temp, temp2);
        CharacterIconMessage m18 =createCharacter("xgjjszz",temp, temp2);
        CharacterIconMessage m10 =createCharacter("xgss",temp, temp2);
        CharacterIconMessage m20 =createCharacter("xgss",temp, temp2);
        CharacterIconMessage m12 =createCharacter("sy2",temp, temp2);
        CharacterIconMessage m13 =createCharacter("bbysc",temp, temp2);
        CharacterIconMessage m21 =createCharacter("bbysc2",temp, temp2);
        ArrayList<CharacterIconMessage> addIconList = new ArrayList<>();
        addIconList.add(m1);
        addIconList.add(m5);
        addIconList.add(m13);
        addIconList.add(m4);
        addIconList.add(m10);
        addIconList.add(m9);
        addIconList.add(m8);
        addIconList.add(m2);
        addIconList.add(m7);
        addIconList.add(m3);
        addIconList.add(m6);
        ArrayList<CharacterIconMessage> addIconList2 = new ArrayList<>();
        addIconList2.add(m11);
        addIconList2.add(m12);
        addIconList2.add(m14);
        addIconList2.add(m15);
        addIconList2.add(m16);
        addIconList2.add(m17);
        addIconList2.add(m18);
        addIconList2.add(m19);
        addIconList2.add(m20);
        addIconList2.add(m21);
        Collections.sort(addIconList);
        Team team = new Team(addIconList, 30, 57, placeList.get(0).name, placeList.get(0).coordinary[0], placeList.get(0).coordinary[1],1,"第一编队");
        m1.position = 4;
        m2.position = 9;
        m3.position = 6;
        m4.position = 13;
        m5.position = 18;
        teamList.add(team);

        Team team2 = new Team(addIconList2,60,24,placeList.get(0).name, placeList.get(0).coordinary[0], placeList.get(0).coordinary[1],2,"第二编队");
        m11.position = 0;
        m12.position = 5;
        m15.position = 10;
        m14.position = 15;
        teamList.add(team2);

        ArrayList<CharacterIconMessage> addIconList3 = new ArrayList<>();
        ArrayList<CharacterIconMessage> addIconList4 = new ArrayList<>();
        Team team3 = new Team(addIconList3,60,24,placeList.get(0).name, placeList.get(0).coordinary[0], placeList.get(0).coordinary[1],3,"第三编队");
        Team team4 = new Team(addIconList4,60,24,placeList.get(0).name, placeList.get(0).coordinary[0], placeList.get(0).coordinary[1],4,"第四编队");
        teamList.add(team3);
        teamList.add(team4);
    }

    private void initItem(){
        itemModelList.clear();
        ItemModel model = new ItemModel("基础作战记录",1,R.drawable.item_exp1,0,"记录了作战录像的存储装置，可以些微增加干员的经验值。\n" +
                "在恶劣环境下的每次行动都可能有人会丢掉性命。如果做好了充足的准备，或许也能多拯救一些生命。\n" +
                "存储了数场战斗的录像。\n可以提升干员等级至20。");
        itemModelList.add(model);
        model = new ItemModel("初级作战记录",2,R.drawable.item_exp2,1,"记录了作战录像的存储装置，可以少许增加干员的经验值。\n" +
                "在恶劣环境下的每次行动都可能有人会丢掉性命。如果做好了充足的准备，或许也能多拯救一些生命。\n" +
                "存储了多场战斗的录像与详细数据分析资料。附送了三小时的花絮与访谈。\n可以提升干员等级至40。 ");
        itemModelList.add(model);
        model = new ItemModel("中级作战记录",3,R.drawable.item_exp3,2,"记录了作战录像的存储装置，可以大幅增加干员的经验值。\n" +
                "在恶劣环境下的每次行动都可能有人会丢掉性命。如果做好了充足的准备，或许也能多拯救一些生命。\n" +
                "存储了多个录像集锦，夹带了一张录像人的签名版。\n可以提升干员等级至60。 ");
        itemModelList.add(model);
        model = new ItemModel("高级作战记录",4,R.drawable.item_exp4,3,"记录了作战录像的存储装置，可以极大增加干员的经验值。\n" +
                "在恶劣环境下的每次行动都可能有人会丢掉性命。如果做好了充足的准备，或许也能多拯救一些生命。\n" +
                "追加了总集篇。附带高清版、高清重制版、威力加强版、导演剪辑版、年度黄金版…… \n可以提升干员等级至90。");
        itemModelList.add(model);
        model = new ItemModel("源石",4,R.drawable.item_ys,4,"危险而又必不可少的物质。较为珍贵，用途多样。\n" +
                "在工业中用量巨大的源石结晶，但有着较高的提取难度。是世界的主要能源类精加工产物，同时也是几乎所有源石技艺得以施展的根本。如今，即使“它会传播绝症”的传言甚嚣尘上，也依旧没多少人能抵挡住它的诱惑。 \n");
        itemModelList.add(model);
        model = new ItemModel("合成玉",4,R.drawable.item_hcy2,5,"合成物质。常用于招募干员。\n" +
                "由至纯源石加工而来，混合了其他矿物以后的源石产物。以前仅能当作一些传导元件使用，现在却逐渐成为人事交互关系中令人信服的象征物。");
        itemModelList.add(model);
        model = new ItemModel("龙门币",3,R.drawable.item_lmb,6,"由龙门发行的货币，用途广泛。\n" +
                "经济危机发生后，经济的衰退与政权之间的对立让贸易参与者们举步维艰。龙门币的流通使商业复兴成为可能。 ");
        itemModelList.add(model);
        model = new ItemModel("赤金",3,R.drawable.item_cj,7,"提纯精炼后的金条，可以换取大量龙门币。\n" +
                "需要多少矿石才能提炼出如此贵重的金条呢——之类的想法已经不重要了。现在它正整整齐齐堆在你眼前，这才是关键啊。 ");
        itemModelList.add(model);
        model = new ItemModel("高级凭证",4,R.drawable.item_gjpz,8,"代表干员拥有某方向专业能力的权威性凭证，可用于兑换稀有物资。\n" +
                "感染者隔离法案是现代国家最后的遮羞布。只要你运用些许特殊的技巧，这一切便如薄纸般不堪一击——莱塔尼亚的一位导师曾如此教导她。 ");
        itemModelList.add(model);
        model = new ItemModel("资质凭证",2,R.drawable.item_zzpz,9,"代表干员拥有普通能力的凭证，可用于兑换物资。\n" +
                "当今时代，搜罗人才是一件困难的事；而若要从躲在阴暗处的感染者中寻求可用之才，更无异于大海捞针。如果罗德岛能成为包容所有人的大海，是否就可以省去诸多烦恼呢？ ");
        itemModelList.add(model);
        model = new ItemModel("碳",1,R.drawable.item_t,10,"碳原料，用于基础设施建设。\n" +
                "可生产出种类繁多的聚合物，是工业制造不可缺少的原材料之一。 ");
        itemModelList.add(model);
        model = new ItemModel("碳素",2,R.drawable.item_ts,11,"碳素砖，用于基础设施建设。\n" +
                "轻量，高纯度，具备优质的可加工性，我们如今的辉煌时代正是建立在它们的脊梁上。 ");
        itemModelList.add(model);
        model = new ItemModel("碳素组",3,R.drawable.item_tsz,12,"一组碳素砖，用于基础设施建设。\n" +
                "大量的碳素资源。每一块砖都饱含着燃烧的工业之魂。");
        itemModelList.add(model);
        model = new ItemModel("酯原料",0,R.drawable.item_zyl,13,"工业制造所需的酯原料，可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "近代工业中至关重要的材料之一，许多现代产品的诞生都归功于它的出现。当然了，这只是原料，几乎派不上什么大用场。 ");
        itemModelList.add(model);
        model = new ItemModel("聚酸酯",1,R.drawable.item_jsz,14,"工业制造所需的零散聚酸酯，可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "虽然强度上还略有不足，但已经可以用来制作我们所需要的部分基础物件。同时是一些缓释药物中的常用成分。 ");
        itemModelList.add(model);
        model = new ItemModel("聚酸酯组",2,R.drawable.item_jszz,15,"工业制造所需的一组聚酸酯，可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "进一步加工后的材料，符合通用标准，能够满足市面上绝大多数的需求。可以用于加工一些特殊材料。 ");
        itemModelList.add(model);
        model = new ItemModel("聚酸酯块",3,R.drawable.item_jszk,16,"工业制造所需的成块聚酸酯，可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "精炼提纯后的材料。作为商品，主要面向的客户是对原材料有极高要求的组织和科研机构。也许会成为新一代材料的试金石也说不定哦。 ");
        itemModelList.add(model);
        model = new ItemModel("代糖",0,R.drawable.item_dt,17,"机械化制作的廉价天然糖替代产物，可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "有着微弱的甜味，也许可以吃。同时其溶液也常用于化工用途。 ");
        itemModelList.add(model);
        model = new ItemModel("糖",1,R.drawable.item_t2,18,"机械化制作的少量糖块，可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "使用自然原材制作的稍显贵重的糖料。啊啊，这个味道……会带来不错的好心情。不是用来当零食吃的。");
        itemModelList.add(model);
        model = new ItemModel("糖组",2,R.drawable.item_tz,19,"机械化制作的中等量糖块，可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "饱含能量、广受欢迎的糖块组合包。在被加工成化工材料之前，份量就总是有所减少。流水线上的员工们似乎脱不了干系。 ");
        itemModelList.add(model);
        model = new ItemModel("糖聚块",3,R.drawable.item_tjk,20,"机械化制作的大量糖块，可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "充满诱惑力的精加工糖块堆，通常用于制备药剂。严禁在加工前偷吃！这不是一般的食品，也不会作为食品售卖！不会！ ");
        itemModelList.add(model);
        model = new ItemModel("异铁碎片",0,R.drawable.item_ytsp,21,"普普通通的工业材料。可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "大规模金属原料加工的副产物，因其可塑性强、难以被氧化的特性，常被用于材料金属熔炼与阶段性再处理。 ");
        itemModelList.add(model);
        model = new ItemModel("异铁",1,R.drawable.item_yt,22,"罕见的工业材料。可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "在少数惰化流程中，异铁碎片偶尔会产生相变，聚合为异铁。一般认为达到此量级的异铁产物才是异铁较为稳定的形态。 ");
        itemModelList.add(model);
        model = new ItemModel("异铁组",2,R.drawable.item_ytz,23,"珍贵的工业材料。可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "加工过程中，多块异铁在极少数非人为因素影响下，偶然产生的异铁组合，硬度下降，纯度进一步上升。 ");
        itemModelList.add(model);
        model = new ItemModel("异铁块",3,R.drawable.item_ytk,24,"价值高昂的工业材料。可用于多种强化场合，也常作为制作聚合剂的原料之一。\n" +
                "在极为苛刻的条件下，由多套异铁组熔铸成型的异铁块。已知异铁材料中最为稳定也最为稀少的形式，作为原材料，足以打破现有制品运用领域的疆界。 ");
        itemModelList.add(model);
        model = new ItemModel("双酮",0,R.drawable.item_st,25,"极少量的工业用有机化合物。可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "极少量的非单酮制剂，通过再处理后，工程干员将藉由其发生的化合反应转变过程中的固化现象，来粘结其它稳定结构。 ");
        itemModelList.add(model);
        model = new ItemModel("酮凝集",1,R.drawable.item_tnj,26,"少量的工业用有机化合物。可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "经过特殊处理后产出的少量多态酮制剂，易于闭合的分子构造能将原本繁杂的处理工艺简化为单纯的化学反应，当然，也需要一定的技术支持。");
        itemModelList.add(model);
        model = new ItemModel("酮凝集组",2,R.drawable.item_tnjz,27,"中等量的工业用有机化合物。可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "进一步脱烃处理后得到的中等量酮制剂。制剂接触空气后易与空气中的一些非氧分子发生反应。所以在处理过程中，工程干员理当精准操作，以免浪费材料。 ");
        itemModelList.add(model);
        model = new ItemModel("酮阵列",3,R.drawable.item_tzl,28,"大量的工业用有机化合物。可用于多种强化场合，也常作为制作聚合剂的原料之一。\n" +
                "由工程干员严格监控的大量不稳定实验性酮制剂。是高级的工业材料之一，运用时也请多加小心。 ");
        itemModelList.add(model);
        model = new ItemModel("源岩",0,R.drawable.item_yy,29,"从地表开采出的岩石，可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "富含有机物，常见于源石挥发殆尽后的地区。相对源石来说是较为容易采集的材料。 ");
        itemModelList.add(model);
        model = new ItemModel("固源岩",1,R.drawable.item_gyy,30,"从地表开采出的岩石固块，可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "内含有密集的微孔，常作为源石气体分解物的吸附剂。多用于防护夹层。");
        itemModelList.add(model);
        model = new ItemModel("固源岩组",2,R.drawable.item_gyyz,31,"从地表开采出的岩石固块组，可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "固源岩的压缩定型产物，可自然形成。易碎，随着工业实力的提升，采集到更加完整的固源岩组已不成问题。只是在分类存放时容易和其它用途的石料搞混。");
        itemModelList.add(model);
        model = new ItemModel("提纯源岩",3,R.drawable.item_tcyy,32,"从地表开采出的岩石固块组提纯出的物质，可用于多种强化场合，也常作为制作聚合剂的原料之一。\n" +
                "高度提纯后的源岩展现出了不同于原料的型态，其工艺消耗与提纯成本也急剧上升。无论是谁，看着规则的切割面都会不禁发出感慨吧。这就是工业与自然结合的魅力。 ");
        itemModelList.add(model);
        model = new ItemModel("破损装置",0,R.drawable.item_pszz,33,"收缴来的破损机械装置，可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "一些残破的装置。这些装置曾被拼装在敌人的武器和防具上，经历了激烈的战斗后已经损坏，不过其内部元件依旧具备一定的使用价值。 ");
        itemModelList.add(model);
        model = new ItemModel("装置",1,R.drawable.item_zz,34,"收缴来的常规机械装置，可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "一套相对完整的装置，含有大量密集的可用电子元件。为了迎合轻便实用的设计，主板寸土寸金的空间被塞的满满当当。 \n");
        itemModelList.add(model);
        model = new ItemModel("全新装置",2,R.drawable.item_qxzz,35,"收缴来的全新机械装置，可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "一套全新装配而成的装置，通过重构同类型装置的结构，主板原本一直存在的空间紧张的问题得到了解决，当然启动时的能耗要求也变高了。");
        itemModelList.add(model);
        model = new ItemModel("改量装置",3,R.drawable.item_glzz,36,"收缴来的高等机械装置，可用于多种强化场合，也常作为制作双极纳米片的原料之一。\n" +
                "经过大量私自改造的装置，是极大扩充了容量后的版本，大幅提升性能的同时牺牲了稳定性，从中能感受到制作者所投入的狂热与执着…… ");
        itemModelList.add(model);
        model = new ItemModel("轻锰矿",2,R.drawable.item_qmk,37,"用于提炼冶炼用的金属矿物。可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "用以产出极为泛用的工业催化剂的金属矿物。再加工过程十分复杂，因不规范操作而导致事故的例子比比皆是。 ");
        itemModelList.add(model);
        model = new ItemModel("三水锰矿",3,R.drawable.item_ssmk,38,"用于提炼冶炼用的金属矿物。可用于多种强化场合，也常作为制作D32钢的原料之一。\n" +
                "少有厂家愿意用于生产工业催化剂的珍贵金属矿物。所制催化剂寿命极长，能够多次反复利用并汽提再生，然而复杂的加工工序令多数厂商望而却步。 ");
        itemModelList.add(model);
        model = new ItemModel("RMA70-12",2,R.drawable.item_rma7012,39,"一种敏感且有良好传导效果的矿物。可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "自然态呈复杂多面体的矿物。在被发现其现代工业价值之前，就已经被发现了其在源石技艺施展中的价值。 ");
        itemModelList.add(model);
        model = new ItemModel("RMA70-24",3,R.drawable.item_rma7024,40,"一种十分敏感且有优秀传导效果的矿物。可用于多种强化场合，也常作为制作D32钢的原料之一。\n" +
                "自然态呈复杂多面体的矿物。于1024年被发现，在庞大的工业体系中，展现出了在源石技艺体系内其他矿物不曾具备的巨大价值。");
        itemModelList.add(model);
        model = new ItemModel("研磨石",2,R.drawable.item_yms,41,"用于加工武器零件的研磨石。可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "不起爆、不粉化、不开裂，于零件加工工序中占有重要地位的工具材料。自然属性相当稳定。 \n");
        itemModelList.add(model);
        model = new ItemModel("五水研磨石",3,R.drawable.item_wsyms,42,"用于精加工武器零件的高级研磨石。可用于多种强化场合，也常作为制作D32钢的原料之一。\n" +
                "相较普通研磨石，物质结构更加稳定的工具材料。极难与其它物质发生化学反应。 ");
        itemModelList.add(model);
        model = new ItemModel("凝胶",2,R.drawable.item_nj,43,"一种高强度的可塑性材料。可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "于实验室中意外诞生的人工材料。拥有优良的高低温度耐性，强度高，重量轻，易加工，广泛运用于高新项目。 ");
        itemModelList.add(model);
        model = new ItemModel("聚合凝胶",3,R.drawable.item_jhnj,44,"一种具备极高强度的可塑性材料。可用于多种强化场合。\n" +
                "以凝胶为源材料，历经大量实验与反复测试诞生的人工材料。即使是在高压力环境中也能保持性质的稳定，在少数高新项目中有着重要地位。 ");
        itemModelList.add(model);
        model = new ItemModel("扭转醇",2,R.drawable.item_nzc,45,"片状有机化合物。可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "优秀的化工中介体，常在两种形状间转换以储存和放出传导物质。液化后与酒精在某些性质上十分相似，时常导致整个工作间醉醺醺的。 ");
        itemModelList.add(model);
        model = new ItemModel("白马醇",3,R.drawable.item_bmc,46,"片状有机化合物。可用于多种强化场合，也常作为制作双极纳米片的原料之一。\n" +
                "扭转醇精加工后的产物，名字来自发现其产出方式的厂家。实验结果表明，拥有在非常态环境中向更高级结构转化的趋势。 ");
        itemModelList.add(model);
        model = new ItemModel("炽合金",2,R.drawable.item_chj,47,"电子工业用特殊高熔点合金。可用于多种强化场合，也常作为制造站合成项目的原料。\n" +
                "由数种稀有泰拉金属熔炼成的合金材料。用于制造少数电子元件和电路板，是尖端电子工业中不可或缺的材料。 ");
        itemModelList.add(model);
        model = new ItemModel("炽合金块",3,R.drawable.item_chjk,48,"产量稀少的电子工业用特殊高熔点合金。可用于多种强化场合。\n" +
                "由炽合金进一步加工得到的合金材料。历经复杂的工业处理，保证了一定温度下固液混合态的稳定，在尖端电子工业的产品研发中有着无可替代的作用。 ");
        itemModelList.add(model);
        model = new ItemModel("聚合剂",4,R.drawable.item_jhj,49,"复杂的液态工业产物。用于高级强化场合。\n" +
                "精密穿戴着装中常用的材料。多作为隔绝涂层使用。强大的聚合粘接效果足以阻断源石的挥发。 ");
        itemModelList.add(model);
        model = new ItemModel("双极纳米片",4,R.drawable.item_sjnmp,50,"现代工业的创造力结晶。用于高级强化场合。\n" +
                "对范围内源石敏感的装置。能有效提升源石附近范围内作战武器和设备对源石的敏感性，使源石技艺的存储几近可能。 ");
        itemModelList.add(model);
        model = new ItemModel("D32钢",4,R.drawable.item_d32g,51,"自然界中不应存在的人造金属材料，呈固态。用于高级强化场合。\n" +
                "强度超群，无法击穿，顺畅传导源石技艺，将重新订立武器材料的标准。 ");
        itemModelList.add(model);

        for(int i = 0; i < compositionArrayList.size(); i++){
            itemModelList.get(compositionArrayList.get(i).composition.get(0)).synthesisCompositionPosition.add(i);
        }

        //调试用
        SharedPreferences.Editor editor = getSharedPreferences("Item",MODE_PRIVATE).edit();
        for(int i = 0; i < itemModelList.size(); i++){
            editor.putInt(""+i, i < 4? 1000:50);
        }
        editor.putInt("ysd",500);
        editor.putInt("5",5000);
        editor.putInt("8",5);
        editor.putInt("9",5);
        editor.apply();
    }

    /*private void initEquipment(){
        Square square = new Square(3,2);
        square.setSquare(2,1).setSquare(2,0).setSquare(0,0);
        Equipment weapon = new Equipment("大枪",square,true,3);
        weapon.setIllustration("攻击力+37%");
        weaponList.add(weapon);

        square = new Square(1,2);
        square.setSquare(0,0).setSquare(0,1);
        weapon = new Equipment("复仇者",square,true,0);
        weapon.setIllustration("攻击力+15%");
        weaponList.add(weapon);

        square = new Square(2,1);
        square.setSquare(0,0).setSquare(1,0);
        Equipment armor = new Equipment("盾",square,false,1);
        armor.setIllustration("防御力+25%");
        armorList.add(armor);

        square = new Square(3,1);
        square.setSquare(0,0).setSquare(1,0).setSquare(2,0);
        armor = new Equipment("铳盾",square,false,2);
        armor.setIllustration("攻击力+10%，防御力+22%");
        armorList.add(armor);

        square = new Square(4,3);
        square.setSquare(0,0).setSquare(1,0).setSquare(2,0).setSquare(1,1).setSquare(2,1).setSquare(3,1).setSquare(3,2).setSquare(2,2);
        CharacterSquare characterSquare = new CharacterSquare(square,addIconList.get(0));
        addIconList.get(0).characterSquare = characterSquare;

        square = new Square(3,1);
        square.setSquare(0,0).setSquare(1,0).setSquare(2,0);
        characterSquare = new CharacterSquare(square,MainActivity.addIconList.get(1));
        addIconList.get(1).characterSquare = characterSquare;
    }*/
    /*
    public static String[] PLACENAME= {"斯大林格勒","伏尔加德勒","切尔诺伯格","圣彼得堡"};
    public static int[][] PLACECOORDINARY = {{8,6},{13,3},{9,1},{9,20}};
    public static String[] PLACECOLOR = {"red", "purple", "yellow", "green"};
     */
    public void initPlace(){
        Village village = new Village("哈巴斯克村",10,19,"yellow",0,5,"乌萨斯在远东地区最大的移动城市。由于位于于炎国的交界处，历史上曾经于炎国有过纷争。");
        List<ShopItem> shopItemList = new ArrayList<>();
        shopItemList.add(new ShopItem(itemModelList.get(0),20, 1, ShopItem.YSD));
        shopItemList.add(new ShopItem(itemModelList.get(1),20, 100, ShopItem.HCY));
        shopItemList.add(new ShopItem(itemModelList.get(2),20, 1, ShopItem.YS));
        shopItemList.add(new ShopItem(itemModelList.get(3),20, 2, ShopItem.YS));
        shopItemList.add(new ShopItem(itemModelList.get(4),7, 200, ShopItem.HCY));
        shopItemList.add(new ShopItem(itemModelList.get(5),50, 1, ShopItem.YSD));
        village.sceneList.add(new Shop(getResource("interaction_1"),"杂货铺","Grocery",2,shopItemList,R.drawable.npc_003,"杂货铺店主 诺埃尔"));
        List<ShopItem> shopItemList2 = new ArrayList<>();
        shopItemList2.add(new ShopItem(itemModelList.get(15),30, 5, ShopItem.YSD));
        shopItemList2.add(new ShopItem(itemModelList.get(19),30, 50, ShopItem.HCY));
        shopItemList2.add(new ShopItem(itemModelList.get(23),30, 1, ShopItem.YS));
        shopItemList2.add(new ShopItem(itemModelList.get(27),30, 1, ShopItem.YS));
        shopItemList2.add(new ShopItem(itemModelList.get(31),30, 50, ShopItem.HCY));
        shopItemList2.add(new ShopItem(itemModelList.get(35),30, 5, ShopItem.YSD));
        village.sceneList.add(new Shop(getResource("interaction_7"),"商店","Shop",3,shopItemList2,R.drawable.npc_005,"店主 阿托斯"));
        ItemOperation itemOperation = new ItemOperation(MainActivity.this);
        ItemDropRate rate;
        for(int i = 30; i < 52; i++){
            rate = new ItemDropRate(50,20, MainActivity.itemModelList.get(i));
            itemOperation.addItemDropRate(rate);
        }
        Operation op = new Operation("所有远程攻击的干员和敌人攻击范围都是3路。","乌萨斯雪原","维季姆高原",1,12,itemOperation,MainActivity.this);
        village.sceneList.add(new Battle(getResource("interaction_4"),"作战 Lv10","Herd",1,op));

        itemOperation = new ItemOperation(MAINCONTEXT);
        for(int i = 14; i < 30; i++){
            rate = new ItemDropRate(50,20, MainActivity.itemModelList.get(i));
            itemOperation.addItemDropRate(rate);
        }
        op = new Operation("爆炸生物在死亡时会对周围敌人造成十倍的伤害。","乌萨斯雪原","阿尔丹山区",2,12,itemOperation,MAINCONTEXT);
        village.sceneList.add(new Battle(getResource("interaction_11"),"作战 精英1 Lv20","Mercenary",2,op));

        List<ShopItem> shopItemList3 = new ArrayList<>();
        shopItemList3.add(new ShopItem(itemModelList.get(39),30, 5, ShopItem.YSD));
        shopItemList3.add(new ShopItem(itemModelList.get(43),30, 50, ShopItem.HCY));
        shopItemList3.add(new ShopItem(itemModelList.get(44),30, 1, ShopItem.YS));
        shopItemList3.add(new ShopItem(itemModelList.get(36),30, 1, ShopItem.YS));
        shopItemList3.add(new ShopItem(itemModelList.get(41),30, 50, ShopItem.HCY));
        shopItemList3.add(new ShopItem(itemModelList.get(28),30, 5, ShopItem.YSD));
        village.sceneList.add(new Shop(getResource("interaction_7"),"商店","Shop",1,shopItemList3,R.drawable.npc_004,"老板娘 安迪尔"));
        itemOperation = new ItemOperation(MAINCONTEXT);
        for(int i = 0; i < 5; i++){
            rate = new ItemDropRate(50,20, MainActivity.itemModelList.get(i));
            itemOperation.addItemDropRate(rate);
        }
        op = new Operation("战斗开始时，往往先部署费用较低且速度较快的单位。","冲突","丘利曼镇",3,12,itemOperation,MAINCONTEXT);
        village.sceneList.add(new Battle(getResource("interaction_6"),"作战 精英2 Lv40","Conflict",3,op));

        List<CharacterIconMessage> messageList = new ArrayList<>();
        messageList.add(createCharacter(getBarRecruitName(1),1,0));
        messageList.add(createCharacter(getBarRecruitName(1),1,0));
        messageList.add(createCharacter(getBarRecruitName(1),1,0));
        village.sceneList.add(new Bar(getResource("interaction_13"),"酒馆","Bar",1,messageList,R.drawable.npc_001,"酒馆老板 杰克"));

        List<CharacterIconMessage> messageList2 = new ArrayList<>();
        messageList2.add(createCharacter(getBarRecruitName(2),1,0));
        messageList2.add(createCharacter(getBarRecruitName(2),1,0));
        messageList2.add(createCharacter(getBarRecruitName(2),1,0));
        village.sceneList.add(new Bar(getResource("interaction_16"),"高级酒馆","Bar",2,messageList2,R.drawable.npc_002,"酒馆老板 维列奇"));


        /*Dialogue dialogue = new Dialogue("话说，hyz好强啊。","sgy");
                dialogue.setCharacterImage(0,R.drawable.agz_drawing);
                dialogue.textSpeed = GalFragment.TEXTSLOW;
                dialogue.imageSpeed = GalFragment.IMAGEVERYSLOW;
                Dialogue dialogue1 = new Dialogue("这还用你说？我老早就知道了。","wzy");
                dialogue1.setCharacterImageNull(0);
                dialogue1.textSpeed = GalFragment.TEXTSLOW;
                dialogue1.imageSpeed = GalFragment.IMAGEVERYSLOW;
                dialogue1.setMusic(R.raw.qiechengintro, R.raw.qiechengloop);
                dialogue1.setBackgroundChange(R.drawable.test_background);
                Dialogue dialogue2 = new Dialogue("确实啊，为什么我还要再重复一遍呢。","sgy");
                dialogue2.setCharacterImage(2,R.drawable.sx2_serious);
                dialogue2.setMusic(R.raw.game02intro, R.raw.game02loop);
                Dialogue dialogue3 = new Dialogue("可能有人想水点字数用于测试吧。","wzy");
                dialogue3.setCharacterImageNull(2);
                dialogue3.setBackgroundNull();
                ArrayList<Dialogue> dialoguesList = new ArrayList<>();
                dialoguesList.add(dialogue);
                dialoguesList.add(dialogue1);
                dialoguesList.add(dialogue2);
                dialoguesList.add(dialogue3);*/
        Dialogue dialogue;
        ArrayList<Dialogue> dialoguesList = new ArrayList<>();
        dialogue = new Dialogue("...","");
        dialogue.setBackgroundChange(R.drawable.interaction_11);
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("一个清冷的早晨。阴天。","");
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("乌萨斯东部，一座荒凉的矿场。","");
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("矿场空地前的一个高台上，一个穿着皮大衣的军官叫着：","");
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("\"24，36，18，52...\"","");
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("叶莲娜低头看了一眼自己手上的号码牌。18。","");
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("丝毫不意外——六年前是父母，一年前是祖母，现在轮到自己了。","");
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("矿场前的空地上的人一年比一年少，现在站着的这些人平均年龄可能还不到20岁。","");
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("叶莲娜深吸了一口气。","");
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("接下来自己要做的事情，很简单，很早之前就和同伴们商量过——","");
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("用自己尚且稚嫩的法术，做最后一搏。","");
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("在行刑的时候，距离四个士官不到一米，这个范围内自己有信心爆发出具有杀伤力的法术。","");
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("这似乎是自己这条命唯一的价值。","");
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("但这显然不应该是这条命的价值。","");
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("为什么我会这么窝囊的死去？","");
        dialogue.setBackgroundNull();
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("....","");
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("...","");
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("..","");
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("接下来的事情很简单，","");
        dialogue.setBackgroundChange(R.drawable.interaction_11);
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("叶莲娜爆发出的法术远比她自己想象的要强大，","");
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("四个行刑的士官一瞬间就被冻成了冰块。","");
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("同伴们惊呼着，其他士官气急败坏的扑上来按住了她，","");
        dialogue.setBackgroundNull();
        dialogue.imageSpeed = GalFragment.IMAGEQUICK;
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("正当叶莲娜打算闭上眼接受自己的命运的时候，","");
        dialogue.setBackgroundChange(R.drawable.background1);
        dialoguesList.add(dialogue);
        dialogue = new Dialogue("那个黑塔一般的怪物带着他的士兵来到了这里。","");
        dialoguesList.add(dialogue);
        village.sceneList.add(new Plot(getResource("interaction_11"),"往事","Plot",1,dialoguesList));
        placeList.add(village);

        village = new Village("符斯托克村",23,20,"green",3,3,"沿海的一个小村落，居民以捕鱼为主。");
        //List<ShopItem> shopItemList2 = new ArrayList<>();
        //shopItemList2.add(new ShopItem(itemModelList.get(15),30, 5, ShopItem.YSD));
        //shopItemList2.add(new ShopItem(itemModelList.get(19),30, 50, ShopItem.HCY));
        //shopItemList2.add(new ShopItem(itemModelList.get(23),30, 1, ShopItem.YS));
        //shopItemList2.add(new ShopItem(itemModelList.get(27),30, 1, ShopItem.YS));
        //shopItemList2.add(new ShopItem(itemModelList.get(31),30, 50, ShopItem.HCY));
        //shopItemList2.add(new ShopItem(itemModelList.get(35),30, 5, ShopItem.YSD));
        //village.sceneList.add(new Shop(getResource("interaction_7"),"商店","Shop",3,shopItemList2,R.drawable.npc_005,"店主 阿托斯"));
        placeList.add(village);


        BattleField battleField = new BattleField("维季姆高原",10,12,"red", "Lv10","此处埋藏有丰富的铀矿，但在天灾的影响下铀矿变得异常具有感染性，并诞生了众多的感染生物。请进入此处的人员小心应对。\n 该地区掉落材料。");
        //ItemOperation itemOperation = new ItemOperation(MainActivity.this);
        //ItemDropRate rate;
        //for(int i = 30; i < 52; i++){
        //    rate = new ItemDropRate(50,20, MainActivity.itemModelList.get(i));
        //    itemOperation.addItemDropRate(rate);
        //}
        //Operation op = new Operation("所有远程攻击的干员和敌人攻击范围都是3路。","乌萨斯雪原","维季姆高原",1,12,itemOperation,MainActivity.this);
        //battleField.sceneList.add(new Battle(getResource("interaction_4"),"作战","Herd",1,op));
        placeList.add(battleField);


        battleField = new BattleField("阿尔丹山区",5,16,"red", "精英1 Lv20","该山区地势易守难攻，常年活跃着多只萨卡兹雇佣军小队和强盗团伙，打劫路过的商队。\n 该地区掉落材料。");
        //itemOperation = new ItemOperation(MAINCONTEXT);
        //for(int i = 14; i < 30; i++){
        //    rate = new ItemDropRate(50,20, MainActivity.itemModelList.get(i));
        //    itemOperation.addItemDropRate(rate);
        //}
        //op = new Operation("爆炸生物在死亡时会对周围敌人造成十倍的伤害。","乌萨斯雪原","阿尔丹山区",2,12,itemOperation,MAINCONTEXT);
        //battleField.sceneList.add(new Battle(getResource("interaction_11"),"作战","Mercenary",2,op));
        placeList.add(battleField);


        village = new Village("丘利曼镇",16,8,"green",5,1,"乌萨斯远东地区少有的对感染者包容的聚落。由于位置偏僻，气候寒冷，乌萨斯军的巡逻并没有经过这片区域。");
        //List<ShopItem> shopItemList3 = new ArrayList<>();
        //shopItemList3.add(new ShopItem(itemModelList.get(39),30, 5, ShopItem.YSD));
        //shopItemList3.add(new ShopItem(itemModelList.get(43),30, 50, ShopItem.HCY));
        //shopItemList3.add(new ShopItem(itemModelList.get(44),30, 1, ShopItem.YS));
        //shopItemList3.add(new ShopItem(itemModelList.get(36),30, 1, ShopItem.YS));
        //shopItemList3.add(new ShopItem(itemModelList.get(41),30, 50, ShopItem.HCY));
        //shopItemList3.add(new ShopItem(itemModelList.get(28),30, 5, ShopItem.YSD));
        //village.sceneList.add(new Shop(getResource("interaction_7"),"商店","Shop",1,shopItemList3,R.drawable.npc_004,"老板娘 安迪尔"));
        placeList.add(village);

        battleField = new BattleField("黎曼山区",24,9,"red", "精英2 Lv40","该山区地势易守难攻，常年活跃着多只萨卡兹雇佣军小队和强盗团伙，打劫路过的商队。\n 该地区掉落作战记录和原石。");
        //itemOperation = new ItemOperation(MAINCONTEXT);
        //for(int i = 0; i < 5; i++){
        //    rate = new ItemDropRate(50,20, MainActivity.itemModelList.get(i));
        //    itemOperation.addItemDropRate(rate);
        //}
        //op = new Operation("战斗开始时，往往先部署费用较低且速度较快的单位。","冲突","丘利曼镇",3,12,itemOperation,MAINCONTEXT);
        //battleField.sceneList.add(new Battle(getResource("interaction_6"),"作战","Conflict",3,op));
        placeList.add(battleField);
    }

    public void playSound(int voiceId,float leftSound, float rightSound){
        soundPool.play(voiceId, leftSound, rightSound, 1, 0, 1);
    }

    @Override
    protected void onDestroy() {
        if(backgroundSound != null){
            backgroundSound.destroy();
        }
        if(battleFragment != null && battleFragment.handler != null){
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    public CharacterIconMessage createCharacter(String name, int level, int elite){
        CharacterIconMessage m = null;
        int atk;
        int hp;
        int def;
        float atkAdd;
        float hpAdd;
        float defAdd;
        CharacterSkill s = null;
        int rnd;
        switch(name){
            case "sy":
                rnd = ((int)getRandom2(0,3));
                if(rnd == 0){
                    s = new CharacterSkill(skillList.get(9),1, 9,this);//奔跑
                }else if(rnd == 1){
                    s = new CharacterSkill(skillList.get(0),1, 0,this);//攻击a
                }
                hp = (int)getRandom(693,758);
                atk = (int)getRandom(170,185);
                def = (int)getRandom(136,137);
                atkAdd = getAbilityAmplification("atk",VANGUARD);
                hpAdd = getAbilityAmplification("hp",VANGUARD);
                defAdd = getAbilityAmplification("def",VANGUARD);
                m = new CharacterIconMessage(name,"霜牙","O4",1,0,4,0,5,PHYSIC,20,3,1,hp,atk,CHARGE,0,def,0,false,true,s, hpAdd,defAdd,atkAdd,VANGUARD,VANGUARD,VANGUARD);
                break;
            case "sr":
                rnd = ((int)getRandom2(0,3));
                if(rnd == 0){
                    s = new CharacterSkill(skillList.get(9),1, 9,this);//奔跑
                }else if(rnd == 1){
                    s = new CharacterSkill(skillList.get(0),5, 0,this);//攻击a
                }
                hp = (int)getRandom(727,812);
                atk = (int)getRandom(183,203);
                def = (int)getRandom(139,147);
                atkAdd = getAbilityAmplification("atk",VANGUARD);
                hpAdd = getAbilityAmplification("hp",VANGUARD);
                defAdd = getAbilityAmplification("def",VANGUARD);
                m = new CharacterIconMessage(name,"霜锐","O5",1,1,4,0,8,PHYSIC,30,3,1,hp,atk,CHARGE,0,def,0,false,true,s, hpAdd,defAdd,atkAdd,VANGUARD,VANGUARD,VANGUARD);
                break;
            case "yjdlq":
                rnd = ((int)getRandom2(0,3));
                if(rnd == 0){
                    s = new CharacterSkill(skillList.get(9),1, 9,this);//奔跑
                }else if(rnd == 1){
                    s = new CharacterSkill(skillList.get(2),3, 2,this);//攻击c
                }
                hp = (int)getRandom(911,975);
                atk = (int)getRandom(212,230);
                def = (int)getRandom(154,163);
                atkAdd = getAbilityAmplification("atk",VANGUARD);
                hpAdd = getAbilityAmplification("hp",VANGUARD);
                defAdd = getAbilityAmplification("def",VANGUARD);
                m = new CharacterIconMessage(name,"游击队猎犬","SI1",1,0,4,0,10,PHYSIC,30,3,1,hp,atk,CHARGE,0,def,5,false,true,s, hpAdd,defAdd,atkAdd,VANGUARD,VANGUARD,VANGUARD);
                break;
            case "bbysc":
                rnd = ((int)getRandom2(0,2));
                if(rnd == 0){
                    s = new CharacterSkill(skillList.get(9),1, 9,this);//奔跑
                }
                hp = (int)getRandom(533,618);
                atk = (int)getRandom(170,185);
                def = (int)getRandom(76,87);
                atkAdd = getAbilityAmplification("atk",VANGUARD);
                hpAdd = getAbilityAmplification("hp",VANGUARD);
                defAdd = getAbilityAmplification("def",VANGUARD);
                m = new CharacterIconMessage(name,"冰爆源石虫","B9",1,0,4,0,10,PHYSIC,80,1,2,hp,atk,CHARGE,0,def,0,true,true,s, hpAdd,defAdd,atkAdd,VANGUARD,VANGUARD,VANGUARD);
                break;
            case "xgxd":
                rnd = ((int)getRandom2(0,4));
                if(rnd == 0){
                    s = new CharacterSkill(skillList.get(0),7, 0,this);//攻击a
                }else if(rnd == 1){
                    s = new CharacterSkill(skillList.get(3),7, 3,this);//防御a
                }else if(rnd == 2){
                    s = new CharacterSkill(skillList.get(6),7, 6,this);//治疗a
                }
                hp = (int)getRandom(949,990);
                atk = (int)getRandom(233,272);
                def = (int)getRandom(142,154);
                atkAdd = getAbilityAmplification("atk",GUARD);
                hpAdd = getAbilityAmplification("hp",GUARD );
                defAdd = getAbilityAmplification("def",VANGUARD);
                m = new CharacterIconMessage(name,"雪怪小队","Y1",1,0,4,0,11,PHYSIC,40,2,2,hp,atk,CHARGE,0,def,0,false,true,s, hpAdd,defAdd,atkAdd,VANGUARD,GUARD,VANGUARD);
                break;
            case "bbysc2":
                rnd = ((int)getRandom2(0,2));
                if(rnd == 0){
                    s = new CharacterSkill(skillList.get(9),1, 9,this);//奔跑
                }
                hp = (int)getRandom(533,618);
                atk = (int)getRandom(183,203);
                def = (int)getRandom(89,117);
                atkAdd = getAbilityAmplification("atk",VANGUARD);
                hpAdd = getAbilityAmplification("hp",VANGUARD);
                defAdd = getAbilityAmplification("def",VANGUARD);
                m = new CharacterIconMessage(name,"冰爆源石虫·α","B10",1,1,4,0,14,PHYSIC,80,2,2,hp,atk,CHARGE,0,def,0,true,true,s, hpAdd,defAdd,atkAdd,VANGUARD,VANGUARD,VANGUARD);
                break;
            case "sy2":
                s = new CharacterSkill(skillList.get(2),3, 2,this);//攻击c
                hp = (int)getRandom(949,949);
                atk = (int)getRandom(272,272);
                def = (int)getRandom(154,154);
                atkAdd = getAbilityAmplification("atk",VANGUARD);
                hpAdd = getAbilityAmplification("hp",GUARD);
                defAdd = getAbilityAmplification("def",GUARD);
                m = new CharacterIconMessage(name,"霜叶","FL",1,2,4,0,15,MAGIC,60,2,2,hp,atk,CHARGE,100,def,10,false,true,s, hpAdd,defAdd,atkAdd,GUARD,VANGUARD,GUARD);
                break;
            case "xgjjs":
                rnd = ((int)getRandom2(0,4));
                if(rnd == 0){
                    s = new CharacterSkill(skillList.get(0),7, 0,this);//攻击a
                }else if(rnd == 1){
                    s = new CharacterSkill(skillList.get(1),7, 1,this);//攻击b
                }else if(rnd == 2){
                    s = new CharacterSkill(skillList.get(2),3, 2,this);//攻击c
                }
                hp = (int)getRandom(550,604);
                atk = (int)getRandom(163,166);
                def = (int)getRandom(54,57);
                atkAdd = getAbilityAmplification("atk",SNIPER);
                hpAdd = getAbilityAmplification("hp",SNIPER);
                defAdd = getAbilityAmplification("def",SNIPER);
                m = new CharacterIconMessage(name,"雪怪狙击手","Y2",1,0,4,0,14,PHYSIC,50,1,1,hp,atk,CHARGE,400,def,0,false,true,s, hpAdd,defAdd,atkAdd,SNIPER,SNIPER,SNIPER);
                break;
            case "xgss":
                rnd = ((int)getRandom2(0,4));
                if(rnd == 0){
                    s = new CharacterSkill(skillList.get(0),7, 0,this);//攻击a
                }else if(rnd == 1){
                    s = new CharacterSkill(skillList.get(1),7, 1,this);//攻击b
                }else if(rnd == 2){
                    s = new CharacterSkill(skillList.get(2),3, 2,this);//攻击c
                }
                hp = (int)getRandom(589,619);
                atk = (int)getRandom(145,155);
                def = (int)getRandom(42,47);
                atkAdd = getAbilityAmplification("atk",CASTER);
                hpAdd = getAbilityAmplification("hp",CASTER);
                defAdd = getAbilityAmplification("def",CASTER);
                m = new CharacterIconMessage(name,"雪怪术士","Y4",1,0,4,0,22,MAGIC,50,1,1,hp,atk,CHARGE,400,def,20,false,true,s, hpAdd,defAdd,atkAdd,CASTER,CASTER,CASTER);
                break;
            case "xgxdzbr":
                rnd = ((int)getRandom2(0,4));
                if(rnd == 0){
                    s = new CharacterSkill(skillList.get(1),7, 1,this);//攻击b
                }else if(rnd == 1){
                    s = new CharacterSkill(skillList.get(4),7, 4,this);//防御b
                }else if(rnd == 2){
                    s = new CharacterSkill(skillList.get(7),7, 7,this);//治疗b
                }
                hp = (int)getRandom(1038,1138);
                atk = (int)getRandom(754,778);
                def = (int)getRandom(241,249);
                atkAdd = getAbilityAmplification("atk",GUARD);
                hpAdd = getAbilityAmplification("hp",DEFENDER);
                defAdd = getAbilityAmplification("def",GUARD);
                m = new CharacterIconMessage(name,"雪怪小队凿冰人","Y6",1,1,4,0,23,PHYSIC,50,1,1,hp,atk,CHARGE,0,def,10,false,true,s, hpAdd,defAdd,atkAdd,DEFENDER,GUARD,GUARD);
                break;
            case "xgjjszz":
                rnd = ((int)getRandom2(0,4));
                if(rnd == 0){
                    s = new CharacterSkill(skillList.get(0),9, 0,this);//攻击a
                }else if(rnd == 1){
                    s = new CharacterSkill(skillList.get(1),9, 1,this);//攻击b
                }else if(rnd == 2){
                    s = new CharacterSkill(skillList.get(2),5, 2,this);//攻击c
                }
                hp = (int)getRandom(667,693);
                atk = (int)getRandom(171,177);
                def = (int)getRandom(53,58);
                atkAdd = getAbilityAmplification("atk",SNIPER);
                hpAdd = getAbilityAmplification("hp",SNIPER);
                defAdd = getAbilityAmplification("def",SNIPER);
                m = new CharacterIconMessage(name,"雪怪狙击手组长","Y3",1,1,4,0,16,PHYSIC,60,1,1,hp,atk,CHARGE,400,def,0,false,true,s, hpAdd,defAdd,atkAdd,SNIPER,SNIPER,SNIPER);
                break;
            case "yjddw":
                rnd = ((int)getRandom2(0,3));
                if(rnd == 0){
                    s = new CharacterSkill(skillList.get(3),9, 3,this);//防御a
                }else if(rnd == 1){
                    s = new CharacterSkill(skillList.get(4),9, 4,this);//防御b
                }else if(rnd == 2){
                    s = new CharacterSkill(skillList.get(5),5, 5,this);//防御c
                }
                hp = (int)getRandom(1539,1602);
                atk = (int)getRandom(192,198);
                def = (int)getRandom(254,257);
                atkAdd = getAbilityAmplification("atk",DEFENDER);
                hpAdd = getAbilityAmplification("hp",DEFENDER);
                defAdd = getAbilityAmplification("def",DEFENDER);
                m = new CharacterIconMessage(name,"游击队盾卫","SP",1,2,4,0,24,PHYSIC,50,1,4,hp,atk,CHARGE,0,def,80,false,true,s, hpAdd,defAdd,atkAdd,DEFENDER,DEFENDER,DEFENDER);
                break;
            case "xgsszz":
                rnd = ((int)getRandom2(0,4));
                if(rnd == 0){
                    s = new CharacterSkill(skillList.get(0),9, 0,this);//攻击a
                }else if(rnd == 1){
                    s = new CharacterSkill(skillList.get(1),9, 1,this);//攻击b
                }else if(rnd == 2){
                    s = new CharacterSkill(skillList.get(2),5, 2,this);//攻击c
                }
                hp = (int)getRandom(619,699);
                atk = (int)getRandom(171,189);
                def = (int)getRandom(47,48);
                atkAdd = getAbilityAmplification("atk",CASTER);
                hpAdd = getAbilityAmplification("hp",CASTER);
                defAdd = getAbilityAmplification("def",CASTER);
                m = new CharacterIconMessage(name,"雪怪术士组长","Y5",1,1,4,0,26,MAGIC,60,1,1,hp,atk,CHARGE,400,def,25,false,true,s, hpAdd,defAdd,atkAdd,CASTER,CASTER,CASTER);
                break;
            case "xgxdpbz":
                rnd = ((int)getRandom2(0,3));
                if(rnd == 0){
                    s = new CharacterSkill(skillList.get(1),11, 1,this);//攻击b
                }else if(rnd == 1){
                    s = new CharacterSkill(skillList.get(4),11, 4,this);//防御b
                }else if(rnd == 2){
                    s = new CharacterSkill(skillList.get(7),11, 7,this);//治疗b
                }
                hp = (int)getRandom(1238,1338);
                atk = (int)getRandom(814,838);
                def = (int)getRandom(257,267);
                atkAdd = getAbilityAmplification("atk",GUARD);
                hpAdd = getAbilityAmplification("hp",DEFENDER);
                defAdd = getAbilityAmplification("def",GUARD);
                m = new CharacterIconMessage(name,"雪怪小队破冰者","Y7",1,2,4,0,26,PHYSIC,60,1,1,hp,atk,CHARGE,0,def,15,false,true,s, hpAdd,defAdd,atkAdd,DEFENDER,GUARD,GUARD);
                break;
            case "yjdjjs":
                rnd = ((int)getRandom2(0,3));
                if(rnd == 0){
                    s = new CharacterSkill(skillList.get(0),9, 0,this);//攻击a
                }else if(rnd == 1){
                    s = new CharacterSkill(skillList.get(1),9, 1,this);//攻击b
                }else if(rnd == 2){
                    s = new CharacterSkill(skillList.get(2),5, 2,this);//攻击c
                }
                hp = (int)getRandom(711,787);
                atk = (int)getRandom(253,282);
                def = (int)getRandom(57,62);
                atkAdd = getAbilityAmplification("atk",SNIPER);
                hpAdd = getAbilityAmplification("hp",SNIPER);
                defAdd = getAbilityAmplification("def",SNIPER);
                m = new CharacterIconMessage(name,"游击队狙击手","SI5",1,1,4,0,26,PHYSIC,50,1,1,hp,atk,CHARGE,400,def,0,false,true,s, hpAdd,defAdd,atkAdd,SNIPER,SNIPER,SNIPER);
                break;
            case "yjdskzzzzz":
                rnd = ((int)getRandom2(0,3));
                if(rnd == 0){
                    s = new CharacterSkill(skillList.get(2),5, 2,this);//攻击c
                }else if(rnd == 1){
                    s = new CharacterSkill(skillList.get(5),5, 5,this);//防御c
                }else if(rnd == 2){
                    s = new CharacterSkill(skillList.get(8),5, 8,this);//治疗c
                }
                hp = (int)getRandom(1236,1253);
                atk = (int)getRandom(322,335);
                def = (int)getRandom(108,121);
                atkAdd = getAbilityAmplification("atk",GUARD);
                hpAdd = getAbilityAmplification("hp",DEFENDER);
                defAdd = getAbilityAmplification("def",GUARD);
                m = new CharacterIconMessage(name,"游击队萨卡兹战士组长","SP7",1,1,4,0,28,PHYSIC,60,1,3,hp,atk,CHARGE,0,def,20,false,true,s, hpAdd,defAdd,atkAdd,DEFENDER,GUARD,GUARD);
                break;
            case "sx2":
                s = new CharacterSkill(skillList.get(2),7, 2,this);//攻击c
                hp = (int)getRandom(732,732);
                atk = (int)getRandom(362,362);
                def = (int)getRandom(48,48);
                atkAdd = getAbilityAmplification("atk",SNIPER);
                hpAdd = getAbilityAmplification("hp",GUARD);
                defAdd = getAbilityAmplification("def",GUARD);
                m = new CharacterIconMessage(name,"霜星","FN",1,2,4,0,48,MAGIC,120,1,1,hp,atk,CHARGE,400,def,50,false,true,s, hpAdd,defAdd,atkAdd,GUARD,SNIPER,GUARD);
                break;
            case "agz":
                s = new CharacterSkill(skillList.get(2),7, 2,this);//攻击c
                hp = (int)getRandom(1602,1602);
                atk = (int)getRandom(415,415);
                def = (int)getRandom(279,279);
                atkAdd = getAbilityAmplification("atk",GUARD);
                hpAdd = getAbilityAmplification("hp",DEFENDER);
                defAdd = getAbilityAmplification("def",DEFENDER);
                m = new CharacterIconMessage(name,"爱国者","PT",1,2,4,0,52,PHYSIC,120,1,5,hp,atk,CHARGE,0,def,40,false,true,s, hpAdd,defAdd,atkAdd,DEFENDER,GUARD,DEFENDER);
                break;
            default:
        }
        m.attack *= 1.1f;
        m.physicalDefend *= 0.8f;
        m.level = Math.min(40 + 10*(elite + m.quality), level);
        m.elite = elite;
        m.maxLevel = 40 + (m.elite + m.quality)*10;
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

    private float getRandom(float min, float max){
        int temp = (int)((Math.random()*(max*10- min*10)+1) + min*10);
        return 0.1f*temp;
    }

    private int getRandom2(int min, int max){
        int temp = (int)((Math.random()*(max-min+1)) + min);
        return temp;
    }

    private float getAbilityAmplification(String ability, int occupation){
        //记得修改的时候也要去CharacterRecruitAdapter里修改对应的等第判定
        switch(occupation){
            case VANGUARD:
                switch(ability){
                    case "hp":
                        return getRandom(6.5f,7.5f);
                    case "atk":
                        return getRandom(1.7f,2.7f);
                    case "def":
                        return getRandom(1.0f,1.8f);
                    default:
                }
                break;
            case GUARD:
                switch(ability){
                    case "hp":
                        return getRandom(8.6f,10.8f);
                    case "atk":
                        return getRandom(2.7f,3.8f);
                    case "def":
                        return getRandom(0.8f,1.0f);
                    default:
                }
                break;
            case DEFENDER:
                switch(ability){
                    case "hp":
                        return getRandom(12.7f,15.9f);
                    case "atk":
                        return getRandom(1.2f,1.8f);
                    case "def":
                        return getRandom(2f,2.3f);
                    default:
                }
                break;
            case SNIPER:
                switch(ability){
                    case "hp":
                        return getRandom(5.4f,6.5f);
                    case "atk":
                        return getRandom(2.6f,3.1f);
                    case "def":
                        return getRandom(0.6f,0.9f);
                    default:
                }
                break;
            case CASTER:
                switch(ability){
                    case "hp":
                        return getRandom(5.4f,6.5f);
                    case "atk":
                        return getRandom(1.9f,2.7f);
                    case "def":
                        return getRandom(0.6f,0.9f);
                    default:
                }
                break;
            default:
        }
        return -1;
    }

    public int getResource(String idName){
        int resId = getResources().getIdentifier(idName, "drawable", getPackageName());
        return resId;
    }

    public String getBarRecruitName(int mode){
        String[] s = new String[]{};
        switch(mode){
            case 1:
                s = new String[]{"xgxd", "xgjjs", "xgxd", "xgss", "xgxd"};
                break;
            case 2:
                s = new String[]{"xgjjszz","xgxdzbr","xgsszz","xgxdpbz","xgxdzbr"};
                break;
            default:
        }
        int temp = getRandom2(0,s.length-1);
        return s[temp];
    }

    public void setCompound(){
        if(compositionArrayList.size() == 0){
            ArrayList<Integer> models;
            ArrayList<Integer> integers;
            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(51);
            integers.add(1);
            models.add(38);
            integers.add(1);
            models.add(42);
            integers.add(1);
            models.add(40);
            integers.add(1);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(50);
            integers.add(1);
            models.add(36);
            integers.add(1);
            models.add(46);
            integers.add(2);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(49);
            integers.add(1);
            models.add(32);
            integers.add(1);
            models.add(24);
            integers.add(1);
            models.add(28);
            integers.add(1);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(40);
            integers.add(1);
            models.add(39);
            integers.add(1);
            models.add(31);
            integers.add(2);
            models.add(27);
            integers.add(1);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(42);
            integers.add(1);
            models.add(41);
            integers.add(1);
            models.add(23);
            integers.add(1);
            models.add(35);
            integers.add(1);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(38);
            integers.add(1);
            models.add(37);
            integers.add(2);
            models.add(15);
            integers.add(1);
            models.add(45);
            integers.add(1);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(46);
            integers.add(1);
            models.add(45);
            integers.add(1);
            models.add(19);
            integers.add(1);
            models.add(39);
            integers.add(1);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(34);
            integers.add(1);
            models.add(33);
            integers.add(3);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(35);
            integers.add(1);
            models.add(34);
            integers.add(4);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(36);
            integers.add(1);
            models.add(35);
            integers.add(1);
            models.add(31);
            integers.add(2);
            models.add(41);
            integers.add(1);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(26);
            integers.add(1);
            models.add(25);
            integers.add(3);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(27);
            integers.add(1);
            models.add(26);
            integers.add(4);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(28);
            integers.add(1);
            models.add(27);
            integers.add(2);
            models.add(19);
            integers.add(1);
            models.add(37);
            integers.add(1);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(22);
            integers.add(1);
            models.add(21);
            integers.add(3);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(23);
            integers.add(1);
            models.add(22);
            integers.add(4);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(24);
            integers.add(1);
            models.add(23);
            integers.add(2);
            models.add(35);
            integers.add(1);
            models.add(15);
            integers.add(1);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(14);
            integers.add(1);
            models.add(13);
            integers.add(3);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(15);
            integers.add(1);
            models.add(14);
            integers.add(4);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(16);
            integers.add(1);
            models.add(15);
            integers.add(2);
            models.add(26);
            integers.add(1);
            models.add(45);
            integers.add(1);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(18);
            integers.add(1);
            models.add(17);
            integers.add(3);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(19);
            integers.add(1);
            models.add(18);
            integers.add(4);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(20);
            integers.add(1);
            models.add(19);
            integers.add(2);
            models.add(23);
            integers.add(1);
            models.add(37);
            integers.add(1);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(30);
            integers.add(1);
            models.add(29);
            integers.add(3);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(31);
            integers.add(1);
            models.add(30);
            integers.add(5);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(32);
            integers.add(1);
            models.add(31);
            integers.add(4);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(1);
            integers.add(1);
            models.add(0);
            integers.add(5);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(2);
            integers.add(1);
            models.add(1);
            integers.add(4);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(3);
            integers.add(1);
            models.add(2);
            integers.add(3);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(0);
            integers.add(5);
            models.add(1);
            integers.add(1);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(1);
            integers.add(4);
            models.add(2);
            integers.add(1);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(2);
            integers.add(3);
            models.add(3);
            integers.add(1);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(11);
            integers.add(1);
            models.add(10);
            integers.add(3);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(12);
            integers.add(1);
            models.add(11);
            integers.add(3);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(10);
            integers.add(3);
            models.add(11);
            integers.add(1);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(11);
            integers.add(3);
            models.add(12);
            integers.add(1);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(48);
            integers.add(1);
            models.add(35);
            integers.add(1);
            models.add(41);
            integers.add(1);
            models.add(47);
            integers.add(1);
            compositionArrayList.add(new Composition(models,integers));

            models = new ArrayList<>();
            integers = new ArrayList<>();
            models.add(44);
            integers.add(1);
            models.add(23);
            integers.add(1);
            models.add(43);
            integers.add(1);
            models.add(47);
            integers.add(1);
            compositionArrayList.add(new Composition(models,integers));

        }
    }

}

class Skill{
    public static final int AUTO = 1;
    public static final int ATTACK = 2;
    public static final int DEFEND = 3;
    public static final int MOVING = 4;

    String name;
    int skillPoint;
    int initSkillPoint;
    int duration;
    boolean autoRelease;
    int increaseMethod;
    String illustration1;
    String illustration2;
    int affect;
    int kForAffect;
    int bForAffect;
    int kForInitSkillPoint;//点数显示的时候全部/10
    int bForInitSkillPoint;
    int kForSkillPoint;
    int bForSkillPoint;
    int kForDuration;
    int bForDuration;
    Drawable skillImage;

    public Skill(String name){
        this.name = name;
    }
    public void initSkill(int lv){
        getAffect(lv);
        getInitSkillPoint(lv);
        getSkillPoint(lv);
        getDuration(lv);
    }
    public void getAffect(int lv){
        affect = lv * kForAffect + bForAffect;
    }
    public void getInitSkillPoint(int lv){
        initSkillPoint = lv * kForInitSkillPoint + bForInitSkillPoint;
    }
    public void getSkillPoint(int lv){
        skillPoint = lv * kForSkillPoint + bForSkillPoint;
    }
    public void getDuration(int lv){
        duration = lv * kForDuration + bForDuration;
    }
    public String getIllustration(){
        return illustration1+this.affect+illustration2;
    }
}
class ItemOperation{
    private ArrayList<ItemDropRate> itemDropList = new ArrayList<>();
    private ArrayList<Item> outPutItemList = new ArrayList<>();
    Context context;
    //在创建了对象之后需要尽快把对象填入itemDropList
    public ItemOperation(Context context){
        this.context = context;
    }

    public ItemOperation addItemDropRate(ItemDropRate rate){
        itemDropList.add(rate);
        return this;
    }

    public ArrayList<Item> getOutcome(){
        for(ItemDropRate rate: itemDropList){
            int number = 0;
            for(int i = 0; i < rate.number; i++){
                if(judgePossibility(rate.rate)) number++;
            }
            if(number > 0){
                Item item = new Item(context,rate.item,number);
                outPutItemList.add(item);
            }
        }
        return outPutItemList;
    }

    private boolean judgePossibility(int possibility){
        int temp = ((int)((Math.random()*100)+1));
        return temp <= possibility;
    }
}
class ItemDropRate{
    int rate;//记录该关卡获得单个物品的概率
    int number;//记录该关卡能获得几个这种物品
    ItemModel item;
    public ItemDropRate(int rate, int number, ItemModel itemModel){
        this.rate = rate;
        item = itemModel;
        this.number = number;
    }
}
class ItemModel{
    String ChineseName;//中文名字
    int quality;//0最差- 4最好
    int imageId;//记录物品图片id
    int id;//记录处于ItemModelList哪个位置
    String illustration;
    ArrayList<Integer> synthesisCompositionPosition = new ArrayList<>();
    public ItemModel(String ChineseName, int quality, int imageId, int id){
        this.ChineseName = ChineseName;
        this.quality = quality;
        this.imageId = imageId;
        this.id = id;
    }
    public ItemModel(String ChineseName, int quality, int imageId, int id, String illustration){
        this.ChineseName = ChineseName;
        this.quality = quality;
        this.imageId = imageId;
        this.id = id;
        this.illustration = illustration;
    }
}

class Operation{
    String title;
    String subTitle;
    String hint;
    int maxNumber;
    int level;
    int star;
    Context context;
    ItemOperation operation;
    public Operation(String hint, String title, String subTitle, int level, int maxNumber, ItemOperation operation, Context context){
        this.hint = hint;
        this.title = title;
        this.level = level;
        this.subTitle = subTitle;
        this.maxNumber = maxNumber;
        this.operation = operation;
        this.context = context;
    }
}
class Team{
    ArrayList<CharacterIconMessage> characterList;
    int food;
    int wine;
    String site;
    int x;
    int y;
    int teamNumber;
    String teamName;
    public Team(ArrayList<CharacterIconMessage> characterList, int food, int wine, String site, int x, int y, int teamNumber, String teamName){
        this.characterList = characterList;
        this.food = food;
        this.wine = wine;
        this.site = site;
        this.teamName = teamName;
        this.x = x;
        this.y = y;
        this.teamNumber = teamNumber;
    }
}

class Place{
    String name;
    String illustration;
    int[] coordinary = new int[2];
    String color;
    List<Scene> sceneList = new ArrayList<>();
}

class Village extends Place{
    int friendship;
    int villageLevel;
    public Village(String name, int X, int Y, String color, int friendship, int villageLevel, String illustration){
        super.name = name;
        super.illustration = illustration;
        super.coordinary[0] = X;
        super.coordinary[1] = Y;
        super.color = color;
        this.friendship = friendship;
        this.villageLevel = villageLevel;
    }
}

class BattleField extends Place{
    String averageLevel;
    public BattleField(String name, int X, int Y, String color, String averageLevel, String illustration){
        super.name = name;
        super.illustration = illustration;
        super.coordinary[0] = X;
        super.coordinary[1] = Y;
        super.color = color;
        this.averageLevel = averageLevel;
    }
}

class Scene{
    int resourceImage;
    String ChineseName;
    String EnglishName;
    int level;
}

class Shop extends Scene{
    int backgroundImage;
    int merchantImage;
    List<ShopItem> shopItemList;
    String merchantName;
    public Shop(int resourceImage, String ChineseName, String EnglishName, int level, List<ShopItem> shopItemList, int merchantImage, String merchantName){
        super.resourceImage = resourceImage;
        super.ChineseName = ChineseName;
        super.EnglishName = EnglishName;
        super.level = level;
        this.shopItemList = shopItemList;
        this.merchantImage = merchantImage;
        this.merchantName = merchantName;
    }
}

class Bar extends Scene{
    int backgroundImage;
    int merchantImage;
    List<CharacterIconMessage> recruitList;
    String merchantName;
    public Bar(int resourceImage, String ChineseName, String EnglishName, int level, List<CharacterIconMessage> recruitList, int merchantImage, String merchantName){
        super.resourceImage = resourceImage;
        super.ChineseName = ChineseName;
        super.EnglishName = EnglishName;
        super.level = level;
        this.recruitList = recruitList;
        this.merchantImage = merchantImage;
        this.merchantName = merchantName;
    }
}

class Plot extends Scene{
    ArrayList<Dialogue> dialogueArrayList;
    public Plot(int resourceImage, String ChineseName, String EnglishName, int level, ArrayList<Dialogue> dialogueArrayList){
        super.resourceImage = resourceImage;
        super.ChineseName = ChineseName;
        super.EnglishName = EnglishName;
        super.level = level;
        this.dialogueArrayList = dialogueArrayList;
    }
}

class Battle extends Scene{
    Operation operation;
    public Battle(int resourceImage, String ChineseName, String EnglishName, int level, Operation op){
        super.resourceImage = resourceImage;
        super.ChineseName = ChineseName;
        super.EnglishName = EnglishName;
        super.level = level;
        this.operation = op;
    }
}

class ShopItem{
    public static final int HCY = 0;
    public static final int YSD = 1;
    public static final int YS = 2;
    ItemModel itemModel;
    int itemNumber;
    int itemPrice;
    int currency;
    public ShopItem(ItemModel itemModel, int itemNumber, int itemPrice, int currency){
        this.itemNumber = itemNumber;
        this.itemModel = itemModel;
        this.itemPrice = itemPrice;
        this.currency = currency;
    }
}

class Composition{
    public ArrayList<Integer> composition;
    public ArrayList<Integer> materialNumber;
    public Composition(ArrayList<Integer> composition, ArrayList<Integer> materialNumber){
        this.composition = composition;
        this.materialNumber = materialNumber;
    }
}