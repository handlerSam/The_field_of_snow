package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.PRINT_SERVICE;
import static com.example.myapplication.MainActivity.PHYSIC;

public class BarFragment extends Fragment {
    public List<String> merchantDialogList = new ArrayList<>();
    Bar bar;
    MainActivity mainActivity;
    LinearLayout barBackButton;
    ImageView merchantImageView;
    TextView merchantNameTextView;
    TextView merchantDialogTextView;
    RecyclerView barRecyclerView;

    public Timer mainTimer;
    public Timer timer;
    public int stringCurrentLength = 1;
    public int currentDialogIndex = -1;
    public LinearLayout barRecruitConfirmLayout;
    public ImageView barRecruitIcon;
    public ImageView barRecruitElite;
    public TextView barRecruitLv;
    public ImageView barRecruitWeapon;
    public TextView barRecruitCancel;
    public TextView barRecruitConfirm;
    public TextView barRecruitShade2;
    public boolean isAnimationEnd = true;
    public SharedPreferences pref;
    public TextView barGreenTicketNumber;
    public TextView barYellowTicketNumber;
    public ImageView barConfirmTicketIcon;
    public TextView barConfirmTicketNumber;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case 1:
                    String str = merchantDialogList.get(currentDialogIndex);
                    merchantDialogTextView.setText(str.substring(0,stringCurrentLength));
                    if(stringCurrentLength < str.length()){
                        stringCurrentLength++;
                    }else{
                        stringCurrentLength = 0;
                        timer.cancel();
                    }
                    break;
                default:
            }
        }
    };
    public BarFragment(Bar bar, MainActivity m){
        this.bar = bar;
        this.mainActivity = m;
        this.pref = m.getSharedPreferences("Item", Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bar_layout,container,false);
        initFind(view);
        initDialog();
        initSet();
        return view;
    }

    public void initFind(View v){
        barBackButton = v.findViewById(R.id.barBackButton);
        merchantImageView = v.findViewById(R.id.merchantImageView);
        merchantNameTextView = v.findViewById(R.id.merchantNameTextView);
        merchantDialogTextView = v.findViewById(R.id.merchantDialogTextView);
        barRecyclerView = v.findViewById(R.id.barRecyclerView);
        barRecruitConfirmLayout = v.findViewById(R.id.barRecruitConfirmLayout);
        barRecruitIcon = v.findViewById(R.id.barRecruitIcon);
        barRecruitElite = v.findViewById(R.id.barRecruitElite);
        barRecruitLv = v.findViewById(R.id.barRecruitLv);
        barRecruitWeapon = v.findViewById(R.id.barRecruitWeapon);
        barRecruitCancel = v.findViewById(R.id.barRecruitCancel);
        barRecruitConfirm = v.findViewById(R.id.barRecruitConfirm);
        barRecruitShade2 = v.findViewById(R.id.barRecruitShade2);
        barGreenTicketNumber = v.findViewById(R.id.barGreenTicketNumber);
        barYellowTicketNumber = v.findViewById(R.id.barYellowTicketNumber);
        barConfirmTicketIcon = v.findViewById(R.id.barConfirmTicketIcon);
        barConfirmTicketNumber = v.findViewById(R.id.barConfirmTicketNumber);
    }

    private void initSet(){
        merchantImageView.setImageResource(bar.merchantImage);
        barRecyclerView.setAdapter(new CharacterRecruitAdapter(bar.recruitList, mainActivity,this));
        LinearLayoutManager manager = new LinearLayoutManager(mainActivity);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        barRecyclerView.setLayoutManager(manager);
        barBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                mainActivity.setFragment(MainActivity.MAINMANU);
            }
        });
        merchantNameTextView.setText(bar.merchantName);
        refreshResourceNumber();

        //商人对话部分
        timer = new Timer();
        mainTimer = new Timer();
        TimerTask mainTimeTask = new TimerTask(){
            @Override
            public void run() {
                stringCurrentLength = 0;
                timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        Message m = new Message();
                        m.what = 1;
                        handler.sendMessage(m);
                    }
                };
                timer.schedule(timerTask,0,50);
                if(currentDialogIndex < (merchantDialogList.size()-1)){
                    currentDialogIndex++;
                }else{
                    currentDialogIndex = 0;
                }
                stringCurrentLength = 1;
            }
        };
        mainTimer.schedule(mainTimeTask,0,5000);

        barRecruitCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barRecruitConfirmLayout.setVisibility(View.INVISIBLE);
                barRecruitShade2.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void initDialog(){
        if(merchantDialogList.size() == 0){
            merchantDialogList.add("欢迎光临 请问要喝点什么？");
            merchantDialogList.add("这两天又有几个新人在找工作...");
            merchantDialogList.add("听说邻村昨天又来那些检察官了...作孽啊...");
        }
    }

    public void setCharacterConfirm(final CharacterIconMessage m, final CharacterRecruitAdapter adapter, final CharacterRecruitAdapter.ViewHolder holder){
        barRecruitConfirmLayout.setVisibility(View.VISIBLE);
        barRecruitShade2.setVisibility(View.VISIBLE);
        barRecruitShade2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        barRecruitIcon.setImageResource(getResource(m.name+"icon"));
        barRecruitElite.setImageResource(getResource("elite"+m.elite));
        barRecruitLv.setText("Lv"+m.level);
        barRecruitWeapon.setImageResource(getResource(""+(m.weapon == PHYSIC? "physic":"magic")+"battleicon"));
        barRecruitConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Team t = MainActivity.teamList.get(MainActivity.chooseTeam);
                boolean affordable = pref.getInt((m.quality > 1? "8":"9"),0) >= (m.quality == 1? 3:1);
                if(t.characterList.size() < 12 && affordable){
                    t.characterList.add(m);
                    barRecruitConfirmLayout.setVisibility(View.INVISIBLE);
                    barRecruitShade2.setVisibility(View.INVISIBLE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt((m.quality > 1? "8":"9"),pref.getInt((m.quality > 1? "8":"9"),0) - (m.quality == 1? 3:1));
                    editor.apply();
                    adapter.setShade(holder,true);
                    refreshResourceNumber();
                    Toast.makeText(mainActivity, "招募成功", Toast.LENGTH_SHORT).show();
                }else if(!affordable){
                    Toast.makeText(mainActivity, "资源不足", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mainActivity, "人员已满", Toast.LENGTH_SHORT).show();
                }
            }
        });
        barConfirmTicketIcon.setImageResource(m.quality > 1? R.drawable.yellow_ticket:R.drawable.green_ticket);
        barConfirmTicketNumber.setText(""+(m.quality == 1? 3:1));
    }

    public int getResource(String idName){
        int resId = mainActivity.getResources().getIdentifier(idName, "drawable", mainActivity.getPackageName());
        return resId;
    }

    public void refreshResourceNumber(){
        barYellowTicketNumber.setText(""+pref.getInt(""+8,0));
        barGreenTicketNumber.setText(""+pref.getInt(""+9,0));
    }
}
