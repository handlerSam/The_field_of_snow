package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

public class BagFragment extends Fragment {
    RecyclerView bagRecyclerView;
    LinearLayout backButton;
    MainActivity m;
    LinearLayout itemIllustrationLayout;
    TextView itemIllustrationNameTextView;
    TextView itemIllustrationNumberTextview;
    ImageView itemIllustrationItemImageView;
    TextView itemIllustrationIllustrationTextView;
    TextView characterIconShade5;
    boolean isAnimationEnd = true;
    boolean isFirstComposition = true;

    LinearLayout synthesisLayout;
    ConstraintLayout synthesisLayout1;
    ImageView bagSynthesis1MainItem;
    TextView bagSynthesis1MainItemNumber;
    TextView bagSynthesis1MainItemName;
    TextView bagSynthesis1Item1Red;
    ImageView bagSynthesis1Item1;
    TextView bagSynthesis1Item1Number;
    TextView bagSynthesis1Item2Red;
    ImageView bagSynthesis1Item2;
    TextView bagSynthesis1Item2Number;
    TextView bagSynthesis1Item3Red;
    ImageView bagSynthesis1Item3;
    TextView bagSynthesis1Item3Number;
    ConstraintLayout synthesisLayout2;
    ImageView bagSynthesis2MainItem;
    TextView bagSynthesis2MainItemNumber;
    TextView bagSynthesis2MainItemName;
    TextView bagSynthesis2Item1Red;
    ImageView bagSynthesis2Item1;
    TextView bagSynthesis2Item1Number;
    TextView bagSynthesis2Item2Red;
    ImageView bagSynthesis2Item2;
    TextView bagSynthesis2Item2Number;
    TextView bagSynthesis2Item3Red;
    ImageView bagSynthesis2Item3;
    TextView bagSynthesis2Item3Number;
    TextView synthesisAdd;
    TextView synthesisMax;
    TextView synthesisNumber;
    TextView synthesisReduce;
    TextView synthesisMin;
    TextView synthesisButton;
    ImageView synthesisLayout1State;
    ImageView synthesisLayout2State;
    LinearLayout itemSynthesisButton;
    TextView characterIconShade6;
    TextView synthesisCompositionChoose;

    ArrayList<ImageView> bagSynthesis1Item = new ArrayList<>();
    ArrayList<TextView> bagSynthesis1Number = new ArrayList<>();
    ArrayList<TextView> bagSynthesis1Red = new ArrayList<>();
    ArrayList<ImageView> bagSynthesis2Item = new ArrayList<>();
    ArrayList<TextView> bagSynthesis2Number = new ArrayList<>();
    ArrayList<TextView> bagSynthesis2Red = new ArrayList<>();
    SharedPreferences pref;
    int compositionNumber = 0;

    ItemAdapter itemAdapter;
    public BagFragment(MainActivity m){
        this.m = m;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bag_layout,container,false);
        initFind(v);
        initSet();
        return v;
    }

    public void initFind(View v){
        bagRecyclerView = (RecyclerView) v.findViewById(R.id.bagRecyclerView);
        backButton = (LinearLayout) v.findViewById(R.id.backButton2);
        itemIllustrationLayout = (LinearLayout) v.findViewById(R.id.itemIllustrationLayout);
        itemIllustrationNameTextView = (TextView) v.findViewById(R.id.itemIllustrationNameTextView);
        itemIllustrationNumberTextview = (TextView) v.findViewById(R.id.itemIllustrationNumberTextview);
        itemIllustrationItemImageView = (ImageView) v.findViewById(R.id.itemIllustrationItemImageView);
        itemIllustrationIllustrationTextView = (TextView) v.findViewById(R.id.itemIllustrationIllustrationTextView);
        characterIconShade5 = (TextView) v.findViewById(R.id.characterIconShade5);
        synthesisLayout = v.findViewById(R.id.synthesisLayout);
        synthesisLayout1 = v.findViewById(R.id.synthesisLayout1);
        bagSynthesis1MainItem = v.findViewById(R.id.bagSynthesis1MainItem);
        bagSynthesis1MainItemNumber = v.findViewById(R.id.bagSynthesis1MainItemNumber);
        bagSynthesis1MainItemName = v.findViewById(R.id.bagSynthesis1MainItemName);

        bagSynthesis1Item1Red = v.findViewById(R.id.bagSynthesis1Item1Red);
        bagSynthesis1Item1 = v.findViewById(R.id.bagSynthesis1Item1);
        bagSynthesis1Item1Number = v.findViewById(R.id.bagSynthesis1Item1Number);
        bagSynthesis1Red.add(bagSynthesis1Item1Red);
        bagSynthesis1Item.add(bagSynthesis1Item1);
        bagSynthesis1Number.add(bagSynthesis1Item1Number);

        bagSynthesis1Item2Red = v.findViewById(R.id.bagSynthesis1Item2Red);
        bagSynthesis1Item2 = v.findViewById(R.id.bagSynthesis1Item2);
        bagSynthesis1Item2Number = v.findViewById(R.id.bagSynthesis1Item2Number);
        bagSynthesis1Red.add(bagSynthesis1Item2Red);
        bagSynthesis1Item.add(bagSynthesis1Item2);
        bagSynthesis1Number.add(bagSynthesis1Item2Number);

        bagSynthesis1Item3Red = v.findViewById(R.id.bagSynthesis1Item3Red);
        bagSynthesis1Item3 = v.findViewById(R.id.bagSynthesis1Item3);
        bagSynthesis1Item3Number = v.findViewById(R.id.bagSynthesis1Item3Number);
        bagSynthesis1Red.add(bagSynthesis1Item3Red);
        bagSynthesis1Item.add(bagSynthesis1Item3);
        bagSynthesis1Number.add(bagSynthesis1Item3Number);

        synthesisLayout2 = v.findViewById(R.id.synthesisLayout2);
        bagSynthesis2MainItem = v.findViewById(R.id.bagSynthesis2MainItem);
        bagSynthesis2MainItemNumber = v.findViewById(R.id.bagSynthesis2MainItemNumber);
        bagSynthesis2MainItemName = v.findViewById(R.id.bagSynthesis2MainItemName);
        bagSynthesis2Item1Red = v.findViewById(R.id.bagSynthesis2Item1Red);
        bagSynthesis2Item1 = v.findViewById(R.id.bagSynthesis2Item1);
        bagSynthesis2Item1Number = v.findViewById(R.id.bagSynthesis2Item1Number);
        bagSynthesis2Red.add(bagSynthesis2Item1Red);
        bagSynthesis2Item.add(bagSynthesis2Item1);
        bagSynthesis2Number.add(bagSynthesis2Item1Number);

        bagSynthesis2Item2Red = v.findViewById(R.id.bagSynthesis2Item2Red);
        bagSynthesis2Item2 = v.findViewById(R.id.bagSynthesis2Item2);
        bagSynthesis2Item2Number = v.findViewById(R.id.bagSynthesis2Item2Number);
        bagSynthesis2Red.add(bagSynthesis2Item2Red);
        bagSynthesis2Item.add(bagSynthesis2Item2);
        bagSynthesis2Number.add(bagSynthesis2Item2Number);

        bagSynthesis2Item3Red = v.findViewById(R.id.bagSynthesis2Item3Red);
        bagSynthesis2Item3 = v.findViewById(R.id.bagSynthesis2Item3);
        bagSynthesis2Item3Number = v.findViewById(R.id.bagSynthesis2Item3Number);
        bagSynthesis2Red.add(bagSynthesis2Item3Red);
        bagSynthesis2Item.add(bagSynthesis2Item3);
        bagSynthesis2Number.add(bagSynthesis2Item3Number);

        synthesisAdd = v.findViewById(R.id.synthesisAdd);
        synthesisMax = v.findViewById(R.id.synthesisMax);
        synthesisNumber = v.findViewById(R.id.synthesisNumber);
        synthesisReduce = v.findViewById(R.id.synthesisReduce);
        synthesisMin = v.findViewById(R.id.synthesisMin);
        synthesisButton = v.findViewById(R.id.synthesisButton);
        synthesisLayout1State = v.findViewById(R.id.synthesisLayout1State);
        synthesisLayout2State = v.findViewById(R.id.synthesisLayout2State);
        itemSynthesisButton = v.findViewById(R.id.itemSynthesisButton);
        characterIconShade6 = v.findViewById(R.id.characterIconShade6);
        synthesisCompositionChoose = v.findViewById(R.id.synthesisCompositionChoose);
    }

    public void initSet(){
        pref = m.getSharedPreferences("Item", Context.MODE_PRIVATE);
        itemAdapter = new ItemAdapter(MainActivity.itemModelList, m, this);
        bagRecyclerView.setAdapter(itemAdapter);
        StaggeredGridLayoutManager m1 = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
        bagRecyclerView.setLayoutManager(m1);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.setFragment(MainActivity.MAINMANU);
            }
        });
        characterIconShade5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(characterIconShade5.getVisibility() == View.VISIBLE){
                    showDetail(false);
                }
            }
        });
        characterIconShade6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(characterIconShade6.getVisibility() == View.VISIBLE){
                    showSynthesisDetail(false);
                }
            }
        });
    }

    public void showDetail(final boolean isToShow){
        if(isAnimationEnd){
            isAnimationEnd = false;
            itemIllustrationLayout.setVisibility(isToShow? View.VISIBLE:View.INVISIBLE);
            characterIconShade5.setVisibility(isToShow? View.VISIBLE:View.INVISIBLE);
            Animation loadAnimation = AnimationUtils.loadAnimation(getActivity(),
                    isToShow? R.anim.fade_in_fast: R.anim.fade_out_fast);
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
            itemIllustrationLayout.startAnimation(loadAnimation);
            characterIconShade5.startAnimation(loadAnimation);
        }
    }

    public void setDetailContent(final ItemModel model, int number){
        itemIllustrationNameTextView.setText("  "+model.ChineseName+"  ");
        itemIllustrationNumberTextview.setText(" "+number+"  ");
        itemIllustrationItemImageView.setImageResource(model.imageId);
        itemIllustrationIllustrationTextView.setText(model.illustration);
        if(model.synthesisCompositionPosition.size() > 0){
            setComposition(true);
            synthesisLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setComposition(true);
                }
            });
            synthesisLayout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setComposition(false);
                }
            });
            itemSynthesisButton.setVisibility(View.VISIBLE);
            itemSynthesisButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemModel model2 = MainActivity.itemModelList.get(MainActivity.compositionArrayList.get(model.synthesisCompositionPosition.get(0)).composition.get(0));
                    int itemNumber1 = MainActivity.compositionArrayList.get(model.synthesisCompositionPosition.get(0)).materialNumber.get(0);
                    bagSynthesis1MainItem.setImageResource(model2.imageId);
                    bagSynthesis1MainItemName.setText("      方案一：" + model2.ChineseName);
                    bagSynthesis1MainItemNumber.setText("  "+itemNumber1+"  ");
                    switch(model2.quality){
                        case 0:
                            bagSynthesis1MainItem.setBackgroundResource(R.drawable.itembackgroundgrey);
                            break;
                        case 1:
                            bagSynthesis1MainItem.setBackgroundResource(R.drawable.itembackgroundgreen);
                            break;
                        case 2:
                            bagSynthesis1MainItem.setBackgroundResource(R.drawable.itembackgroundblue);
                            break;
                        case 3:
                            bagSynthesis1MainItem.setBackgroundResource(R.drawable.itembackgroundpurple);
                            break;
                        case 4:
                            bagSynthesis1MainItem.setBackgroundResource(R.drawable.itembackgroundyellow);
                            break;
                        default:
                    }
                    for(int i = 1; i < 4; i++){
                        if(i < MainActivity.compositionArrayList.get(model.synthesisCompositionPosition.get(0)).materialNumber.size()){
                            ItemModel model1 = MainActivity.itemModelList.get(MainActivity.compositionArrayList.get(model.synthesisCompositionPosition.get(0)).composition.get(i));
                            int itemNumber = MainActivity.compositionArrayList.get(model.synthesisCompositionPosition.get(0)).materialNumber.get(i);
                            switch(model1.quality){
                                case 0:
                                    bagSynthesis1Item.get(i-1).setBackgroundResource(R.drawable.itembackgroundgrey);
                                    break;
                                case 1:
                                    bagSynthesis1Item.get(i-1).setBackgroundResource(R.drawable.itembackgroundgreen);
                                    break;
                                case 2:
                                    bagSynthesis1Item.get(i-1).setBackgroundResource(R.drawable.itembackgroundblue);
                                    break;
                                case 3:
                                    bagSynthesis1Item.get(i-1).setBackgroundResource(R.drawable.itembackgroundpurple);
                                    break;
                                case 4:
                                    bagSynthesis1Item.get(i-1).setBackgroundResource(R.drawable.itembackgroundyellow);
                                    break;
                                default:
                            }
                            bagSynthesis1Item.get(i-1).setImageResource(model1.imageId);
                            int numberHave = pref.getInt(""+model1.id, 0);
                            bagSynthesis1Number.get(i-1).setText(""+numberHave+"/"+itemNumber);
                            if(numberHave < itemNumber){
                                bagSynthesis1Red.get(i-1).setVisibility(View.VISIBLE);
                            }else{
                                bagSynthesis1Red.get(i-1).setVisibility(View.INVISIBLE);
                            }
                        }else{
                            bagSynthesis1Item.get(i-1).setImageResource(R.color.transparent);
                            bagSynthesis1Item.get(i-1).setBackgroundResource(R.drawable.null_item_background);
                            bagSynthesis1Number.get(i-1).setText("0/0");
                        }
                    }
                    compositionNumber = 0;
                    synthesisNumber.setText(""+compositionNumber);
                    if(model.synthesisCompositionPosition.size() > 1){
                        synthesisLayout2.setVisibility(View.VISIBLE);
                        ItemModel model3 = MainActivity.itemModelList.get(MainActivity.compositionArrayList.get(model.synthesisCompositionPosition.get(1)).composition.get(0));
                        int itemNumber3 = MainActivity.compositionArrayList.get(model.synthesisCompositionPosition.get(1)).materialNumber.get(0);
                        bagSynthesis2MainItem.setImageResource(model3.imageId);
                        bagSynthesis2MainItemName.setText("      方案二：" + model3.ChineseName);
                        bagSynthesis2MainItemNumber.setText("  "+itemNumber3+"  ");
                        switch(model3.quality){
                            case 0:
                                bagSynthesis2MainItem.setBackgroundResource(R.drawable.itembackgroundgrey);
                                break;
                            case 1:
                                bagSynthesis2MainItem.setBackgroundResource(R.drawable.itembackgroundgreen);
                                break;
                            case 2:
                                bagSynthesis2MainItem.setBackgroundResource(R.drawable.itembackgroundblue);
                                break;
                            case 3:
                                bagSynthesis2MainItem.setBackgroundResource(R.drawable.itembackgroundpurple);
                                break;
                            case 4:
                                bagSynthesis2MainItem.setBackgroundResource(R.drawable.itembackgroundyellow);
                                break;
                            default:
                        }
                        for(int i = 1; i < 4; i++){
                            if(i < MainActivity.compositionArrayList.get(model.synthesisCompositionPosition.get(1)).materialNumber.size()){
                                ItemModel model4 = MainActivity.itemModelList.get(MainActivity.compositionArrayList.get(model.synthesisCompositionPosition.get(1)).composition.get(i));
                                int itemNumber4 = MainActivity.compositionArrayList.get(model.synthesisCompositionPosition.get(1)).materialNumber.get(i);
                                switch(model4.quality){
                                    case 0:
                                        bagSynthesis2Item.get(i-1).setBackgroundResource(R.drawable.itembackgroundgrey);
                                        break;
                                    case 1:
                                        bagSynthesis2Item.get(i-1).setBackgroundResource(R.drawable.itembackgroundgreen);
                                        break;
                                    case 2:
                                        bagSynthesis2Item.get(i-1).setBackgroundResource(R.drawable.itembackgroundblue);
                                        break;
                                    case 3:
                                        bagSynthesis2Item.get(i-1).setBackgroundResource(R.drawable.itembackgroundpurple);
                                        break;
                                    case 4:
                                        bagSynthesis2Item.get(i-1).setBackgroundResource(R.drawable.itembackgroundyellow);
                                        break;
                                    default:
                                }
                                bagSynthesis2Item.get(i-1).setImageResource(model4.imageId);
                                int numberHave = pref.getInt(""+model4.id, 0);
                                bagSynthesis2Number.get(i-1).setText(""+numberHave+"/"+itemNumber4);
                                if(numberHave < itemNumber4){
                                    bagSynthesis2Red.get(i-1).setVisibility(View.VISIBLE);
                                }else{
                                    bagSynthesis2Red.get(i-1).setVisibility(View.INVISIBLE);
                                }
                            }else{
                                bagSynthesis2Item.get(i-1).setImageResource(R.color.transparent);
                                bagSynthesis2Item.get(i-1).setBackgroundResource(R.drawable.null_item_background);
                                bagSynthesis2Number.get(i-1).setText("0/0");
                            }
                        }
                    }else{
                        synthesisLayout2.setVisibility(View.GONE);
                    }
                    showSynthesisDetail(true);

                    synthesisAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean temp = true;
                            for(int i = 1; i < MainActivity.compositionArrayList.get(model.synthesisCompositionPosition.get(isFirstComposition? 0:1)).materialNumber.size(); i++){
                                ItemModel model1 = MainActivity.itemModelList.get(MainActivity.compositionArrayList.get(model.synthesisCompositionPosition.get(isFirstComposition? 0:1)).composition.get(i));
                                int itemNumber = MainActivity.compositionArrayList.get(model.synthesisCompositionPosition.get(isFirstComposition? 0:1)).materialNumber.get(i);
                                int numberHave = pref.getInt(""+model1.id, 0);
                                if(itemNumber*(compositionNumber + 1) > numberHave){
                                    temp = false;
                                }
                            }
                            if(temp){
                                if(compositionNumber >= 99){
                                    compositionNumber = 98;
                                }
                                compositionNumber++;
                                synthesisNumber.setText(""+compositionNumber);
                            }
                        }
                    });
                    synthesisMax.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int canReach = 99;
                            for(int i = 1; i < MainActivity.compositionArrayList.get(model.synthesisCompositionPosition.get(isFirstComposition? 0:1)).materialNumber.size(); i++){
                                ItemModel model1 = MainActivity.itemModelList.get(MainActivity.compositionArrayList.get(model.synthesisCompositionPosition.get(isFirstComposition? 0:1)).composition.get(i));
                                int itemNumber = MainActivity.compositionArrayList.get(model.synthesisCompositionPosition.get(isFirstComposition? 0:1)).materialNumber.get(i);
                                int numberHave = pref.getInt(""+model1.id, 0);
                                if(itemNumber*canReach > numberHave){
                                    canReach = (int)(numberHave / itemNumber);
                                }
                            }
                            compositionNumber = canReach;
                            synthesisNumber.setText(""+compositionNumber);
                        }
                    });

                    synthesisReduce.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(compositionNumber > 0){
                                compositionNumber--;
                                synthesisNumber.setText(""+compositionNumber);
                            }
                        }
                    });
                    synthesisMin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(compositionNumber > 0){
                                compositionNumber = 0;
                                synthesisNumber.setText(""+compositionNumber);
                            }
                        }
                    });

                    synthesisButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(compositionNumber > 0){
                                SharedPreferences.Editor editor = pref.edit();
                                for(int i = 0; i < MainActivity.compositionArrayList.get(model.synthesisCompositionPosition.get(isFirstComposition? 0:1)).materialNumber.size(); i++){
                                    ItemModel model1 = MainActivity.itemModelList.get(MainActivity.compositionArrayList.get(model.synthesisCompositionPosition.get(isFirstComposition? 0:1)).composition.get(i));
                                    int itemNumber = MainActivity.compositionArrayList.get(model.synthesisCompositionPosition.get(isFirstComposition? 0:1)).materialNumber.get(i);
                                    int numberHave = pref.getInt(""+model1.id, 0);
                                    if(i == 0){
                                        editor.putInt(""+model1.id,numberHave + itemNumber*compositionNumber);
                                        itemIllustrationNumberTextview.setText(" "+(numberHave + itemNumber*compositionNumber)+"  ");

                                    }else{
                                        editor.putInt(""+model1.id,numberHave - itemNumber*compositionNumber);
                                    }
                                }
                                editor.apply();
                                if(characterIconShade6.getVisibility() == View.VISIBLE){
                                    Toast.makeText(m, "合成成功！", Toast.LENGTH_SHORT).show();
                                    showSynthesisDetail(false);
                                    itemAdapter.notifyItemRangeChanged(0,MainActivity.itemModelList.size());
                                }
                            }
                        }
                    });
                }
            });
        }else{
            itemSynthesisButton.setVisibility(View.INVISIBLE);
        }


    }

    public void showSynthesisDetail(final boolean isToShow){
        if(isAnimationEnd){
            isAnimationEnd = false;
            synthesisLayout.setVisibility(isToShow? View.VISIBLE:View.INVISIBLE);
            characterIconShade6.setVisibility(isToShow? View.VISIBLE:View.INVISIBLE);
            Animation loadAnimation = AnimationUtils.loadAnimation(getActivity(),
                    isToShow? R.anim.fade_in_fast: R.anim.fade_out_fast);
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
            synthesisLayout.startAnimation(loadAnimation);
            characterIconShade6.startAnimation(loadAnimation);
        }
    }

    public void setComposition(boolean isSetFirst){
        isFirstComposition = isSetFirst;
        synthesisCompositionChoose.setText("已选：方案"+(isSetFirst? "一":"二"));
        compositionNumber = 0;
        synthesisNumber.setText(""+compositionNumber);
        //synthesisLayout1State.setVisibility(isSetFirst? View.VISIBLE:View.INVISIBLE);
        //synthesisLayout2State.setVisibility(isSetFirst? View.INVISIBLE:View.VISIBLE);
    }

}
