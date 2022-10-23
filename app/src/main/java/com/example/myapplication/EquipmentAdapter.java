package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> implements Filterable {
    List<Equipment> filteredList;
    private List<Equipment> equipmentList;
    private Context context;
    private CharacterSquare characterSquare;
    public CharacterIconMessage onChosenCharacterIconMessage;
    public EquipmentSquare equipmentSquare;
    EquipmentAdapter adapter = this;
    EquipmentAdapter anotherAdapter;
    static class ViewHolder extends RecyclerView.ViewHolder{
        public static final int CANEQUIP = 0;
        public static final int ONEQUIP = 1;
        public static final int ONOTHEREQUIP = 2;
        public static final int BLOCKCONFLICT = 3;
        ImageView weaponIcon;
        TextView weaponIconQuality;
        TextView weaponIconName;
        TextView weaponIconIllustration;
        ImageView weaponIconOnChosen;
        ImageView weaponIconUnavailable;
        FrameLayout weaponIconOnClick;
        int state = CANEQUIP;
        public ViewHolder(View view){
            super(view);
            weaponIcon = (ImageView) view.findViewById(R.id.weaponIcon);
            weaponIconQuality = (TextView) view.findViewById(R.id.weaponIconQuality);
            weaponIconName = (TextView) view.findViewById(R.id.weaponIconName);
            weaponIconIllustration = (TextView) view.findViewById(R.id.weaponIconIllustration);
            weaponIconOnChosen = (ImageView) view.findViewById(R.id.weaponIconOnChosen);
            weaponIconUnavailable = (ImageView) view.findViewById(R.id.weaponIconUnavailable);
            weaponIconOnClick = (FrameLayout) view.findViewById(R.id.weaponIconOnClick);
        }
    }

    public EquipmentAdapter(List<Equipment> equipmentList, Context context, CharacterSquare characterSquare, CharacterIconMessage characterIconMessage, EquipmentSquare equipmentSquare){
        this.equipmentList = equipmentList;
        this.filteredList = equipmentList;
        this.context = context;
        this.characterSquare = characterSquare;
        this.onChosenCharacterIconMessage = characterIconMessage;
        this.equipmentSquare = equipmentSquare;
        equipmentSquare.setCharacterSquare(characterSquare);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weapon_icon,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Equipment equipment = filteredList.get(position);
        if(equipment.imageId != 0) holder.weaponIcon.setImageResource(equipment.imageId);
        holder.weaponIconIllustration.setText(equipment.illustration);
        holder.weaponIconName.setText(equipment.name);
        holder.weaponIconName.setTextColor(context.getResources().getColor(equipment.isWeapon? R.color.colorYellow:R.color.colorLightBlue));
        //4卓越黄 3优良紫 2标准蓝 1普通绿 0缺陷灰
        switch(equipment.quality){
            case 0:
                holder.weaponIconQuality.setText("缺陷");
                holder.weaponIconQuality.setBackground(context.getResources().getDrawable(R.drawable.greyrect));
                break;
            case 1:
                holder.weaponIconQuality.setText("普通");
                holder.weaponIconQuality.setBackground(context.getResources().getDrawable(R.drawable.greenrect));
                break;
            case 2:
                holder.weaponIconQuality.setText("标准");
                holder.weaponIconQuality.setBackground(context.getResources().getDrawable(R.drawable.bluerect));
                break;
            case 3:
                holder.weaponIconQuality.setText("优良");
                holder.weaponIconQuality.setBackground(context.getResources().getDrawable(R.drawable.pinkrect));
                break;
            default:
                holder.weaponIconQuality.setText("卓越");
                holder.weaponIconQuality.setBackground(context.getResources().getDrawable(R.drawable.yellowrect));
                break;
        }
        if(equipment.equipCharacterIconMessage != null){
            if(equipment.equipCharacterIconMessage == onChosenCharacterIconMessage){
                holder.weaponIconOnChosen.setVisibility(VISIBLE);
                holder.weaponIconUnavailable.setVisibility(INVISIBLE);
                holder.state = ViewHolder.ONEQUIP;
            }else{
                holder.weaponIconOnChosen.setVisibility(INVISIBLE);
                holder.weaponIconUnavailable.setVisibility(VISIBLE);
                holder.state = ViewHolder.ONOTHEREQUIP;
            }
        }else{
            if(characterSquare.canEquip(equipment)){
                holder.weaponIconOnChosen.setVisibility(INVISIBLE);
                holder.weaponIconUnavailable.setVisibility(INVISIBLE);
                holder.state = ViewHolder.CANEQUIP;
            }else{
                holder.weaponIconOnChosen.setVisibility(INVISIBLE);
                holder.weaponIconUnavailable.setVisibility(VISIBLE);
                holder.state = ViewHolder.BLOCKCONFLICT;
            }
        }

        holder.weaponIconOnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).playSound(MainActivity.voiceBtn,1.0f,1.0f);
                if(equipment.equipCharacterIconMessage == onChosenCharacterIconMessage){
                    characterSquare.unsnatchEquipment(equipment.isWeapon);
                    equipmentSquare.updateEquipment();
                    adapter.notifyDataSetChanged();
                    anotherAdapter.notifyDataSetChanged();
                }
                if(holder.state == ViewHolder.CANEQUIP){
                    characterSquare.setEquipment(equipment);
                    equipmentSquare.updateEquipment();
                    adapter.notifyDataSetChanged();
                    anotherAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            //执行过滤操作
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                filteredList = new ArrayList<>();
                int temp = Integer.parseInt(constraint.toString());
                if(temp != 5){
                    for(Equipment e : equipmentList){
                        if(e.quality == temp){
                            filteredList.add(e);
                        }
                    }
                }else{
                    filteredList = equipmentList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }


            //把过滤后的值返回出来
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<Equipment>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }
}
