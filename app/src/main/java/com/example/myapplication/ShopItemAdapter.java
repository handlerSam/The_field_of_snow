package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ViewHolder> {
    private List<ShopItem> shopItemList;
    public ShopFragment shopFragment;
    private SharedPreferences pref;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView shopItemNameTextView;
        ImageView shopItemImageView;
        TextView shopItemNumberTextView;
        TextView shopItemPriceTextView;
        LinearLayout shopItemShade;
        LinearLayout linearLayout4;
        ImageView shopItemCurrencyIcon;
        public ViewHolder(View view){
            super(view);
            shopItemNameTextView = (TextView) view.findViewById(R.id.shopItemNameTextView);
            shopItemImageView = (ImageView) view.findViewById(R.id.shopItemImageView);
            shopItemNumberTextView = (TextView) view.findViewById(R.id.shopItemNumberTextView);
            shopItemPriceTextView = (TextView) view.findViewById(R.id.shopItemPriceTextView);
            shopItemShade = (LinearLayout) view.findViewById(R.id.shopItemShade);
            linearLayout4 = (LinearLayout) view.findViewById(R.id.linearLayout4);
            shopItemCurrencyIcon = (ImageView) view.findViewById(R.id.shopItemCurrencyIcon);
        }
    }
    public ShopItemAdapter(List<ShopItem> shopItemList, ShopFragment shopFragment){
        this.shopItemList = shopItemList;
        this.shopFragment = shopFragment;
        pref = shopFragment.getActivity().getSharedPreferences("Item", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ShopItem shopItem = shopItemList.get(position);
        refresh(holder,shopItem);
        holder.linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shopFragment.shopRecyclerView.getVisibility() == View.VISIBLE){
                    shopFragment.itemDetailNameTextView.setText("  "+shopItem.itemModel.ChineseName+"  ");
                    shopFragment.itemDetailNumberInBagTextView.setText("   库存 "+pref.getInt(""+shopItem.itemModel.id, 0)+"   ");
                    shopFragment.itemDetailImageView.setImageResource(shopItem.itemModel.imageId);
                    shopFragment.itemDetailIllustrationTextView.setText(shopItem.itemModel.illustration);
                    shopFragment.itemDetailPriceTextView.setText("价格："+shopItem.itemPrice);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) shopFragment.itemDetailCurrencyIconImageView.getLayoutParams();
                    int temp = shopItem.currency == ShopItem.HCY ? 65 : (shopItem.currency == ShopItem.YS ? 60 : 53);
                    params.width = temp;
                    params.height = temp;
                    shopFragment.itemDetailCurrencyIconImageView.setLayoutParams(params);
                    shopFragment.itemDetailCurrencyIconImageView.setImageResource(shopItem.currency == ShopItem.HCY ? R.drawable.item_hcy : (shopItem.currency == ShopItem.YS ? R.drawable.source : R.drawable.source_ingot));
                    shopFragment.itemDetailLeftTextView.setText("剩余："+shopItem.itemNumber);
                    shopFragment.showPurchaseLayout(true);
                    final int maxPurchaseNumber = Math.min(getCurrencyResourceNumber(shopItem.currency) / shopItem.itemPrice,shopItem.itemNumber);
                    shopFragment.itemDetailNumberText.setText(""+Math.min(maxPurchaseNumber,1));
                    shopFragment.itemDetailMin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shopFragment.itemDetailNumberText.setText(""+0);
                        }
                    });
                    shopFragment.itemDetailMax.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shopFragment.itemDetailNumberText.setText(""+maxPurchaseNumber);
                        }
                    });
                    shopFragment.itemDetailAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int temp = Integer.parseInt((String)shopFragment.itemDetailNumberText.getText());
                            if(maxPurchaseNumber > temp){
                                shopFragment.itemDetailNumberText.setText(""+(temp+1));
                            }
                        }
                    });
                    shopFragment.itemDetailReduce.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int temp = Integer.parseInt((String)shopFragment.itemDetailNumberText.getText());
                            if(temp > 0){
                                shopFragment.itemDetailNumberText.setText(""+(temp-1));
                            }
                        }
                    });
                    shopFragment.itemDetailPromotionConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(shopFragment.shopRecyclerView.getVisibility() == View.INVISIBLE){
                                int temp = Integer.parseInt((String)shopFragment.itemDetailNumberText.getText());
                                setCurrencyResourceNumber(shopItem.currency,getCurrencyResourceNumber(shopItem.currency)-temp*shopItem.itemPrice);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putInt(""+shopItem.itemModel.id,pref.getInt(""+shopItem.itemModel.id,0)+temp);
                                editor.apply();
                                shopItem.itemNumber -= temp;
                                refresh(holder,shopItem);
                                checkSoldOut(holder,shopItem);
                                shopFragment.refreshResource();
                                shopFragment.showPurchaseLayout(false);
                                Toast.makeText(shopFragment.m.MAINCONTEXT, "购买成功！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        int currencyIcon = shopItem.currency;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.shopItemCurrencyIcon.getLayoutParams();
        int temp = currencyIcon == ShopItem.HCY ? 65 : (currencyIcon == ShopItem.YS ? 60 : 53);
        params.width = temp;
        params.height = temp;
        holder.shopItemCurrencyIcon.setLayoutParams(params);
        holder.shopItemCurrencyIcon.setImageResource(currencyIcon == ShopItem.HCY ? R.drawable.item_hcy : (currencyIcon == ShopItem.YS ? R.drawable.source : R.drawable.source_ingot));
        checkSoldOut(holder,shopItem);
    }

    @Override
    public int getItemCount() {
        return shopItemList.size();
    }

    public int getCurrencyResourceNumber(int currencyIcon){
        switch(currencyIcon){
            case ShopItem.HCY:
                return pref.getInt("5",0);
            case ShopItem.YS:
                return pref.getInt("4",0);
            case ShopItem.YSD:
                return pref.getInt("ysd",0);
            default:
        }
        return -1;
    }
    public void setCurrencyResourceNumber(int currencyIcon, int number){
        SharedPreferences.Editor editor = pref.edit();
        switch(currencyIcon){
            case ShopItem.HCY:
                editor.putInt("5",number);
                break;
            case ShopItem.YS:
                editor.putInt("4",number);
                break;
            case ShopItem.YSD:
                editor.putInt("ysd",number);
                break;
            default:
        }
        editor.apply();
    }
    public void checkSoldOut(ViewHolder holder, ShopItem item){
        if(item.itemNumber == 0){
            holder.shopItemShade.setVisibility(View.VISIBLE);
            holder.linearLayout4.setOnClickListener(null);
        }else{
            holder.shopItemShade.setVisibility(View.INVISIBLE);
        }
    }
    public void refresh(ViewHolder holder, ShopItem shopItem){
        holder.shopItemNameTextView.setText(shopItem.itemModel.ChineseName);
        holder.shopItemImageView.setImageResource(shopItem.itemModel.imageId);
        holder.shopItemNumberTextView.setText("剩余"+shopItem.itemNumber);
        holder.shopItemPriceTextView.setText(""+shopItem.itemPrice);
    }
}
