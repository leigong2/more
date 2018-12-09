package com.moreclub.moreapp.main.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.City;
import com.moreclub.moreapp.util.GlideRoundTransform;

import java.util.List;

/**
 * Created by Administrator on 2017/2/27.
 */

public class SelectCityAdapter extends RecyclerAdapter<City> {
    private final static int RADIUS = 4;
    private int radius;

    public SelectCityAdapter(Context context, int layoutId, List<City> datas) {
        super(context, layoutId, datas);
        radius = ScreenUtil.dp2px(context, RADIUS);
    }

    @Override
    public void convert(RecyclerViewHolder holder, City city) {
        Glide.with(mContext)
                .load(city.getThumb())
                .dontAnimate()
                .bitmapTransform(new CenterCrop(mContext),
                        new GlideRoundTransform(mContext, radius, 0))
                .into((ImageView) holder.getView(R.id.selectcity_item_logo));

        holder.setText(R.id.selectcity_item_name, city.getCityName());
        holder.setText(R.id.selectcity_item_ename, city.getCityEname());

        if (city.isSelected()) {
            holder.setAlpha(R.id.selectcity_cover, 0.2f);
        } else {
            holder.setAlpha(R.id.selectcity_cover, 0.7f);
        }
    }
}
