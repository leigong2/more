package com.moreclub.moreapp.main.ui.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.MChannelGoodUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017-07-28-0028.
 */

public class MChannelGoodAdapter extends RecyclerAdapter<MChannelGoodUser> {
    public MChannelGoodAdapter(Context context, int layoutId, List<MChannelGoodUser> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, MChannelGoodUser user) {
        if (user != null && !TextUtils.isEmpty(user.getThumb()))
            Glide.with(mContext)
                    .load(user.getThumb())
                    .dontAnimate()
                    .into((CircleImageView)holder.getView(R.id.civ_user_thumb));
        else if (user != null)
            holder.setImageResource(R.id.civ_user_thumb,R.drawable.profilephoto_small);
        holder.setText(R.id.tv_user_name,user.getNickName());
    }
}
