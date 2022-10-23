package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ShopFragment extends Fragment {
    public Shop shop;
    public RecyclerView shopRecyclerView;
    public LinearLayout shopBackButton;
    public MainActivity m;
    public TextView merchantDialogTextView;
    public TextView merchantNameTextView;
    public LinearLayout itemDetailLayout;
    public TextView itemDetailNameTextView;
    public TextView itemDetailNumberInBagTextView;
    public ImageView itemDetailImageView;
    public TextView itemDetailIllustrationTextView;
    public LinearLayout itemDetailMin;
    public LinearLayout itemDetailReduce;
    public TextView itemDetailNumberText;
    public LinearLayout itemDetailAdd;
    public LinearLayout itemDetailMax;
    public TextView itemDetailPromotionCancel;
    public TextView itemDetailPromotionConfirm;
    public TextView shopSourceNumber;
    public TextView shopSourceIngotNumber;
    public TextView shopHCYNumber;
    public TextView itemDetailPriceTextView;
    public ImageView itemDetailCurrencyIconImageView;
    public TextView itemDetailLeftTextView;
    public ImageView merchantImageView;

    public List<String> merchantDialogList = new ArrayList<>();
    public Timer mainTimer;
    public Timer timer;
    public int stringCurrentLength = 1;
    public int currentDialogIndex = -1;
    public boolean isAnimationEnd = true;
    public SharedPreferences pref;
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

    public ShopFragment(Shop shop, MainActivity m){
        this.shop = shop;
        this.m = m;
        pref = m.getSharedPreferences("Item", Context.MODE_PRIVATE);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shop_layout,container,false);
        initFind(view);
        initDialog();
        initSet();
        return view;
    }

    public void initFind(View v){
        this.shopRecyclerView = (RecyclerView) v.findViewById(R.id.shopRecyclerView);
        shopBackButton = (LinearLayout) v.findViewById(R.id.shopBackButton);
        merchantDialogTextView = (TextView) v.findViewById(R.id.merchantDialogTextView);
        merchantNameTextView = (TextView) v.findViewById(R.id.merchantNameTextView);
        itemDetailLayout = (LinearLayout) v.findViewById(R.id.itemDetailLayout);
        itemDetailNameTextView = (TextView) v.findViewById(R.id.itemDetailNameTextView);
        itemDetailNumberInBagTextView = (TextView) v.findViewById(R.id.itemDetailNumberInBagTextView);
        itemDetailImageView = (ImageView) v.findViewById(R.id.itemDetailImageView);
        itemDetailIllustrationTextView = (TextView) v.findViewById(R.id.itemDetailIllustrationTextView);
        itemDetailMin = (LinearLayout) v.findViewById(R.id.itemDetailMin);
        itemDetailReduce = (LinearLayout) v.findViewById(R.id.itemDetailReduce);
        itemDetailNumberText = (TextView) v.findViewById(R.id.itemDetailNumberText);
        itemDetailAdd = (LinearLayout) v.findViewById(R.id.itemDetailAdd);
        itemDetailMax = (LinearLayout) v.findViewById(R.id.itemDetailMax);
        itemDetailPromotionCancel = (TextView) v.findViewById(R.id.itemDetailPromotionCancel);
        itemDetailPromotionConfirm = (TextView) v.findViewById(R.id.itemDetailPromotionConfirm);
        shopSourceNumber = (TextView) v.findViewById(R.id.shopSourceNumber);
        shopSourceIngotNumber = (TextView) v.findViewById(R.id.shopSourceIngotNumber);
        shopHCYNumber = (TextView) v.findViewById(R.id.shopHCYNumber);
        itemDetailPriceTextView = (TextView) v.findViewById(R.id.itemDetailPriceTextView);
        itemDetailCurrencyIconImageView = (ImageView) v.findViewById(R.id.itemDetailCurrencyIconImageView);
        itemDetailLeftTextView = (TextView) v.findViewById(R.id.itemDetailLeftTextView);
        merchantImageView = (ImageView) v.findViewById(R.id.merchantImageView);
    }
    public void initSet(){
        refreshResource();
        merchantImageView.setImageResource(shop.merchantImage);
        shopRecyclerView.setAdapter(new ShopItemAdapter(shop.shopItemList, this));
        shopRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        shopBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                m.setFragment(MainActivity.MAINMANU);
            }
        });
        merchantNameTextView.setText(shop.merchantName);
        itemDetailPromotionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shopRecyclerView.getVisibility() == View.INVISIBLE){
                    showPurchaseLayout(false);
                }
            }
        });
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
    }
    public void initDialog(){
        if(merchantDialogList.size() == 0){
            merchantDialogList.add("欢迎光临~ 请问要买点什么？");
            merchantDialogList.add("唉 今年的收成着实不太好...");
            merchantDialogList.add("听说邻村昨天又来那些检察官了...作孽啊...");
        }
    }

    public void refreshResource(){
        shopSourceNumber.setText(""+pref.getInt("4",0));
        shopSourceIngotNumber.setText(""+pref.getInt("ysd",0));
        shopHCYNumber.setText(""+pref.getInt("5",0));
    }

    public void showPurchaseLayout(final boolean isToShow){
        if(isAnimationEnd){
            isAnimationEnd = false;
            itemDetailLayout.setVisibility(isToShow? View.VISIBLE:View.INVISIBLE);
            shopRecyclerView.setVisibility(isToShow? View.INVISIBLE:View.VISIBLE);
            Animation loadAnimation = AnimationUtils.loadAnimation(getActivity(),
                    isToShow? R.anim.fade_in_fast: R.anim.fade_out_fast);
            Animation loadAnimation2 = AnimationUtils.loadAnimation(getActivity(),
                    isToShow? R.anim.fade_out_fast: R.anim.fade_in_fast);
            loadAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    isAnimationEnd = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            itemDetailLayout.startAnimation(loadAnimation);
            shopRecyclerView.startAnimation(loadAnimation2);
        }
    }
}
