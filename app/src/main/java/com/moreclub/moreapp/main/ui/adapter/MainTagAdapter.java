package com.moreclub.moreapp.main.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.TagDict;

import java.util.List;

/**
 * Created by Administrator on 2017/2/27.
 */

public class MainTagAdapter extends RecyclerAdapter<TagDict> {

    public MainTagAdapter(Context context, int layoutId, List<TagDict> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, TagDict tagDict) {
        Glide.with(mContext).load(tagDict.getExt()).dontAnimate()
                .into((ImageView) holder.getView(R.id.main_bartag_logo));
        holder.setText(R.id.main_bartag_name, tagDict.getName());
    }
}
