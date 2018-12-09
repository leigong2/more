package com.moreclub.moreapp.ucenter.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.ucenter.model.CollectlistInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017-05-26.
 */

public class CollectActivityAdapter extends RecyclerAdapter<CollectlistInfo> {

    public CollectActivityAdapter(Context context, int layoutId, List<CollectlistInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(final RecyclerViewHolder holder, final CollectlistInfo activityItem) {
        holder.setText(R.id.tv_activities_title, activityItem.getTitle());
        holder.setText(R.id.tv_activities_desc, activityItem.getIntroduction());
        holder.setText(R.id.tv_activities_location, activityItem.getFromNickName());
        holder.setText(R.id.tv_activities_date, format(activityItem.getFromTime()));
        holder.getView(R.id.iv_activity_logo).setVisibility(View.GONE);
        ((ImageView) holder.getView(R.id.iv_activities_collect)).setImageResource(R.drawable.collect_fav_highlight);
        Glide.with(mContext)
                .load(activityItem.getThumb())
                .dontAnimate()
                .into((ImageView) holder.getView(R.id.iv_activities_picture));

        holder.getView(R.id.tv_activities_deadline).setVisibility(View.GONE);
    }

    private String format(long startTime) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 EE HH:mm");
        Date date = new Date(startTime);
        String formatDate = format.format(date);
        return formatDate;
    }
}
