package com.moreclub.moreapp.main.ui.adapter;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.util.GlideCircleTransform;
import com.moreclub.common.util.MusicUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.MerchantDetailsItem;

import java.util.ArrayList;
import java.util.List;

import static com.moreclub.moreapp.main.constant.Constants.MERCHANT_DETAILS_FACILITIES;
import static com.moreclub.moreapp.main.constant.Constants.MERCHANT_DETAILS_MUSIC;
import static com.moreclub.moreapp.main.constant.Constants.MERCHANT_DETAILS_SCENE;

/**
 * Created by Captain on 2017/3/1.
 */

public class MerchantDetailsItemAdapter extends RecyclerAdapter<MerchantDetailsItem> {
    private int type;
    private Context mContext;

    static boolean isPlayMusic;

    MusicUtils musicTools;
    public MerchantDetailsItemAdapter(Context context, int layoutId,
                                      List<MerchantDetailsItem> datas, int type) {
        super(context, layoutId, datas);
        this.type = type;
        mContext = context;
    }

    @Override
    public void convert(RecyclerViewHolder holder,final MerchantDetailsItem merchantItem) {
        if (type == MERCHANT_DETAILS_SCENE) {
            ArrayList<String> array = merchantItem.getPictures();

            Glide.with(mContext)
                    .load(array.get(0))
                    .dontAnimate()
                    .transform(new GlideCircleTransform(mContext, 2, 0xF0F0F0))
                    .into((ImageView) holder.getView(R.id.main_bartag_logo));
            holder.setText(R.id.main_bartag_name, merchantItem.getName());
        } else if (type == MERCHANT_DETAILS_FACILITIES) {
            Glide.with(mContext).load(merchantItem.getExt())
                    .into((ImageView) holder.getView(R.id.main_bartag_logo));
            holder.setText(R.id.main_bartag_name, merchantItem.getName());
        } else if (type == MERCHANT_DETAILS_MUSIC) {
            if (merchantItem.isExistMusic()) {
                View logoView = holder.getView(R.id.main_bartag_logo);
                View nameView = holder.getView(R.id.main_bartag_name);
                final  ImageView musicBartagBg = holder.getView(R.id.music_bartag_bg);
                final  ImageView musicBartagStart = holder.getView(R.id.music_bartag_start);
                View musicLogoView = holder.getView(R.id.music_bartag_logo);
                View musicNameView = holder.getView(R.id.music_bartag_name);
                logoView.setVisibility(View.GONE);
                nameView.setVisibility(View.GONE);
                musicLogoView.setVisibility(View.VISIBLE);
                musicNameView.setVisibility(View.VISIBLE);

                musicLogoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (musicTools==null){
                            musicTools = new MusicUtils();
                            musicTools.setPlayerUrl(merchantItem.getMusicPlayUrl());
                            musicTools.setListener(new MusicUtils.MusicPlayCompletionListener() {
                                @Override
                                public void musicPlayCompletion() {
                                    musicBartagBg.clearAnimation();
                                    musicBartagStart.setImageResource(R.mipmap.business_play_start);
                                    musicTools.stop();
                                    musicTools = null;
                                }
                            });
                            Animation anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                            anim.setFillAfter(true);
                            anim.setDuration(3000);
                            anim.setRepeatCount(Animation.INFINITE);
                            anim.setInterpolator(new LinearInterpolator());
                            musicBartagBg.startAnimation(anim);
                            musicBartagStart.setImageResource(R.mipmap.business_play_stop);
                            musicTools.playUrl();
                        } else {
                            if (musicTools.isPlayerStatus()) {
                                musicBartagBg.clearAnimation();
                                musicTools.pause();
                                musicBartagStart.setImageResource(R.mipmap.business_play_start);
                            } else {
                                Animation anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                            anim.setFillAfter(true);
                                anim.setDuration(3000);
                                anim.setRepeatCount(Animation.INFINITE);
                                anim.setInterpolator(new LinearInterpolator());
                                musicBartagBg.startAnimation(anim);
                                musicBartagStart.setImageResource(R.mipmap.business_play_stop);
                                musicTools.playUrl();
                            }
                        }
                    }
                });
            } else {
                Glide.with(mContext).load(merchantItem.getThumb())
                        .into((ImageView) holder.getView(R.id.main_bartag_logo));
                holder.setText(R.id.main_bartag_name, merchantItem.getName());
            }
        } else {
            Glide.with(mContext).load(merchantItem.getThumb())
                    .into((ImageView) holder.getView(R.id.main_bartag_logo));
            holder.setText(R.id.main_bartag_name, merchantItem.getName());
        }
    }

    public void musicClose(){
        if (musicTools!=null){
            musicTools.stop();
            musicTools = null;
        }
    }
}
