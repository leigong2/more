package com.moreclub.moreapp.information.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2017-05-16.
 */

public class NewStoreAdapter extends RecyclerAdapter<MerchantItem> {

    public NewStoreAdapter(Context context, int layoutId, List<MerchantItem> datas) {
        super(context, layoutId, datas);
        sortDatas(datas);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.updatePosition(position);
        MerchantItem newStoreDetailDetail = mDatas.get(position);
        convert(holder, newStoreDetailDetail);
        TextView tvCreateTime = holder.getView(R.id.tv_create_time);
        if (position > 0 && TextUtils.equals(mDatas.get(position).getShelveDate(), mDatas.get(position - 1).getShelveDate())) {
            tvCreateTime.setVisibility(View.GONE);
        } else if (position == 0) {
            tvCreateTime.setVisibility(View.VISIBLE);
            tvCreateTime.setBackgroundResource(R.color.regButton);
            tvCreateTime.setText(getWeekOfDate(mDatas.get(position).getShelveDate()));
        } else {
            tvCreateTime.setVisibility(View.VISIBLE);
            tvCreateTime.setBackgroundResource(R.color.mainItemDvider);
            tvCreateTime.setText(getWeekOfDate(mDatas.get(position).getShelveDate()));
        }
    }

    @Override
    public void convert(RecyclerViewHolder holder, final MerchantItem newStoreDetail) {
        ImageView itemImage = holder.getView(R.id.main_bar_item_logo);
        Glide.with(mContext)
                .load(newStoreDetail.getThumb())
                .dontAnimate()
                .into(itemImage);
        holder.setText(R.id.main_bar_title, newStoreDetail.getName());
        holder.setText(R.id.main_bar_item_distance, newStoreDetail.getDistanceResult());


        holder.setText(R.id.main_bar_item_desc, newStoreDetail.getSellingPoint());
        holder.setText(R.id.main_bar_bizarea, newStoreDetail.getBusName());
        holder.setText(R.id.main_bar_tags, newStoreDetail.getTags());
        LinearLayout item = holder.getView(R.id.main_bar_item_layout);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(mContext, MerchantDetailsViewActivity.class);
                intent.putExtra("mid", newStoreDetail.getMid() + "");
                ActivityCompat.startActivity(mContext, intent, compat.toBundle());
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    public static String getWeekOfDate(String date) {
        java.sql.Date format = java.sql.Date.valueOf(date);
        String[] weekDaysName = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return "— " + date + " " + weekDaysName[intWeek]+" —";
    }

    private boolean compareDates(String date1, String date2) {
        boolean bigger;
        String year1 = date1.substring(0, 4);
        String year2 = date2.substring(0, 4);
        String month1 = date1.substring(5, 7);
        String month2 = date2.substring(5, 7);
        String day1 = date1.substring(8, 10);
        String day2 = date1.substring(8, 10);
        if (Integer.parseInt(year1) > Integer.parseInt(year2)) {
            bigger = true;
        } else if (TextUtils.equals(year1, year2) && Integer.parseInt(month1) > Integer.parseInt(month2)) {
            bigger = true;
        } else if (TextUtils.equals(year1, year2) && TextUtils.equals(month1, month2) && Integer.parseInt(day1) > Integer.parseInt(day2)) {
            bigger = true;
        } else {
            bigger = false;
        }
        return bigger;
    }

    private void sortDatas(List<MerchantItem> datas) {
        for (int i = 1; i < datas.size(); i++) {
            for (int j = 0; j < datas.size() - i - 1; j++) {
                if (compareDates(datas.get(j).getShelveDate(), datas.get(j + 1).getShelveDate())) {
                    MerchantItem temp = datas.get(j);
                    datas.set(j, datas.get(j + 1));
                    datas.set(j + 1, temp);
                }
            }
        }
    }
}
