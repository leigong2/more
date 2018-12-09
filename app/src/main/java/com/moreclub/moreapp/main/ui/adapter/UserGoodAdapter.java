package com.moreclub.moreapp.main.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.information.ui.activity.LiveActivity;
import com.moreclub.moreapp.ucenter.model.UserGoodLike;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailV2Activity;
import com.moreclub.moreapp.util.AppUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017-08-25-0025.
 */

public class UserGoodAdapter extends RecyclerAdapter<UserGoodLike.UserLikeRespsBean> {

    public UserGoodAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, final UserGoodLike.UserLikeRespsBean userLikeRespsBean) {
        holder.setText(R.id.tv_user_name, userLikeRespsBean.getNickName());
        Glide.with(mContext)
                .load(userLikeRespsBean.getThumb())
                .dontAnimate()
                .into((CircleImageView) holder.getView(R.id.civ_user_thumb));
        holder.setOnClickListener(R.id.rl_root, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                Intent intent_merchant = new Intent(mContext, UserDetailV2Activity.class);
                intent_merchant.putExtra("toUid", userLikeRespsBean.getUid() + "");
                ActivityCompat.startActivity(mContext, intent_merchant, compat_merchant.toBundle());
            }
        });
    }
}
