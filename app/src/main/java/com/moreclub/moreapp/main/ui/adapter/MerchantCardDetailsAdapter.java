package com.moreclub.moreapp.main.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.MerchantCardPromoExtra;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/4/14.
 */

public class MerchantCardDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private enum ITEM_TYPE{
        ITEM_TYPE_Norom,
        ITEM_TYPE_PromoExtra,
        ITEM_TYPE_PromoWine,
        ITEM_TYPE_Tips,
        ITEM_TYPE_SUBTITLE
    }

    private ArrayList<MerchantCardPromoExtra> promoExtrasList;

    private Context mContext;
    public MerchantCardDetailsAdapter(Context context,ArrayList<MerchantCardPromoExtra> list){
        promoExtrasList = list;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == MerchantCardDetailsAdapter.ITEM_TYPE.ITEM_TYPE_PromoExtra.ordinal()){
                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.merchant_card_extra_item, parent, false);
                return new MerchantCardDetailsAdapter.ItemExtraRecyclerViewHolder(view);
        } else if (viewType == MerchantCardDetailsAdapter.ITEM_TYPE.ITEM_TYPE_PromoWine.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.merchant_card_details_wine_item, parent, false);
            return new MerchantCardDetailsAdapter.ItemWineRecyclerViewHolder(view);
        }else if (viewType == MerchantCardDetailsAdapter.ITEM_TYPE.ITEM_TYPE_Tips.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.merchant_card_details_tip_item, parent, false);
            return new MerchantCardDetailsAdapter.ItemTipRecyclerViewHolder(view);
        }else if (viewType==MerchantCardDetailsAdapter.ITEM_TYPE.ITEM_TYPE_SUBTITLE.ordinal()){
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.merchant_card_details_subtitle_item, parent, false);
            return new MerchantCardDetailsAdapter.ItemSubTitleRecyclerViewHolder(view);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        MerchantCardPromoExtra item = promoExtrasList.get(position);
        if (viewHolder instanceof MerchantCardDetailsAdapter.ItemExtraRecyclerViewHolder){
            TextView extraTitle = ((MerchantCardDetailsAdapter.ItemExtraRecyclerViewHolder)viewHolder).extraTitle;
            TextView extraDes = ((MerchantCardDetailsAdapter.ItemExtraRecyclerViewHolder)viewHolder).extraDes;
            extraTitle.setText(item.getTitle());
            extraDes.setText(item.getRemark());
        } else if (viewHolder instanceof MerchantCardDetailsAdapter.ItemWineRecyclerViewHolder){
            TextView wineTitle = ((MerchantCardDetailsAdapter.ItemWineRecyclerViewHolder)viewHolder).wineTitle;
            TextView wineDes = ((MerchantCardDetailsAdapter.ItemWineRecyclerViewHolder)viewHolder).wineDes;
            wineTitle.setText(item.getWineName()+":"+item.getWineCount()+"款");
            wineDes.setText(item.getExWineCollect());
        }else if (viewHolder instanceof MerchantCardDetailsAdapter.ItemTipRecyclerViewHolder){
            TextView tipsTitle = ((MerchantCardDetailsAdapter.ItemTipRecyclerViewHolder)viewHolder).tipsTitle;
            tipsTitle.setText(item.getTipStr());
        }else if (viewHolder instanceof MerchantCardDetailsAdapter.ItemSubTitleRecyclerViewHolder){
            TextView tipsTitle = ((MerchantCardDetailsAdapter.ItemSubTitleRecyclerViewHolder)viewHolder).title;
            tipsTitle.setText(item.getTipStr());
        }

    }

    @Override
    public int getItemCount() {
        return promoExtrasList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MerchantCardPromoExtra item = promoExtrasList.get(position);
        return item.getType();
    }

    static class ItemExtraRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView extraTitle;
        TextView extraDes;
        public ItemExtraRecyclerViewHolder(View itemView) {
            super(itemView);
            extraTitle = ButterKnife.findById(itemView, R.id.extra_title);
            extraDes = ButterKnife.findById(itemView, R.id.extra_des);
        }
    }

    static class ItemWineRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView wineTitle;
        TextView wineDes;
        public ItemWineRecyclerViewHolder(View itemView) {
            super(itemView);
            wineTitle = ButterKnife.findById(itemView, R.id.wine_title);
            wineDes = ButterKnife.findById(itemView, R.id.wine_des);
        }
    }

    static class ItemTipRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView tipsTitle;
        public ItemTipRecyclerViewHolder(View itemView) {
            super(itemView);
            tipsTitle = ButterKnife.findById(itemView, R.id.tips_title);
        }
    }

    static class ItemSubTitleRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        public ItemSubTitleRecyclerViewHolder(View itemView) {
            super(itemView);
            title = ButterKnife.findById(itemView, R.id.title);
        }
    }
}
