package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class EquipmentSquare extends LinearLayout {
    final public int YELLOW = 1;
    final public int WHITE = 2;
    final public int BLUE = 3;
    final public int TRANSPARENT = 4;
    ArrayList<ImageView> squareList = new ArrayList<>();
    CharacterSquare characterSquare;
    public EquipmentSquare(Context context, AttributeSet set) {
        super(context,set);
        View v = LayoutInflater.from(context).inflate(R.layout.weapon_square, EquipmentSquare.this);
        ImageView temp = (ImageView) v.findViewById(R.id.weaponSquare00);
        squareList.add(temp);
        temp = (ImageView) v.findViewById(R.id.weaponSquare10);
        squareList.add(temp);
        temp = (ImageView) v.findViewById(R.id.weaponSquare20);
        squareList.add(temp);
        temp = (ImageView) v.findViewById(R.id.weaponSquare30);
        squareList.add(temp);
        temp = (ImageView) v.findViewById(R.id.weaponSquare01);
        squareList.add(temp);
        temp = (ImageView) v.findViewById(R.id.weaponSquare11);
        squareList.add(temp);
        temp = (ImageView) v.findViewById(R.id.weaponSquare21);
        squareList.add(temp);
        temp = (ImageView) v.findViewById(R.id.weaponSquare31);
        squareList.add(temp);
        temp = (ImageView) v.findViewById(R.id.weaponSquare02);
        squareList.add(temp);
        temp = (ImageView) v.findViewById(R.id.weaponSquare12);
        squareList.add(temp);
        temp = (ImageView) v.findViewById(R.id.weaponSquare22);
        squareList.add(temp);
        temp = (ImageView) v.findViewById(R.id.weaponSquare32);
        squareList.add(temp);
        temp = (ImageView) v.findViewById(R.id.weaponSquare03);
        squareList.add(temp);
        temp = (ImageView) v.findViewById(R.id.weaponSquare13);
        squareList.add(temp);
        temp = (ImageView) v.findViewById(R.id.weaponSquare23);
        squareList.add(temp);
        temp = (ImageView) v.findViewById(R.id.weaponSquare33);
        squareList.add(temp);
    }

    public void setCharacterSquare(CharacterSquare characterSquare){//设置哪些可以用
        this.characterSquare = characterSquare;
        int tempMax = Math.max(characterSquare.maxX, characterSquare.maxY);
        int i;
        for(i = 0; i < characterSquare.maxX; i++){
            int j;
            for(j = 0; j < characterSquare.maxY; j++){
                getImageView(i,j).setMaxHeight(tempMax < 4? 40:20);
                getImageView(i,j).setMaxWidth(tempMax < 4? 40:20);
                getImageView(i,j).setVisibility(VISIBLE);
            }
            for(; j < 4; j++){
                getImageView(i,j).setMaxHeight(tempMax < 4? 40:20);
                getImageView(i,j).setMaxWidth(tempMax < 4? 40:20);
                getImageView(i,j).setVisibility(GONE);
            }
        }
        for(; i < 4; i++){
            for(int j = 0; j < 4; j++){
                getImageView(i,j).setMaxHeight(tempMax < 4? 40:20);
                getImageView(i,j).setMaxWidth(tempMax < 4? 40:20);
                getImageView(i,j).setVisibility(GONE);
            }
        }
    }

    public void updateEquipment(){
        for(int k = 0; k < characterSquare.maxX; k++){
            for(int l = 0; l < characterSquare.maxY; l++){
                if(characterSquare.isBlock[k][l]==1){
                    setBackgroundColor(getImageView(k,l),WHITE);
                }else if(characterSquare.isBlock[k][l] == 2){
                    setBackgroundColor(getImageView(k,l),YELLOW);
                }else if(characterSquare.isBlock[k][l] == 3){
                    setBackgroundColor(getImageView(k,l),BLUE);
                }else{
                    setBackgroundColor(getImageView(k,l),TRANSPARENT);
                }
            }
        }
    }

    public void setBackgroundColor(ImageView imageView, int color){
        int temp;
        switch(color){
            case YELLOW:
                temp = R.color.colorBrown;
                break;
            case BLUE:
                temp = R.color.colorBlue;
                break;
            case WHITE:
                temp = R.color.colorWhite;
                break;
            default:
                temp = R.color.transparent;
        }
        imageView.setBackgroundColor(getResources().getColor(temp));
    }

    private ImageView getImageView(int a, int b){
        return squareList.get(a*4+b);
    }
}
class Square{//存储一把武器、一件装备或装备栏的格子
    boolean[][] block;
    int maxX;
    int maxY;
    public Square(int maxX, int maxY){
        block = new boolean[maxX][maxY];
        this.maxX = maxX;
        this.maxY = maxY;
        for(int i = 0; i < maxX; i++){
            for(int j = 0; j < maxY; j++){
                block[i][j] = false;
            }
        }
    }

    public Square setSquare(int x , int y){
        block[x][y] = true;
        return this;
    }
}

class Equipment{
    Square square;
    String name;
    String illustration = "";
    int quality;//4卓越黄 3优良紫 2标准蓝 1普通绿 0缺陷灰
    int imageId = 0;
    CharacterIconMessage equipCharacterIconMessage = null;
    boolean isWeapon;
    public Equipment(String name, Square square, boolean isWeapon, int quality){
        this.square = square;
        this.name = name;
        this.isWeapon = isWeapon;
        this.quality = quality;
    }
    public Equipment setIllustration(String illustration){
        this.illustration = illustration;
        return this;
    }
    public Equipment setResource(int resource){
        imageId = resource;
        return this;
    }
}

class CharacterSquare{//永久存储一个角色持有的装备和目前的装备栏
    CharacterIconMessage characterIconMessage;
    byte[][] isBlock = new byte[4][4];//是否被武器占用,0没有展示，1展示但为空，2武器，3防具
    int maxX;
    int maxY;
    Equipment weapon;
    Equipment armor;
    int weaponLastI = 0;
    int weaponLastJ = 0;
    int armorLastI = 0;
    int armorLastJ = 0;
    public CharacterSquare(Square square, CharacterIconMessage characterIconMessage){
        this.characterIconMessage = characterIconMessage;
        maxX = square.maxX;
        maxY = square.maxY;
        for(int i = 0; i < maxX; i++){
            for(int j = 0; j < maxY; j++){
                isBlock[i][j] = (byte)(square.block[i][j]? 1:0);
            }
        }
    }

    public void setEquipment(Equipment equipment){
        if(equipment != null){
            if(canEquip(equipment)){
                if(equipment.isWeapon){
                    if(weapon != null){
                        weapon.equipCharacterIconMessage = null;
                    }
                    weapon = equipment;
                    weapon.equipCharacterIconMessage = characterIconMessage;
                }else{
                    if(armor != null){
                        armor.equipCharacterIconMessage = null;
                    }
                    armor = equipment;
                    armor.equipCharacterIconMessage = characterIconMessage;
                }
                int temp = equipment.isWeapon? 2:3;
                for(int m = 0; m < maxX; m++){
                    for(int n = 0; n < maxY; n++){
                        if(isBlock[m][n] == temp){
                            isBlock[m][n] = 1;
                        }
                    }
                }

                int i = equipment.isWeapon ? weaponLastI : armorLastI;
                int j = equipment.isWeapon ? weaponLastJ : armorLastJ;
                j++;
                while(true) {
                    for (; i <= (maxX - equipment.square.maxX); i++) {
                        for (; j <= (maxY - equipment.square.maxY); j++) {
                            boolean flag = true;
                            loop1:for (int k = 0; k < equipment.square.maxX; k++) {
                                for (int l = 0; l < equipment.square.maxY; l++) {
                                    //是否被武器占用,0没有展示，1展示但为空，2武器，3防具
                                    if (equipment.square.block[k][l] && isBlock[k + i][l + j] != 1) {
                                        flag = false;
                                        break loop1;
                                    }
                                }
                            }
                            if (flag) {
                                if(equipment.isWeapon){
                                    weaponLastI = i;
                                    weaponLastJ = j;
                                }else{
                                    armorLastI = i;
                                    armorLastJ = j;
                                }
                                for (int k = 0; k < equipment.square.maxX; k++) {
                                    for (int l = 0; l < equipment.square.maxY; l++) {
                                        if (equipment.square.block[k][l]) {
                                            isBlock[k + i][l + j] = (byte) (equipment.isWeapon ? 2 : 3);
                                        }
                                    }
                                }
                                return;
                            }
                        }
                        j = 0;
                    }
                    i = 0;
                }
            }
        }else{

        }

    }

    public void unsnatchEquipment(boolean isWeapon){
        if(isWeapon){
            if(weapon != null){
                weapon.equipCharacterIconMessage = null;
            }
            weapon = null;
            weaponLastI = 0;
            weaponLastJ = -1;
        }else{
            if(armor != null){
                armor.equipCharacterIconMessage = null;
            }
            armor = null;
            armorLastI = 0;
            armorLastJ = -1;
        }

        int temp = isWeapon? 2:3;
        for(int m = 0; m < maxX; m++){
            for(int n = 0; n < maxY; n++){
                if(isBlock[m][n] == temp){
                    isBlock[m][n] = 1;
                }
            }
        }
    }

    public boolean canEquip(Equipment equipment){
        if(equipment.square.maxX <= maxX && equipment.square.maxY <= maxY){
            for(int i = 0; i <= (maxX - equipment.square.maxX); i++){
                for(int j = 0; j <= (maxY - equipment.square.maxY); j++){
                    boolean flag = true;
                    loop1:for(int k = 0; k < equipment.square.maxX; k++){
                        for(int l = 0; l < equipment.square.maxY; l++){
                            if(equipment.square.block[k][l] && (isBlock[k+i][l+j]==0 || isBlock[k+i][l+j] == (equipment.isWeapon? 3:2) )){
                                flag = false;
                                break loop1;
                            }
                        }
                    }
                    if(flag)return true;
                }
            }
        }
        return false;
    }
}