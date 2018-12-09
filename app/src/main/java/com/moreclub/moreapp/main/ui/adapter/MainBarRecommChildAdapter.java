package com.moreclub.moreapp.main.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.MerchantHomeDto;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;

import java.util.List;

/**
 * Created by Captain on 2017/7/21.
 */

public class MainBarRecommChildAdapter extends RecyclerAdapter<MerchantItem> {

    private int width;
    private int height;

    public MainBarRecommChildAdapter(Context context, int layoutId, List<MerchantItem> datas) {
        super(context, layoutId, datas);
        width = ScreenUtil.getScreenWidth(context) - ScreenUtil.dp2px(context, 32);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        LinearLayout rlParent = viewHolder.getView(R.id.main_bar_item_layout);
//        iv.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        rlParent.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
        return viewHolder;
    }

    @Override
    public void convert(RecyclerViewHolder holder, MerchantItem merchantItem) {
        String url = merchantItem.getThumb();
        String title = merchantItem.getName();
        String desc = merchantItem.getSellingPoint();
        String bizArea = merchantItem.getBusName();
        String tags = merchantItem.getTags();

        holder.setText(R.id.main_bar_title, title);
        holder.setText(R.id.main_bar_item_desc, desc);
        holder.setText(R.id.main_bar_bizarea, bizArea);
        if (tags!=null)
            holder.setText(R.id.main_bar_tags, tags.replaceAll(",", " "));
        if (MoreUser.getInstance().getUserLocationLat()==0){
            holder.setText(R.id.main_bar_item_distance, "");
        } else {
            String distance =  AppUtils.getDistance(
                    MoreUser.getInstance().getUserLocationLat(),
                    MoreUser.getInstance().getUserLocationLng(),
                    merchantItem.getLat(),merchantItem.getLng());
            holder.setText(R.id.main_bar_item_distance, distance);
        }

        Boolean supportCoupon = merchantItem.getSupportCoupon();
        if (supportCoupon!=null&&supportCoupon){
            holder.getView(R.id.coupon_tag).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.coupon_tag).setVisibility(View.GONE);
        }

        List<MerchantHomeDto.CardBean> promoCard = merchantItem.getCardList();
        if (promoCard != null && promoCard.size() > 0) {
            for (int i = 0; i < promoCard.size(); i++) {
                MerchantHomeDto.CardBean promoItem = promoCard.get(i);
                if (promoItem != null && promoItem.getCardType() == 1) {
                    holder.getView(R.id.orange_tag).setVisibility(View.VISIBLE);
                } else if (promoItem != null && promoItem.getCardType() == 2) {
                    holder.getView(R.id.black_tag).setVisibility(View.VISIBLE);
                }
            }
        } else {
            holder.getView(R.id.orange_tag).setVisibility(View.GONE);
            holder.getView(R.id.black_tag).setVisibility(View.GONE);
        }
        Glide.with(mContext)
                .load(url)
                .dontAnimate()
                .into((ImageView) holder.getView(R.id.main_bar_item_logo));
        holder.getView(R.id.rl_first).setPadding(0,0,0,0);
        holder.getView(R.id.rl_second).setPadding(0,0,0,0);
        holder.getView(R.id.main_bar_item_desc).setPadding(0,0,0,0);
    }
}