package com.moreclub.moreapp.ucenter.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.ucenter.model.UserSmallImage;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-08-24-0024.
 */

public class RecyclerImagePhotoAdapter extends RecyclerAdapter<UserSmallImage> {

    public RecyclerImagePhotoAdapter(Context context, int layoutId, ArrayList<UserSmallImage> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, UserSmallImage userSmallImage) {
        ImageView view = holder.getView(R.id.iv_simple);
        holder.getView(R.id.camera_layout).setVisibility(View.GONE);
        int screenWidth = ScreenUtil.getScreenWidth(mContext);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(screenWidth / 4, screenWidth / 4);
        lp.setMargins(2, 2, 2, 2);
        view.setLayoutParams(lp);
        Glide.with(mContext)
                .load(userSmallImage.getThumb())
                .dontAnimate()
                .into(view);
        if (userSmallImage.isSelect()) {
            ImageView view_weight = holder.getView(R.id.view_weight);
            FrameLayout.LayoutParams lp0 = new FrameLayout.LayoutParams(screenWidth / 4 - 4, screenWidth / 4 - 4);
            lp0.setMargins(4, 4, 0, 0);
            view_weight.setLayoutParams(lp0);
            view_weight.setImageResource(R.drawable.photo_add_btn);
        }
    }
}
