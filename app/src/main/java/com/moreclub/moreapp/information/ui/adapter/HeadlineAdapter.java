package com.moreclub.moreapp.information.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.information.model.Headline;
import com.moreclub.moreapp.information.ui.activity.HeadlineDetailActivity;
import com.moreclub.moreapp.main.ui.activity.MChannelDetailsAcitivy;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017-05-16.
 */

public class HeadlineAdapter extends RecyclerAdapter<Headline.HeadlineDetail> {

    public HeadlineAdapter(Context context, int layoutId, List<Headline.HeadlineDetail> datas) {
        super(context, layoutId, datas);
        sortDatas(mDatas);
    }

    private void sortDatas(List<Headline.HeadlineDetail> datas) {
        for (int i = 1; i < datas.size(); i++) {
            for (int j = 0; j < datas.size() - i - 1; j++) {
                if (datas.get(j).getCreateTime() < datas.get(j + 1).getCreateTime()) {
                    Headline.HeadlineDetail temp = datas.get(j);
                    datas.set(j, datas.get(j + 1));
                    datas.set(j + 1, temp);
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.updatePosition(position);
        Headline.HeadlineDetail headlineDetail = mDatas.get(position);
        convert(holder, headlineDetail);
    }

    @Override
    public void convert(final RecyclerViewHolder holder, final Headline.HeadlineDetail headlineDetail) {
        View rl_type1 = holder.getView(R.id.rl_type1);
        View rl_type2 = holder.getView(R.id.rl_type2);
        switch (headlineDetail.getDisplayModal()) {
            case 0:
                rl_type1.setVisibility(View.VISIBLE);
                rl_type2.setVisibility(View.GONE);
                ImageView itemImage = holder.getView(R.id.iv_headline_top);
                ImageView ivStart = holder.getView(R.id.iv_start_btn);
                Glide.with(mContext)
                        .load(headlineDetail.getTitlePicture())
                        .dontAnimate()
                        .into(itemImage);
                if (headlineDetail.getMediaUrl() != null) {
                    ivStart.setVisibility(View.VISIBLE);
                } else
                    ivStart.setVisibility(View.GONE);
                holder.setText(R.id.tv_user, headlineDetail.getUsername());
                holder.setText(R.id.tv_dec, headlineDetail.getTitle());
                Glide.with(mContext)
                        .load(headlineDetail.getUserthumb())
                        .dontAnimate()
                        .into((CircleImageView) holder.getView(R.id.iv_user_thumb));
                break;
            case 1:
                rl_type1.setVisibility(View.GONE);
                rl_type2.setVisibility(View.VISIBLE);
                ImageView itemImage2 = holder.getView(R.id.iv_headline_top2);
                ImageView ivStart2 = holder.getView(R.id.iv_start_btn2);
                Glide.with(mContext)
                        .load(headlineDetail.getTitlePicture())
                        .dontAnimate()
                        .into(itemImage2);
                if (headlineDetail.getMediaUrl() != null) {
                    ivStart2.setVisibility(View.VISIBLE);
                } else
                    ivStart2.setVisibility(View.GONE);
                holder.setText(R.id.tv_user2, headlineDetail.getUsername());
                holder.setText(R.id.tv_dec2, headlineDetail.getTitle());
                Glide.with(mContext)
                        .load(headlineDetail.getUserthumb())
                        .dontAnimate()
                        .into((CircleImageView) holder.getView(R.id.iv_user_thumb2));
                break;
            default:
                break;
        }
        LinearLayout item = holder.getView(R.id.headline_item_layout);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo 进入资讯详情
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(mContext, MChannelDetailsAcitivy.class);
                intent.putExtra("sid", headlineDetail.getSid());
                ActivityCompat.startActivity(mContext, intent, compat.toBundle());
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

}
