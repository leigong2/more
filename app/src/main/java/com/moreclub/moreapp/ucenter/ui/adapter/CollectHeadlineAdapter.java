package com.moreclub.moreapp.ucenter.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.ucenter.model.CollectlistInfo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017-05-26.
 */

public class CollectHeadlineAdapter extends RecyclerAdapter<CollectlistInfo> {

    public CollectHeadlineAdapter(Context context, int layoutId, List<CollectlistInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, CollectlistInfo collectlistInfo) {
        String title = collectlistInfo.getTitle();
        String thumb = collectlistInfo.getThumb(); //图片
        String fromNickName = collectlistInfo.getFromNickName();
        String getFromUserThumb = collectlistInfo.getGetFromUserThumb();
        holder.setText(R.id.tv_title,title);
        holder.setText(R.id.tv_user_thumb,fromNickName);
        Glide.with(mContext)
                .load(thumb)
                .dontAnimate()
                .into(((ImageView)holder.getView(R.id.iv_picture_collect)));
        Glide.with(mContext)
                .load(getFromUserThumb)
                .dontAnimate()
                .into(((CircleImageView)holder.getView(R.id.iv_user_thumb)));
    }
}
