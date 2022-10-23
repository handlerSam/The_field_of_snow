package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SceneAdapter extends RecyclerView.Adapter<SceneAdapter.ViewHolder> {

    private List<Scene> sceneList;
    public MainActivity mainActivity;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView interactionImageView;
        TextView interactionChineseTextView;
        TextView interactionEnglishTextView;
        public ViewHolder(View view) {
            super(view);
            interactionImageView = (ImageView) view.findViewById(R.id.interactionImageView);
            interactionChineseTextView = (TextView) view.findViewById(R.id.interactionChineseTextView);
            interactionEnglishTextView = (TextView) view.findViewById(R.id.interactionEnglishTextView);
        }
    }

    public SceneAdapter(List<Scene> sceneList, MainActivity mainActivity){
        this.sceneList = sceneList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scene_image,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Scene s = sceneList.get(position);
        holder.interactionImageView.setBackgroundResource(s.resourceImage);
        holder.interactionChineseTextView.setText(s.ChineseName);
        holder.interactionEnglishTextView.setText(s.EnglishName);
        if(s instanceof Shop){
            holder.interactionImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.shop = (Shop)s;
                    mainActivity.setFragment(MainActivity.SHOPFRAGMENT);
                }
            });
        }else if(s instanceof Battle){
            holder.interactionImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.operation = ((Battle) s).operation;
                    mainActivity.setFragment(MainActivity.CHOOSEFRAGMENT);
                }
            });
        }else if (s instanceof Bar){
            holder.interactionImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.bar = (Bar)s;
                    MainActivity.chooseTeam = MainMenuFragment.chooseTeam;
                    mainActivity.setFragment(MainActivity.BARFRAGMENT);
                }
            });
        }else if (s instanceof Plot){
            holder.interactionImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.dialoguesList = ((Plot) s).dialogueArrayList;
                    mainActivity.setFragment(MainActivity.GALFRAGMENT);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return sceneList.size();
    }
}
