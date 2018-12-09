package com.moreclub.moreapp.main.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.information.constant.Constants;
import com.moreclub.moreapp.information.ui.activity.LiveActivity;
import com.moreclub.moreapp.main.model.Activity;
import com.moreclub.moreapp.main.model.MerchantHomeDto;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/1.
 */

public class TotalBarsAdapter extends RecyclerAdapter<MerchantItem> {
    public TotalBarsAdapter(Context context, int layoutId, List<MerchantItem> datas) {
        super(context, layoutId, datas);
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
        if (tags != null)
            holder.setText(R.id.main_bar_tags, tags.replaceAll(",", " "));
        if (MoreUser.getInstance().getUserLocationLat() == 0) {
            holder.setText(R.id.main_bar_item_distance, "");
        } else {
            String distance = AppUtils.getDistance(
                    MoreUser.getInstance().getUserLocationLat(),
                    MoreUser.getInstance().getUserLocationLng(),
                    merchantItem.getLat(), merchantItem.getLng());
            holder.setText(R.id.main_bar_item_distance, distance);
        }

        Boolean supportCoupon = merchantItem.getSupportCoupon();
        if (supportCoupon != null && supportCoupon) {
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
                    holder.getView(R.id.black_tag).setVisibility(View.GONE);
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
        final List<Activity> activities = merchantItem.getActivities();
        View.OnClickListener activityClick1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**zune:进入活动1详情**/
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(mContext, LiveActivity.class);
                intent.putExtra(Constants.KEY_ACT_ID, activities.get(0).getActivityId());
                ActivityCompat.startActivity(mContext, intent, compat.toBundle());
            }
        };
        View.OnClickListener activityClick2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**zune:进入活动2详情**/
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(mContext, LiveActivity.class);
                intent.putExtra(Constants.KEY_ACT_ID, activities.get(1).getActivityId());
                ActivityCompat.startActivity(mContext, intent, compat.toBundle());
            }
        };
        if (activities == null || activities.size() == 0) {
            holder.setVisible(R.id.rl_activities1,false);
            holder.setVisible(R.id.rl_activities2,false);
        } else if (activities.size() == 1) {
            holder.setVisible(R.id.rl_activities1,true);
            holder.setVisible(R.id.rl_activities2,false);
            String activityTitle = activities.get(0).getTitle();
            holder.setText(R.id.tv_activity_title,activityTitle);
            String bannerPhoto = activities.get(0).getBannerPhoto();
            Glide.with(mContext)
                    .load(bannerPhoto)
                    .dontAnimate()
                    .into((ImageView) holder.getView(R.id.activity_icon));
            Activity activityItem = activities.get(0);
            String time1 = formatDay(activityItem.getHoldingType(), activityItem.getHoldingTimes(), activityItem.getStartTime())
                    + formatTime(activityItem.getStartTime()) + "-" + formatTime(activityItem.getEndTime());
            holder.setText(R.id.tv_activity_content,time1);
            holder.setOnClickListener(R.id.rl_activities1,activityClick1);
        } else if (activities.size() > 1) {
            holder.setVisible(R.id.rl_activities1,true);
            holder.setVisible(R.id.rl_activities2,true);
            String activityTitle = activities.get(0).getTitle();
            holder.setText(R.id.tv_activity_title,activityTitle);
            String bannerPhoto = activities.get(0).getBannerPhoto();
            Glide.with(mContext)
                    .load(bannerPhoto)
                    .dontAnimate()
                    .into((ImageView) holder.getView(R.id.activity_icon));
            Activity activityItem = activities.get(0);
            String time1 = formatDay(activityItem.getHoldingType(), activityItem.getHoldingTimes(), activityItem.getStartTime())
                    + formatTime(activityItem.getStartTime()) + "-" + formatTime(activityItem.getEndTime());
            holder.setText(R.id.tv_activity_content,time1);
            holder.setOnClickListener(R.id.rl_activities1,activityClick1);
            String activityTitle2 = activities.get(1).getTitle();
            holder.setText(R.id.tv_activity_title2,activityTitle2);
            String bannerPhoto2 = activities.get(1).getBannerPhoto();
            Glide.with(mContext)
                    .load(bannerPhoto2)
                    .dontAnimate()
                    .into((ImageView) holder.getView(R.id.activity_icon2));
            Activity activityItem2 = activities.get(1);
            String time2 = formatDay(activityItem2.getHoldingType(), activityItem2.getHoldingTimes(), activityItem2.getStartTime())
                    + formatTime(activityItem2.getStartTime()) + "-" + formatTime(activityItem2.getEndTime());
            holder.setText(R.id.tv_activity_content2,time2);
            holder.setOnClickListener(R.id.rl_activities2,activityClick2);
        }
    }

    private String formatTime(Long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = new Date(time);
        String minute = format.format(date);
        return minute;
    }

    private String formatDay(int holdingType, String holdingTimes, Long time) {
        if (holdingType == 2) {
            String[] split = holdingTimes.split(",");
            StringBuffer sb = new StringBuffer("每");
            for (int i = 0; i < split.length; i++) {
                if (i < split.length - 1)
                    sb.append("周" + split[i] + ",");
                else
                    sb.append("周" + split[i]);
            }
            return sb.toString();
        } else if (holdingType == 1) {
            SimpleDateFormat format = new SimpleDateFormat("MM月dd日 EE ");
            Date date = new Date(time);
            String day = format.format(date);
            return day;
        }
        return "";
    }
}
