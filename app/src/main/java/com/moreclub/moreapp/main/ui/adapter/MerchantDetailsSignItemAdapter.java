package com.moreclub.moreapp.main.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.SignPersion;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailsActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by Captain on 2017/4/27.
 */

public class MerchantDetailsSignItemAdapter extends RecyclerAdapter<SignPersion> {

    private boolean hasSign = false;

    public MerchantDetailsSignItemAdapter(Context context, int layoutId,
                                          List<SignPersion> datas) {
        super(context, layoutId, datas);
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getUid() == MoreUser.getInstance().getUid()) {
                hasSign = true;
            }
        }
        mContext = context;
    }

    @Override
    public void convert(RecyclerViewHolder holder, final SignPersion item) {
        if (item==null)
            return;
        if (!hasSign && item != null && item.getUserAvatar() != null) {
            int radius = 5;
            int margin = 2;
            Glide.with(mContext)
                    .load(item.getUserAvatar())
                    .dontAnimate()
                    .placeholder(R.drawable.profilephoto_small)
                    .bitmapTransform(new BlurTransformation(mContext, radius, margin))
                    .into((CircleImageView) holder.getView(R.id.header_img));
        } else if (item != null && item.getUserAvatar() != null){
            Glide.with(mContext)
                    .load(item.getUserAvatar())
                    .placeholder(R.drawable.profilephoto_small)
                    .dontAnimate()
                    .into((CircleImageView) holder.getView(R.id.header_img));
        } else if (TextUtils.isEmpty(item.getUserAvatar())) {
            CircleImageView view = holder.getView(R.id.header_img);
            view.setImageResource(R.drawable.profilephoto_small);
        }

        long now = Long.valueOf(TimeUtils.getTimestampSecond());
        long time = now - (item.getSigninTime() / 1000);
        SimpleDateFormat sdr = new SimpleDateFormat("MM月dd日");
        java.sql.Date date = new java.sql.Date(item.getSigninTime());
        String format = sdr.format(date);
        if (time < (10 * 60)) {
            holder.setText(R.id.timeText, "刚刚");
        } else if (time > (10 * 60) && time < (60 * 60)) {
            holder.setText(R.id.timeText, (time / (60)) + "分钟");
        } else if (time > (60 * 60) && time < (24 * 60 * 60)) {
            holder.setText(R.id.timeText, (time / (60 * 60)) + "小时");
        } else if (time > (24 * 60 * 60) && time < (3 * 24 * 60 * 60)) {
            holder.setText(R.id.timeText, (time / (24 * 60 * 60)) + "天前");
        } else {
            holder.setText(R.id.timeText, format);
        }
    }
}

