package com.moreclub.moreapp.main.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.util.GlideCircleTransform;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.SignInterUser;

import java.util.List;

/**
 * Created by Captain on 2017/7/28.
 */

public class SignInterUserAdater extends RecyclerAdapter<SignInterUser> {


    public SignInterUserAdater(Context context, int layoutId, List<SignInterUser> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, SignInterUser signInterUser) {

        Glide.with(mContext)
                .load(signInterUser.getUserThumb())
                .dontAnimate()
                .transform(new GlideCircleTransform(mContext, 2, 0xF0F0F0))
                .into((ImageView)holder.getView(R.id.user_header));

    }
}
