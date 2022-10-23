package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<ItemModel> itemModelList;
    private SharedPreferences pref;
    private BagFragment bagFragment;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView itemBackgroundImage;
        ImageView itemImage;
        TextView itemNumber;
        public ViewHolder(View view){
            super(view);
            itemBackgroundImage = (ImageView) view.findViewById(R.id.itemBackgroundImage);
            itemImage = (ImageView) view.findViewById(R.id.itemImage);
            itemNumber = (TextView) view.findViewById(R.id.itemNumber);
        }
    }

    public ItemAdapter(List<ItemModel> itemModelList, MainActivity m, BagFragment b){
        this.itemModelList = itemModelList;
        pref = m.getSharedPreferences("Item", Context.MODE_PRIVATE);
        bagFragment = b;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ItemModel itemModel = itemModelList.get(position);
        final int number = pref.getInt(""+itemModel.id, 0);
        if(number == 0){
            changeLight(holder, false);
        }else{
            changeLight(holder, true);
        }
        holder.itemNumber.setText(" "+ pref.getInt(""+itemModel.id, 0) +" ");//记得最后直接改成读取SharedPreference
        holder.itemImage.setImageResource(itemModel.imageId);
        holder.itemBackgroundImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bagFragment.setDetailContent(itemModel, number);
                bagFragment.showDetail(true);
            }
        });
        switch(itemModel.quality){
            case 0:
                holder.itemBackgroundImage.setImageResource(R.drawable.itembackgroundgrey);
                break;
            case 1:
                holder.itemBackgroundImage.setImageResource(R.drawable.itembackgroundgreen);
                break;
            case 2:
                holder.itemBackgroundImage.setImageResource(R.drawable.itembackgroundblue);
                break;
            case 3:
                holder.itemBackgroundImage.setImageResource(R.drawable.itembackgroundpurple);
                break;
            case 4:
                holder.itemBackgroundImage.setImageResource(R.drawable.itembackgroundyellow);
                break;
            default:
        }

    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }

    public void changeLight(ViewHolder holder, boolean isBright){
        holder.itemBackgroundImage.clearColorFilter();
        holder.itemImage.clearColorFilter();
        int temp = isBright ? 0: -80;
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[] { 1,0,0,0, temp, 0,1,0,0,temp,0,0,1,0,temp,0,0,0,1,0});
        holder.itemBackgroundImage.setColorFilter(new ColorMatrixColorFilter(cMatrix));
        holder.itemImage.setColorFilter(new ColorMatrixColorFilter(cMatrix));
    }
}
