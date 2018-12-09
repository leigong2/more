package com.moreclub.moreapp.ucenter.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.util.GlideCircleTransform;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.MerchantDetailsItem;
import com.moreclub.moreapp.ucenter.model.PrivilegeAboutItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Captain on 2017/4/11.
 */

public class PrivilegeAboutAdapter extends RecyclerAdapter<PrivilegeAboutItem> {

    public PrivilegeAboutAdapter(Context context, int layoutId,
                                      List<PrivilegeAboutItem> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, final PrivilegeAboutItem item) {
        ((ImageView) holder.getView(R.id.header_img)).setImageResource(item.getIcon());
        holder.setText(R.id.title, item.getTitle());
        holder.setText(R.id.des, item.getDes());
    }
}
