package com.example.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.ViewHolder> {

    public List<CharacterIconMessage> mCharacterIconList;
    public List<ViewHolder> mViewHolderList = new ArrayList<>();

    CharacterFragment fatherFragment;
    Context MAINCONTEXT;

    int[] holderId; // 下标为characterIconList的下标，数值对应holderList的下标
    public int chooseIcon = -1;

    static class ViewHolder extends RecyclerView.ViewHolder{
        int position = -1;
        int id;//自己在holderList里的id
        ImageView iconImage;
        ImageView iconElite;
        TextView iconLv;
        ImageView iconFrame;
        ImageView iconWeapon;
        ImageView iconState;
        public ViewHolder(View v){
            super(v);
            iconImage = (ImageView) v.findViewById(R.id.iconImage);
            iconElite = (ImageView) v.findViewById(R.id.iconElite);
            iconLv = (TextView) v.findViewById(R.id.iconLv);
            iconFrame = (ImageView) v.findViewById(R.id.iconFrame);
            iconWeapon = (ImageView) v.findViewById(R.id.iconWeapon);
            iconState = (ImageView) v.findViewById(R.id.iconState);
        }
    }

    public CharacterAdapter(ArrayList<CharacterIconMessage> mList, Context MAINCONTEXT, CharacterFragment fatherFragment){
        mCharacterIconList = mList;
        holderId = new int[mList.size()];
        this.MAINCONTEXT = MAINCONTEXT;
        this.fatherFragment = fatherFragment;
        for(int i = 0; i < mList.size(); i++){
            holderId[i] = -1;
        }
        onChosen(null);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(MAINCONTEXT).inflate(R.layout.character_icon, parent, false);
        final ViewHolder holder = new ViewHolder(v);
        holder.id = mViewHolderList.size();
        mViewHolderList.add(holder);
        holder.iconImage.setSoundEffectsEnabled(false);
        holder.iconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)MAINCONTEXT).playSound(MainActivity.voiceBtn,1.0f,1.0f);
                if(!fatherFragment.isChooseTeamShow && !fatherFragment.isEquipmentColumnShow){
                    if(chooseIcon != holder.position){
                        onChosen(holder);
                    }else{
                        if(CharacterFragment.mode == CharacterFragment.BROWSER){
                            onChosen(null);
                        }else{
                            if(holder.position != -1){
                                if(mCharacterIconList.get(holder.position).inChooseList){
                                    mCharacterIconList.get(holder.position).inChooseList = false;
                                    MainActivity.mChooseList.remove(mCharacterIconList.get(holder.position));
                                    holder.iconState.setVisibility(View.INVISIBLE);
                                }else{
                                    if(MainActivity.mChooseList.size() < fatherFragment.maxTeamMember){
                                        mCharacterIconList.get(holder.position).inChooseList = true;
                                        MainActivity.mChooseList.remove(mCharacterIconList.get(holder.position));
                                        MainActivity.mChooseList.add(mCharacterIconList.get(holder.position));
                                        holder.iconState.setVisibility(View.VISIBLE);
                                    }else{
                                        Toast.makeText(MAINCONTEXT,"人员已满",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                fatherFragment.setCharacterNumber(MainActivity.mChooseList.size());
                            }
                        }
                    }
                }
            }
        });
        fatherFragment.setCharacterNumber(MainActivity.mChooseList.size());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CharacterIconMessage m = mCharacterIconList.get(position);
        if(holder.position != -1){
            holderId[holder.position] = -1;
        }
        holder.position = position;
        holderId[position] = holder.id;
        holder.iconImage.setImageResource(getResourceByString(m.name + "icon"));
        holder.iconElite.setImageResource(getResourceByString("elite"+m.elite));
        holder.iconLv.setText("Lv"+m.level);
        holder.iconWeapon.setImageResource(getResourceByString(m.weapon == Character.PHYSIC? "physicbattleicon":"magicbattleicon"));
        if(position == chooseIcon){
            holder.iconFrame.setVisibility(View.VISIBLE);
        }else{
            holder.iconFrame.setVisibility(View.INVISIBLE);
        }
        mCharacterIconList.get(holder.position).inChooseList = false;
        holder.iconState.setVisibility(View.INVISIBLE);

        //万一之前就在队伍里：
        if(mCharacterIconList.get(holder.position).inChooseList){
            if(holder.iconState.getVisibility() == View.INVISIBLE){
                holder.iconState.setVisibility(View.VISIBLE);
            }
        }
        if(position == mCharacterIconList.size()-1){
            MainActivity.mChooseList.clear();
            fatherFragment.setCharacterNumber(MainActivity.mChooseList.size());
        }
    }

    @Override
    public int getItemCount() {
        return mCharacterIconList.size();
    }

    private int getResourceByString(String str){
        Resources res = MAINCONTEXT.getResources();
        int resourceId = res.getIdentifier(str,"drawable",MAINCONTEXT.getPackageName());
        return resourceId;
    }

    private void onChosen(ViewHolder holder){
        if(chooseIcon != -1 && holderId[chooseIcon] != -1){
            mViewHolderList.get(holderId[chooseIcon]).iconFrame.setVisibility(View.INVISIBLE);
        }
        if(holder != null){
            chooseIcon = holder.position;
            holder.iconFrame.setVisibility(View.VISIBLE);
            fatherFragment.onChosen(chooseIcon);
        }else{
            chooseIcon = -1;
            fatherFragment.onChosen(-1);
        }
    }
}
