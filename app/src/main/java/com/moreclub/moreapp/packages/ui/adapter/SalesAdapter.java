package com.moreclub.moreapp.packages.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;
import com.moreclub.moreapp.packages.model.SalesInfo;
import com.moreclub.moreapp.packages.ui.activity.PackageDetailsActivity;
import com.moreclub.moreapp.packages.ui.activity.PackageSuperMainListActivity;
import com.moreclub.moreapp.packages.ui.activity.SalesActivity;
import com.moreclub.moreapp.ucenter.ui.activity.CooperationMerchantActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;

import java.util.List;

/**
 * Created by Administrator on 2017-06-14.
 */

public class SalesAdapter extends RecyclerAdapter<SalesInfo.ListBean> {

    public SalesAdapter(Context context, int layoutId, List<SalesInfo.ListBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, final SalesInfo.ListBean listBean) {
        String[] photos = listBean.getPhotos().split(";");
        Glide.with(mContext)
                .load(photos[0])
                .dontAnimate()
                .into((ImageView) holder.getView(R.id.iv_package_icon));
        holder.setText(R.id.package_title, listBean.getTitle());
        String distance = AppUtils.getDistance(
                MoreUser.getInstance().getUserLocationLat(),
                MoreUser.getInstance().getUserLocationLng(),
                listBean.getLat(), listBean.getLng());
        holder.setText(R.id.tv_distance, listBean.getMerchantName() + " | " + distance);
        float price = Float.parseFloat(listBean.getPrice());
        int newPrice = (int) price;
        if (listBean.getPersonNum() > 0)
            holder.setText(R.id.new_price, "￥" + newPrice + "/" + listBean.getPersonNum() + "位");
        else
            holder.setText(R.id.new_price, "￥" + newPrice);
        TextView oldPrice = holder.getView(R.id.old_price);
        TextPaint paint = oldPrice.getPaint();
        paint.setAntiAlias(true);
        paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        float oprice = Float.parseFloat(listBean.getOldPrice());
        int ooldPrice = (int) oprice;
        oldPrice.setText("￥" + ooldPrice);
        holder.setOnClickListener(R.id.rl_root, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo 套餐详情页
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(mContext,
                        PackageDetailsActivity.class);
                intent.putExtra("pid", "" + listBean.getPid());
                intent.putExtra("mid", "" + listBean.getMerchantId() + "");
                ActivityCompat.startActivity(mContext, intent, compat.toBundle());
            }
        });
    }
}
