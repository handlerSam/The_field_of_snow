package com.example.myapplication;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class Item extends ConstraintLayout {
    ImageView itemBackgroundImage;
    ImageView itemImage;
    TextView itemNumber;
    Context context;
    ItemModel model;
    int number;
    public Item(Context context, ItemModel model, int number){
        super(context);
        View v = LayoutInflater.from(context).inflate(R.layout.item,Item.this);
        this.context = context;
        this.number = number;
        itemBackgroundImage = (ImageView) v.findViewById(R.id.itemBackgroundImage);
        itemImage = (ImageView) v.findViewById(R.id.itemImage);
        itemNumber = (TextView) v.findViewById(R.id.itemNumber);
        itemNumber.setText(String.valueOf(number));
        this.model = model;
        if(model != null){
            itemImage.setImageResource(model.imageId);
            switch(model.quality){
                case 0:
                    itemBackgroundImage.setImageResource(R.drawable.itembackgroundgrey);
                    break;
                case 1:
                    itemBackgroundImage.setImageResource(R.drawable.itembackgroundgreen);
                    break;
                case 2:
                    itemBackgroundImage.setImageResource(R.drawable.itembackgroundblue);
                    break;
                case 3:
                    itemBackgroundImage.setImageResource(R.drawable.itembackgroundpurple);
                    break;
                case 4:
                    itemBackgroundImage.setImageResource(R.drawable.itembackgroundyellow);
                    break;
                default:
            }
        }
    }
    public void setNumber(int number){
        this.number = number;
        itemNumber.setText(String.valueOf(number));
    }

    public void changeLight(boolean isBright){
        int temp = isBright ? 10: -10;
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[] { 1,0,0,0, temp, 0,1,0,0,temp,0,0,1,0,temp,0,0,0,1,0});
        itemBackgroundImage.setColorFilter(new ColorMatrixColorFilter(cMatrix));
        itemImage.setColorFilter(new ColorMatrixColorFilter(cMatrix));
    }
}
