package com.example.myapplication;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Timer;
import java.util.TimerTask;

public class BattleStartFragment extends Fragment {
    MainActivity mainActivity;
    TextView loadingTitle;
    TextView loadingSubtitle;
    TextView loadingHint;
    String mainTitle;
    String subTitle;
    String hint;
    int level;
    public BattleStartFragment(Operation operation){
        mainActivity = (MainActivity) operation.context;
        this.mainTitle = operation.title;
        this.subTitle = operation.subTitle;
        this.hint = operation.hint;
        this.level = operation.level;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.battle_start_fragment,container,false);
        loadingTitle = (TextView) v.findViewById(R.id.loadingTitle);
        loadingSubtitle = (TextView) v.findViewById(R.id.loadingSubtitle);
        loadingHint = (TextView) v.findViewById(R.id.loadingHint);

        loadingTitle.setText(mainTitle);
        loadingSubtitle.setText(subTitle);
        loadingHint.setText(hint);

        final Timer timer = new Timer();
        TimerTask task = new TimerTask(){
            @Override
            public void run() {
                Looper.prepare();
                Message m = new Message();
                m.what = MainActivity.BATTLEFRAGMENT;
                mainActivity.handler.sendMessage(m);
                timer.cancel();
                Log.d("Sam","BattleStart!");
                Looper.loop();
            }
        };
        timer.schedule(task,4000,1);
        return v;
    }


}
