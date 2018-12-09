package com.moreclub.moreapp.ucenter.ui.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;

import java.util.List;

/**
 * Created by Administrator on 2017-08-24-0024.
 */

public class RecyclerPhotoAdapter extends RecyclerAdapter<String> {

    public RecyclerPhotoAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, String s) {
        LinearLayout cameraLayout = holder.getView(R.id.camera_layout);
        ImageView view = holder.getView(R.id.iv_simple);
        if ("+".equals(s)) {
            cameraLayout.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            view.setImageResource(R.drawable.addphoto);
            FrameLayout.LayoutParams lp0 = (FrameLayout.LayoutParams) view.getLayoutParams();
            lp0.gravity = Gravity.CENTER;
            view.setLayoutParams(lp0);
            view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            int screenWidth = ScreenUtil.getScreenWidth(mContext) - ScreenUtil.dp2px(mContext, 32);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(screenWidth / 4 - 4, screenWidth / 4 - 4);
            lp.setMargins(4, 4, 0, 0);
            cameraLayout.setLayoutParams(lp);
        } else {
            cameraLayout.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            int screenWidth = ScreenUtil.getScreenWidth(mContext) - ScreenUtil.dp2px(mContext, 32);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(screenWidth / 4, screenWidth / 4);
            lp.setMargins(2, 2, 2, 2);
            view.setLayoutParams(lp);
            Glide.with(mContext)
                    .load(s)
                    .dontAnimate()
                    .into(view);
        }
    }
}
